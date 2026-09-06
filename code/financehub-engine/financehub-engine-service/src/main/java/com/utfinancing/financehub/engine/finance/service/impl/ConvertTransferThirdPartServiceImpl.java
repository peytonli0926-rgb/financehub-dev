package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceEntity;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferThirdPartMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConvertTransferThirdPartServiceImpl extends ServiceImpl<ConvertTransferThirdPartMapper, ConvertTransferThirdPartEntity> implements IConvertTransferThirdPartService {

    private final IOrgCompanyService orgCompanyService;
    private final IContractService contractService;
    private final IContractBalanceService contractBalanceService;

    private final IVoucherService voucherService;
    private final IRuleService ruleService;
    private final IApproveService approveService;

    private final IConvertTransferThirdPartDetailService convertTransferThirdPartDetailService;

    @Value("${approve.url.convert-transfer-third-part-url:null}")
    private String approveUrl;

    @Override
    @Transactional
    public void importFromData(List<ConvertTransferThirdPartDTO> thirdPartDTOS, List<ConvertTransferThirdPartDetailDTO> detailDTOS) {
        List<Pair<ConvertTransferThirdPartEntity, List<ConvertTransferThirdPartDetailEntity>>> pairs = parseImportData(thirdPartDTOS, detailDTOS);
        for (Pair<ConvertTransferThirdPartEntity, List<ConvertTransferThirdPartDetailEntity>> pair : pairs) {
            ConvertTransferThirdPartEntity transfer = pair.getFirst();
            save(transfer);
            List<ConvertTransferThirdPartDetailEntity> transferDetails = pair.getSecond();
            for (ConvertTransferThirdPartDetailEntity detail : transferDetails) {
                detail.setTransferId(transfer.getId());
            }
            convertTransferThirdPartDetailService.saveBatch(transferDetails);
        }

    }

    @Override
    @Transactional
    public Boolean generateVoucher(List<Long> ids) {
        return generateVoucher(ids, YesOrNoEnum.NO);
    }

    @Override
    @Transactional
    public Boolean submit(List<Long> ids) {
        boolean generated = generateVoucher(ids, YesOrNoEnum.YES);
        if (generated) {
            List<ApproveDTO> dtos = new ArrayList<>();
            for (Long id : ids) {
                ApproveDTO dto = new ApproveDTO();
                dto.setDocumentId(id);
                dto.setDocumentType(SceneEnum.DSFZR.getCode());
                dto.setUrl(approveUrl + id);
                dtos.add(dto);
            }
            Map<Long, Long> submit = approveService.submit(dtos);
            for (Long id : ids) {
                lambdaUpdate().set(ConvertTransferThirdPartEntity::getProcessInstanceId, submit.get(id))
                        .set(ConvertTransferThirdPartEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                        .eq(ConvertTransferThirdPartEntity::getId, id)
                        .update(new ConvertTransferThirdPartEntity());
            }
        }
        return generated;

    }

    @Override
    @Transactional
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ConvertTransferThirdPartEntity> transfers = lambdaQuery()
                .in(ConvertTransferThirdPartEntity::getId, ids)
                .eq(ConvertTransferThirdPartEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                .select(Collections.singletonList(ConvertTransferThirdPartEntity::getProcessInstanceId))
                .list();
        if (transfers.size() < ids.size()) {
            throw new ServiceException("只有处理状态为已提交的才可以撤回");
        }

        List<Long> approveIdList = new ArrayList<>();
        for (ConvertTransferThirdPartEntity transfer : transfers) {
            approveIdList.add(transfer.getProcessInstanceId());

        }
        approveService.withdraw(approveIdList);
        voucherService.deleteByBatchIdList(ids, SceneEnum.DSFZR.getCode());
        lambdaUpdate()
                .set(ConvertTransferThirdPartEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode())
                .set(ConvertTransferThirdPartEntity::getProcessInstanceId, null)
                .set(ConvertTransferThirdPartEntity::getVoucherId, null)
                .set(ConvertTransferThirdPartEntity::getErrorInfo, null)
                .in(ConvertTransferThirdPartEntity::getId, ids)
                .update(new ConvertTransferThirdPartEntity());
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        Long count = lambdaQuery()
                .in(ConvertTransferThirdPartEntity::getId, ids)
                .in(ConvertTransferThirdPartEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode())
                .count();
        if (count < ids.size()) {
            throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
        }
        // 删除凭证
        voucherService.deleteByBatchIdList(ids, SceneEnum.DSFZR.getCode());
        // 删除关联的明细
        convertTransferThirdPartDetailService.lambdaUpdate()
                .in(ConvertTransferThirdPartDetailEntity::getTransferId, ids)
                .remove();
        // 删除主表
        removeByIds(ids);

        return true;
    }

    @Override
    public boolean updateProcessStatus(CommonApproveDTO dto) {
        Long documentId = dto.getDocumentId();
        ConvertTransferThirdPartEntity transfer = getOptById(documentId)
                .orElseThrow(() -> new ServiceException("三方转让不存在"));

        String voucherId = transfer.getVoucherId();
        if (StringUtils.isNotBlank(voucherId)) {
            voucherService.updateStatusByids(
                    Arrays.asList(voucherId.split(",")),
                    dto.getDocumentStatus(),
                    dto.getApproverNum(),
                    dto.getApproverName()
            );
        }


        lambdaUpdate()
                .set(ConvertTransferThirdPartEntity::getProcessStatus, dto.getDocumentStatus())
                .set(ConvertTransferThirdPartEntity::getErrorInfo, dto.getRemark())
                .eq(ConvertTransferThirdPartEntity::getId, documentId)
                .update(new ConvertTransferThirdPartEntity());
        return true;
    }

    private boolean generateVoucher(List<Long> ids, YesOrNoEnum code) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请选中一行");
        }
        Pair<List<ConvertTransferThirdPartEntity>, List<ConvertTransferThirdPartDetailEntity>> pair = checkAndPrepare(ids);

        List<Map<String, Object>> voucherMapList = generateVoucherData(pair.getFirst(), pair.getSecond(), code);

        clearOldVoucher(pair, ids);

        List<VoucherInfoVO> vos = ruleService.batchExecuteRule(voucherMapList);

        return afterVoucherGenerate(pair, vos, code);
    }

    private boolean afterVoucherGenerate(Pair<List<ConvertTransferThirdPartEntity>, List<ConvertTransferThirdPartDetailEntity>> pair, List<VoucherInfoVO> vos, YesOrNoEnum code) {
        // 查验数据结果
        boolean isExistVoucherError = vos.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (YesOrNoEnum.YES == code && isExistVoucherError) {
            List<Long> voucherIdList = Lists.newArrayList();
            for (VoucherInfoVO voucherInfoVO : vos) {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).forEach(voucherIdList::add);
                }
            }
            // 异步删除已生成的凭证
            if (CollectionUtils.isNotEmpty(voucherIdList))
                CompletableFuture.runAsync(() -> voucherService.deleteByIdList(voucherIdList));
            return false;
        }

        long count = vos.stream()
                .map(VoucherInfoVO::getVoucherDTOList)
                .filter(Objects::nonNull)
                .mapToLong(Collection::size)
                .sum();
        if (count == 0) {
            return false;
        }

        List<ConvertTransferThirdPartEntity> transfers = pair.getFirst();
        List<ConvertTransferThirdPartDetailEntity> transferDetails = pair.getSecond();

        for (VoucherInfoVO vo : vos) {
            Long detailId = Long.valueOf(vo.getOrderId());
            String voucherIdString = vo.getVoucherDTOList().stream().map(VoucherDTO::getId).map(Object::toString).collect(Collectors.joining(","));
            for (ConvertTransferThirdPartDetailEntity detail : transferDetails) {
                if (detail.getId().equals(detailId)) {
                    detail.setVoucherId(voucherIdString);
                    detail.setErrorInfo(vo.getErrorInfo());
                }
            }
        }
        Map<Long, String> transferVoucherIdlMap = transferDetails.stream()
                .collect(Collectors.groupingBy(ConvertTransferThirdPartDetailEntity::getTransferId, Collectors.mapping(ConvertTransferThirdPartDetailEntity::getVoucherId, Collectors.joining(","))));
        Map<Long, String> transferErrorlMap = transferDetails.stream()
                .collect(Collectors.groupingBy(ConvertTransferThirdPartDetailEntity::getTransferId, Collectors.mapping(ConvertTransferThirdPartDetailEntity::getErrorInfo, Collectors.joining(","))));
        for (ConvertTransferThirdPartEntity transfer : transfers) {
            Long transferId = transfer.getId();
            transfer.setVoucherId(transferVoucherIdlMap.get(transferId));
            transfer.setErrorInfo(transferErrorlMap.get(transferId));
            if (StringUtils.isNotEmpty(transfer.getVoucherId())) {
                transfer.setIsGenerateVoucher(YesOrNoEnum.YES.getCode());
            }
        }
        updateBatchById(transfers);
        convertTransferThirdPartDetailService.updateBatchById(transferDetails);

        return true;
    }

    private void clearOldVoucher(Pair<List<ConvertTransferThirdPartEntity>, List<ConvertTransferThirdPartDetailEntity>> pair, List<Long> ids) {
        List<Long> oldVoucherIds = new ArrayList<>();
        for (ConvertTransferThirdPartEntity transfer : pair.getFirst()) {
            Optional.ofNullable(transfer.getVoucherId())
                    .filter(StringUtils::isNotEmpty)
                    .map(v -> v.split(","))
                    .ifPresent(values -> Arrays.stream(values).map(Long::valueOf).forEach(oldVoucherIds::add));

            transfer.setVoucherId(null);
            transfer.setErrorInfo(null);
        }

        for (ConvertTransferThirdPartDetailEntity detail : pair.getSecond()) {
            detail.setVoucherId(null);
            detail.setErrorInfo(null);
        }

        voucherService.deleteByIdList(oldVoucherIds);
        // 更新主表
        lambdaUpdate()
                .set(ConvertTransferThirdPartEntity::getVoucherId, null)
                .set(ConvertTransferThirdPartEntity::getErrorInfo, null)
                .in(ConvertTransferThirdPartEntity::getId, ids)
                .update(new ConvertTransferThirdPartEntity());
        // 更新详细表
        convertTransferThirdPartDetailService.lambdaUpdate()
                .set(ConvertTransferThirdPartDetailEntity::getVoucherId, null)
                .set(ConvertTransferThirdPartDetailEntity::getErrorInfo, null)
                .in(ConvertTransferThirdPartDetailEntity::getTransferId, ids)
                .update(new ConvertTransferThirdPartDetailEntity());

    }

    private List<Map<String, Object>> generateVoucherData(List<ConvertTransferThirdPartEntity> transfers, List<ConvertTransferThirdPartDetailEntity> transferDetails, YesOrNoEnum code) {
        List<Map<String, Object>> voucherMapList = new ArrayList<>();
        Map<Long, ConvertTransferThirdPartEntity> transferMap = transfers.stream().collect(Collectors.toMap(ConvertTransferThirdPartEntity::getId, Function.identity()));
        for (ConvertTransferThirdPartDetailEntity detail : transferDetails) {
            String orderId = detail.getId().toString();
            ConvertTransferThirdPartEntity transfer = transferMap.get(detail.getTransferId());

            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.DSFZR.getCode());
            executeCommonDTO.setSceneName(SceneEnum.DSFZR.getDesc());
            executeCommonDTO.setBatchId(transfer.getId());
            executeCommonDTO.setBatchType(SceneEnum.DSFZR.getCode());
            executeCommonDTO.setOrderId(orderId);
            executeCommonDTO.setBusinessDate(DateUtils.toDate(transfer.getBusinessDate()));
            executeCommonDTO.setContractCode(detail.getContractCode());
            executeCommonDTO.setClientCode(detail.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setIsSubmit(code.getCode());

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);


            // 评估价
            dataMap.put("estimatePrice", detail.getAppraisedValue());
            // 应收租金
            dataMap.put("receivableLeaseAmount", detail.getReceivableRent());
            // 应收期末残值
            dataMap.put("retainedPrice", detail.getReceivableResidualValue());
            // 应收销项税
            dataMap.put("receivableOuttaxAmount", detail.getReceivableOuttax());
            // 未实现融资租赁收益
            dataMap.put("unrealizedRevenueAmount", detail.getUnrealizedRevenue());
            // 承租人保证金
            dataMap.put("receivableMarginAmount", detail.getLesseeMargin());
            // 基准日后收款
            dataMap.put("receiveUnconfirmed", detail.getBaseDateReceive());

            // 基准日补提拨备 provisionReversalAmount
            dataMap.put("provisionReversalAmount", detail.getSupplementaryProvision());
            // 基准日后收款 receiveUnconfirmed
            dataMap.put("receiveUnconfirmed", detail.getBaseDateReceive());
            // 基准日转让时敞口 financialExpenseAmount
            dataMap.put("financialExpenseAmount", detail.getTransferOpen());


            voucherMapList.add(dataMap);
        }


        return voucherMapList;
    }

    private Pair<List<ConvertTransferThirdPartEntity>, List<ConvertTransferThirdPartDetailEntity>> checkAndPrepare(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<ConvertTransferThirdPartEntity> thirdPartList = listByIds(ids);

        for (ConvertTransferThirdPartEntity thirdPart : thirdPartList) {
            String processStatus = thirdPart.getProcessStatus();
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(processStatus)
                    || ProcessStatusEnum.REJECTED.getCode().equals(processStatus))
            ) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
        }

        List<ConvertTransferThirdPartDetailEntity> thirdPartDetailList = convertTransferThirdPartDetailService.lambdaQuery()
                .in(ConvertTransferThirdPartDetailEntity::getTransferId, ids)
                .list();

        return Pair.of(thirdPartList, thirdPartDetailList);
    }

    private List<Pair<ConvertTransferThirdPartEntity, List<ConvertTransferThirdPartDetailEntity>>> parseImportData(List<ConvertTransferThirdPartDTO> transferDtos, List<ConvertTransferThirdPartDetailDTO> detailDTOS) {

        List<String> contractCodes = new ArrayList<>();
        Map<String, ConvertTransferThirdPartDTO> transferMap = new HashMap<>();
        Map<String, List<ConvertTransferThirdPartDetailDTO>> detailsMap = new HashMap<>();

        validateAndTransfer(transferDtos, detailDTOS, contractCodes, transferMap, detailsMap);

        List<Pair<ConvertTransferThirdPartEntity, List<ConvertTransferThirdPartDetailEntity>>> pairs = new ArrayList<>();

        Map<String, ContractEntity> contractMap = contractService.listClientInfoByCodeList(contractCodes)
                .stream()
                .collect(Collectors.toMap(ContractEntity::getContractCode, Function.identity(), (l, r) -> r));

        for (Map.Entry<String, ConvertTransferThirdPartDTO> entry : transferMap.entrySet()) {
            String key = entry.getKey();
            ConvertTransferThirdPartDTO dto = entry.getValue();
            ConvertTransferThirdPartEntity transfer = dto.toEntity();
            List<ConvertTransferThirdPartDetailDTO> details = detailsMap.get(key);
            Map<String, List<String>> orgContractMap = details
                    .stream()
                    .collect(Collectors.groupingBy(ConvertTransferThirdPartDetailDTO::getOrgId, Collectors.mapping(ConvertTransferThirdPartDetailDTO::getContractCode, Collectors.toList())));
            LocalDate date = transfer.getReferenceDate();
            int periodCode = date.getYear() * 100 + date.getMonthValue();
            List<ContractBalanceEntity> balances = contractBalanceService.selectContractBalanceAfterJZRGroupByOrgIdContractScene(
                    orgContractMap,
                    Arrays.asList("KJFP", "SYJT", "JZJT", "ZLSK"),
                    date,
                    periodCode
            );

            Pair<ConvertTransferThirdPartEntity, List<ConvertTransferThirdPartDetailEntity>> pair = Pair.of(transfer, new ArrayList<>());
            for (ConvertTransferThirdPartDetailDTO detail : details) {
                ConvertTransferThirdPartDetailEntity entity = detail.toEntity();
                ContractEntity contract = contractMap.get(entity.getContractCode());
                entity.setClientCode(contract.getClientCode());
                entity.setClientName(contract.getClientName());
                BigDecimal KJFP = BigDecimal.ZERO, SYJT = BigDecimal.ZERO, JZJT = BigDecimal.ZERO, ZLSK = BigDecimal.ZERO;
                for (ContractBalanceEntity balance : balances) {
                    if ("KJFP".equals(balance.getSceneCode())) {
                        //sum(receivable_service_outtax_amount)
                        // sum(receivable_outtax_amount)
                        // sum(receivable_outtax_debt_restructure_amount)
                        // sum(interest_other_long_payables_amount)
                        // sum(other_payable_spv_amount)
                        // sum(collect_payment_amount)
                        // sum(receive_sum_outtax_amount)
                        // sum(receivable_outtax_investment_property_amount)
                        // sum(other_business_income_amount)
                        KJFP = KJFP
                                .add(balance.getReceivableServiceAmount())
                                .add(balance.getReceivableOuttaxAmount())
                                .add(balance.getReceivableOuttaxDebtRestructureAmount())
                                .add(balance.getInterestOtherLongPayablesAmount())
                                .add(balance.getOtherPayableSpvAmount())
                                .add(balance.getCollectPaymentAmount())
                                .add(balance.getReceiveSumAmount())
                                .add(balance.getReceivableOuttaxInvestmentPropertyAmount())
                                .add(balance.getOtherBusinessIncomeAmount());

                    } else if ("SYJT".equals(balance.getSceneCode())) {
                        // 场景为SYJT，lease_revenue6_amount+lease_revenue_amount
                        SYJT = SYJT.add(balance.getLeaseRevenue6Amount())
                                .add(balance.getLeaseRevenueAmount());
                    } else if ("JZJT".equals(balance.getSceneCode())) {
                        // 场景为JZJT，depreciation_reserves_amount汇总
                        JZJT = JZJT.add(balance.getLeaseRevenueAmount());
                    } else if ("ZLSK".equals(balance.getSceneCode())) {
                        // 场景为ZLSK，receivable_unconfirm_receipt_amount的汇总金额
                        ZLSK = ZLSK.add(balance.getReceivableUnconfirmReceiptBalance());
                    }

                }
                entity.setBaseDateAccruedIncome(SYJT);
                entity.setBaseDateProvision(JZJT);
                entity.setBaseDateReceive(ZLSK);
                entity.setBaseDateInvoices(KJFP);
                pair.getFirst().setTransferParty(contract.getOrgId());
                pair.getSecond().add(entity);
            }
            pair.getFirst().setContractNum(pair.getSecond().size());
            pairs.add(pair);
        }
        return pairs;
    }

    private void validateAndTransfer(List<ConvertTransferThirdPartDTO> transferDtos, List<ConvertTransferThirdPartDetailDTO> detailDTOS, List<String> contractCodes, Map<String, ConvertTransferThirdPartDTO> transferMap, Map<String, List<ConvertTransferThirdPartDetailDTO>> detailsMap) {
        Set<String> batchIdKey = new HashSet<>(), batchIds = new HashSet<>();
        Map<String, String> orgNameIdMap = orgCompanyService.selectAllOrgIdAndName()
                .stream()
                .collect(Collectors.toMap(OrgCompanyVO::getOrgName, OrgCompanyVO::getOrgId, (l, r) -> r));
        // 按照 批次-受让方分组
        for (ConvertTransferThirdPartDTO dto : transferDtos) {
            String transfereeParty = dto.getTransfereeParty();
            dto.setTransfereeParty(orgNameIdMap.get(dto.getTransfereeParty()));
            if (dto.getTransfereeParty() == null) {
                throw new ServiceException("[" + transfereeParty + "]未查询到，请检查");
            }
            String batchKey = dto.getBatch() + dto.getTransfereeParty();
            batchIdKey.add(batchKey);
            batchIds.add(dto.getBatch());
            transferMap.put(batchKey, dto);
        }
        for (ConvertTransferThirdPartDetailDTO dto : detailDTOS) {
            String orgId = dto.getOrgId();
            dto.setOrgId(orgNameIdMap.get(dto.getOrgId()));
            if (dto.getOrgId() == null) {
                throw new ServiceException("[" + orgId + "]未查询到，请检查");
            }
            String key = dto.getBatch() + dto.getOrgId();
            if (!batchIdKey.contains(key)) {
                throw new ServiceException("基本信息表与明细表存在【批次，受让方】不一致，请检查表格，合同号是[" + dto.getContractCode() + "]");
            }
            detailsMap.compute(key, (o, n) -> n == null ? new ArrayList<>() : n).add(dto);
            contractCodes.add(dto.getContractCode());
        }
        if (transferMap.size() != detailsMap.size()) {
            throw new ServiceException("存在【批次，受让方】未填写明细");
        }

        String exsitsBatch = lambdaQuery()
                .in(ConvertTransferThirdPartEntity::getBatch, batchIds)
                .select(Collections.singletonList(ConvertTransferThirdPartEntity::getBatch))
                .list()
                .stream()
                .map(ConvertTransferThirdPartEntity::getBatch)
                .collect(Collectors.joining(","));
        if (!StringUtils.isEmpty(exsitsBatch)) {
            throw new ServiceException("批次[" + exsitsBatch + "]已存在，不能上传");
        }

    }
}
