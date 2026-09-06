package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferEntity;
import com.utfinancing.financehub.engine.finance.entity.DiscountedTransferInternalEntity;
import com.utfinancing.financehub.engine.finance.mapper.DiscountedTransferInternalMapper;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.InternalTransferGenerateDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferService;
import com.utfinancing.financehub.engine.finance.service.IDiscountedTransferInternalService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountedTransferInternalServiceImpl extends ServiceImpl<DiscountedTransferInternalMapper, DiscountedTransferInternalEntity> implements IDiscountedTransferInternalService {

    private final IConvertTransferDetailService convertTransferDetailService;
    private final IConvertTransferService transferService;
    private final IContractBalanceService contractBalanceService;
    private final IRuleService ruleService;
    private final IVoucherService voucherService;
    private final IBankAccountService bankAccountService;
    private final IApproveService approveService;


    @Value("${approve.url.discounted-transfer-internal-url:null}")
    private String approveUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean generatePayment(InternalTransferGenerateDTO dto) {
        List<String> batchList = dto.getBatchList();
        if (ObjectUtil.isEmpty(dto.getFinanceDate())) {
            throw new ServiceException("财务日期不能为空");
        }
        if (CollectionUtils.isEmpty(batchList)) {
            throw new ServiceException("转让批次不能为空");
        }
        List<DiscountedTransferInternalEntity> list = Lists.newArrayList();
        LocalDateTime financeDate = DateUtil.toLocalDateTime(dto.getFinanceDate());
        for (String batch : batchList) {
            List<DiscountedTransferInternalEntity> entityList = lambdaQuery()
                    .eq(DiscountedTransferInternalEntity::getBatch, batch)
                    .in(DiscountedTransferInternalEntity::getProcessStatus, Arrays.asList(ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode()))
                    .list();
            if (CollectionUtils.isNotEmpty(entityList)) {
                throw new ServiceException("转让批次[" + batch +  "]已生成支付信息");
            }
            ConvertTransferEntity transfer = transferService.lambdaQuery()
                    .eq(ConvertTransferEntity::getBatch, batch)
                    .oneOpt()
                    .orElseThrow(() -> new ServiceException("根据转让批次[" + batch + "]没有查询到转让汇总数据"));
            List<ConvertTransferDetailEntity> transferDetails = convertTransferDetailService.lambdaQuery()
                    .eq(ConvertTransferDetailEntity::getBatch, batch)
                    .eq(ConvertTransferDetailEntity::getConvertTransferId, transfer.getId())
                    .list();
            if (CollectionUtils.isEmpty(transferDetails)) {
                throw new ServiceException("根据转让批次[" + batch + "]没有查询到转让明细数据");
            }
            List<String> contractCodeList = transferDetails.stream()
                    .map(ConvertTransferDetailEntity::getContractCode)
                    .collect(Collectors.toList());

            // 合同+签约主体 查余额表：凭证日期<=筛选日期，创建日期最新时collection_transfer_balance余额

            TransferContractBalanceQueryDTO condition = new TransferContractBalanceQueryDTO();
            condition.setOrgId(transfer.getTransferParty());
            condition.setContractCodeList(contractCodeList);
            condition.setVoucherDateEnd(financeDate.toLocalDate());
            Map<String, BigDecimal> contractBalanceEntityMap = contractBalanceService.selectLatestBalanceByOrgIdContractCodeList(condition)
                    .stream()
                    .collect(Collectors.toMap(ContractBalanceVO::getContractCode, ContractBalanceVO::getCollectClaimsBalance, (oldValue, newValue) -> newValue));
            for (ConvertTransferDetailEntity a : transferDetails) {
                DiscountedTransferInternalEntity entity = new DiscountedTransferInternalEntity();
                entity.setBatch(batch);
                entity.setTransferParty(transfer.getTransferParty());
                entity.setContractCode(a.getContractCode());
                entity.setClientCode(a.getClientCode());
                BigDecimal amount = Optional.ofNullable(contractBalanceEntityMap.get(a.getContractCode())).orElse(BigDecimal.ZERO);
                entity.setAmount(amount);
                entity.setFinanceDate(financeDate);
                list.add(entity);
            }
        }
        // 保存
        saveBatch(list);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean importFile(MultipartFile file) {
        ExcelUtil<InternalTransferExcelDTO> util = new ExcelUtil<>(InternalTransferExcelDTO.class);
        List<InternalTransferExcelDTO> list;
        try {
            list = util.importExcel(file.getInputStream());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("没有数据需要上传");
        }
        // 校验数据
        checkDate(list);
        List<DiscountedTransferInternalEntity> updateList = Lists.newArrayList();
        for (InternalTransferExcelDTO importData : list) {
            List<DiscountedTransferInternalEntity> entityList = lambdaQuery()
                    .eq(DiscountedTransferInternalEntity::getBatch, importData.getBatch())
                    .in(DiscountedTransferInternalEntity::getProcessStatus, Arrays.asList(ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode()))
                    .list();
            if (CollectionUtils.isEmpty(entityList)) {
                throw new ServiceException("根据批次号[" + importData.getBatch()  + "]未查询到已录入/已拒绝内部调拨数据");
            }
            for (DiscountedTransferInternalEntity entity : entityList) {
                entity.setBankAccountCode(importData.getBankAccountCode());
                entity.setPaymentDate(DateUtil.toLocalDateTime(importData.getPaymentDate()));
            }
            updateList.addAll(entityList);
        }
        // 更新数据
        updateBatchById(updateList);
        return true;
    }

    @Override
    public Boolean generateVoucher(List<Long> ids, YesOrNoEnum code) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少选择一条数据生成凭证");
        }
        List<DiscountedTransferInternalEntity> entities = this.listByIds(ids);
        for (DiscountedTransferInternalEntity discountedTransferInternalEntity : entities) {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(discountedTransferInternalEntity.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(discountedTransferInternalEntity.getProcessStatus()))) {
                throw new ServiceException("处理状态为已录入或者已拒绝的才可以生成凭证");
            }
            if (ObjectUtil.isEmpty(discountedTransferInternalEntity.getBankAccountCode())
                    || ObjectUtil.isEmpty(discountedTransferInternalEntity.getPaymentDate())) {
                throw new ServiceException("转让批次[" + discountedTransferInternalEntity.getBatch() + "]的银行账号编码或者支付日期为空，请先上传");
            }
        }

        voucherService.deleteByBatchIdList(ids, SceneEnum.ZJZRDB.getCode());
        lambdaUpdate().set(DiscountedTransferInternalEntity::getVoucherId, null)
                .in(DiscountedTransferInternalEntity::getId, ids)
                .update();

        List<Map<String, Object>> voucherMapList = Lists.newArrayList();
        Map<String, DiscountedTransferInternalEntity> entityMap = new HashMap<>();
        for (DiscountedTransferInternalEntity entity : entities) {
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.ZJZRDB.getCode());
            executeCommonDTO.setSceneName(SceneEnum.ZJZRDB.getDesc());
            executeCommonDTO.setBatchId(entity.getId());
            executeCommonDTO.setBatchType(SceneEnum.ZJZRDB.getCode());
            executeCommonDTO.setOrderId(entity.getId().toString());
            executeCommonDTO.setBusinessDate(DateUtils.toDate(entity.getPaymentDate()));
            executeCommonDTO.setContractCode(entity.getContractCode());
            executeCommonDTO.setClientCode(entity.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setIsSubmit(code.getCode());

            entityMap.put(executeCommonDTO.getOrderId(), entity);

            ConvertTransferEntity transfer = transferService.getOne(new LambdaQueryWrapper<ConvertTransferEntity>().eq(ConvertTransferEntity::getBatch, entity.getBatch()));
            if (ObjectUtil.isEmpty(transfer)) {
                throw new ServiceException("根据批次号[" + entity.getBatch() + "]未查询到内部调拨数据");
            }

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("transferType", "折价转让");
            dataMap.put("transferor", transfer.getTransferParty());
            dataMap.put("Transferee", transfer.getTransfereeParty());
            dataMap.put("transferBatch", transfer.getBatch());
            dataMap.put("contractCode", entity.getContractCode());
            dataMap.put("clientCode", entity.getClientCode());
            dataMap.put("paymentAmount", entity.getAmount());

            //查银行账号的组织
            List<BankAccountEntity> bankAccountEntityList = bankAccountService.selectByBankAccountCode(entity.getBankAccountCode());
            if (CollectionUtils.isEmpty(bankAccountEntityList)) {
                throw new ServiceException("根据银行账号编码[" + entity.getBankAccountCode() + "]未查询到银行账号数据");
            }
            BankAccountEntity bankAccountEntity = bankAccountEntityList.get(0);
            // 银行到账主体
            dataMap.put("bankOrgId", bankAccountEntity.getOrgId());
            dataMap.put("ebankNum", bankAccountEntity.getBankAccountNumber());
            voucherMapList.add(dataMap);
        }

        List<VoucherInfoVO> voucherResultList = ruleService.batchExecuteRule(voucherMapList);
        boolean error = voucherResultList.stream().map(VoucherInfoVO::getErrorInfo).allMatch(StringUtils::isNotEmpty);
        if (YesOrNoEnum.YES == code && error) {
            voucherService.deleteByBatchIdList(ids, SceneEnum.ZJZRDB.getCode());
            return Boolean.FALSE;
        }

        for (VoucherInfoVO infoVO : voucherResultList) {
            String errorInfo = "";
            String isGenerateVoucher = YesOrNoEnum.YES.getCode();
            if (StringUtils.isNotEmpty(infoVO.getErrorInfo())) {
                errorInfo = infoVO.getErrorInfo();
                if (infoVO.getErrorInfo().length() > 2000) {
                    errorInfo = infoVO.getErrorInfo().substring(0, 2000);
                }
                isGenerateVoucher = YesOrNoEnum.NO.getCode();
            }
            String voucherIds = Optional.ofNullable(infoVO.getVoucherDTOList())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(VoucherDTO::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            DiscountedTransferInternalEntity internalTransferEntity = entityMap.get(infoVO.getOrderId());
            internalTransferEntity.setAccountDate(LocalDateTime.now());
            internalTransferEntity.setIsGenerateVoucher(isGenerateVoucher);
            internalTransferEntity.setErrorInfo(errorInfo);
            internalTransferEntity.setVoucherId(voucherIds);
            internalTransferEntity.setUpdateTime(null);
            updateById(internalTransferEntity);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<DiscountedTransferInternalEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        for (DiscountedTransferInternalEntity entity : entityList) {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(entity.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            if (ObjectUtil.isEmpty(entity.getBankAccountCode()) || ObjectUtil.isEmpty(entity.getPaymentDate())) {
                throw new ServiceException("转让批次[" + entity.getBatch() + "]的银行账号编码或者支付日期为空，不能提交，请先上传");
            }
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(entity.getId());
            approveDTO.setDocumentType(SceneEnum.ZJZRDB.getCode());
            approveDTO.setUrl(approveUrl + entity.getId());
            approveDTOList.add(approveDTO);
        }

        Boolean generateVoucherFlag = generateVoucher(ids, YesOrNoEnum.YES);
        if (generateVoucherFlag) {
            // 发送审核
            Map<Long, Long> processInstantIdMap = approveService.submit(approveDTOList);

            List<DiscountedTransferInternalEntity> newEntityList = this.listByIds(ids);
            for (DiscountedTransferInternalEntity entity : newEntityList) {
                entity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                if (null != processInstantIdMap && processInstantIdMap.containsKey(entity.getId())) {
                    entity.setProcessInstanceId(processInstantIdMap.get(entity.getId()));
                }
                entity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            }
            // 凭证生成成功
            return this.updateBatchById(newEntityList);
        } else {
            // 凭证生成失败
            throw new ServiceException("凭证生成失败，不能提交");
        }
    }

    @Override
    @Transactional
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<DiscountedTransferInternalEntity> entities = this.listByIds(ids);
        for (DiscountedTransferInternalEntity v : entities) {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            v.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            v.setVoucherId("");
        }
        approveService.withdraw(entities.stream().map(DiscountedTransferInternalEntity::getProcessInstanceId).collect(Collectors.toList()));
        // 删除凭证
        // 逗号拆分
        batchDeleteVoucher(entities);
        return this.updateBatchById(entities);
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<DiscountedTransferInternalEntity> transferRegisterEntityList = this.listByIds(ids);
        for (DiscountedTransferInternalEntity v : transferRegisterEntityList) {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        }
        // 删除凭证
        batchDeleteVoucher(transferRegisterEntityList);
        return removeBatchByIds(ids);
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        DiscountedTransferInternalEntity transfer = getById(approveDTO.getDocumentId());
        if (null == transfer) {
            throw new ServiceException("折价转让调拨数据不存在");
        }
        String processStatus = transfer.getProcessStatus();
        try {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REVIEWED.getCode();
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                processStatus = ProcessStatusEnum.REJECTED.getCode();
            }
            // 修改凭证状态，通过和驳回都修改
            // 逗号拆分
            if (StringUtils.isNotEmpty(transfer.getVoucherId())) {
                voucherService.updateStatusByids(Arrays.asList(transfer.getVoucherId().split(",")), approveDTO.getDocumentStatus(), approveDTO.getApproverNum(), approveDTO.getApproverName());
            }
            transfer.setProcessStatus(processStatus);
            transfer.setErrorInfo("");
        } catch (Exception e) {
            transfer.setErrorInfo(e.getMessage());
        }
        return this.updateById(transfer);
    }

    private void batchDeleteVoucher(List<DiscountedTransferInternalEntity> entities) {
        List<Long> voucherIdList = entities.stream()
                .map(DiscountedTransferInternalEntity::getVoucherId)
                .filter(StringUtils::isNotEmpty)
                .map(v -> v.split(","))
                .flatMap(Arrays::stream)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(voucherIdList)) {
            voucherService.deleteByIdList(voucherIdList);
        }
    }


    /**
     * 校验数据
     *
     * @param list
     */
    private void checkDate(List<InternalTransferExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        for (InternalTransferExcelDTO a : list) {
            if (ObjectUtil.isEmpty(a.getBatch())) {
                throw new ServiceException("转让批次不能为空");
            }
            if (ObjectUtil.isEmpty(a.getPaymentDate())) {
                throw new ServiceException("支付日期不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBankAccountCode())) {
                throw new ServiceException("银行账号编码不能为空");
            }
            // 校验批次号重复
            if (!unionSet.add(a.getBatch())) {
                throw new ServiceException("文件存在重复的批次号[" + a.getBatch() + "],请检查");
            }
            // 校验 银行账号
            List<BankAccountEntity> bankAccountEntityList = bankAccountService.selectByBankAccountCode(a.getBankAccountCode());
            if (CollectionUtils.isEmpty(bankAccountEntityList)) {
                throw new ServiceException("根据银行账号编码[" + a.getBankAccountCode() + "]未查询到银行账号数据");
            }
        }
    }
}
