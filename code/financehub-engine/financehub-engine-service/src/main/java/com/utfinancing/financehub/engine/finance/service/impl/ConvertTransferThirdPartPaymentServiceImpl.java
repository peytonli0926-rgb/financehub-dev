package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.SceneEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartPaymentDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartPaymentEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferThirdPartPaymentMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartPaymentGenerateDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartPaymentDetailVO;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartPaymentDetailService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartPaymentService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import java.util.stream.Stream;

@Service
public class ConvertTransferThirdPartPaymentServiceImpl extends ServiceImpl<ConvertTransferThirdPartPaymentMapper, ConvertTransferThirdPartPaymentEntity> implements IConvertTransferThirdPartPaymentService {

    private final IConvertTransferThirdPartDetailService transferThirdPartDetailService;

    private final IConvertTransferThirdPartService transferThirdPartService;

    private final IConvertTransferThirdPartPaymentDetailService convertTransferThirdPartPaymentDetailService;

    private final IOrgCompanyService orgCompanyService;

    private final IApproveService approveService;

    private final IVoucherService voucherService;

    private final IRuleService ruleService;

    private final IBankAccountService bankAccountService;

    @Value("${approve.url.convert-transfer-third-part-payment-url:null}")
    private String approveUrl;


    public ConvertTransferThirdPartPaymentServiceImpl(IConvertTransferThirdPartDetailService transferThirdPartDetailService, IConvertTransferThirdPartService transferThirdPartService, IConvertTransferThirdPartPaymentDetailService convertTransferThirdPartPaymentDetailService, IOrgCompanyService orgCompanyService, IApproveService approveService, IVoucherService voucherService, IRuleService ruleService, IBankAccountService bankAccountService) {
        this.transferThirdPartDetailService = transferThirdPartDetailService;
        this.transferThirdPartService = transferThirdPartService;
        this.convertTransferThirdPartPaymentDetailService = convertTransferThirdPartPaymentDetailService;
        this.orgCompanyService = orgCompanyService;
        this.approveService = approveService;
        this.voucherService = voucherService;
        this.ruleService = ruleService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    @Transactional
    public void generate(ConvertTransferThirdPartPaymentGenerateDTO dto) {
        String batch = dto.getBatch();
        beforeGeneratePayment(dto);
        // 查询三方转让
        Map<String, ConvertTransferThirdPartEntity> transferMap = transferThirdPartService.lambdaQuery()
                .eq(ConvertTransferThirdPartEntity::getBatch, dto.getBatch())
                .list()
                .stream()
                .collect(Collectors.toMap(transfer -> transfer.getBatch() + "-" + transfer.getTransfereeParty(), Function.identity(), (l, r) -> r));
        if (transferMap.isEmpty()) {
            throw new ServiceException("当前批次未上传转让信息");
        }
        // 查询三方转让详细
        Map<String, List<ConvertTransferThirdPartDetailEntity>> detailMap = transferThirdPartDetailService.lambdaQuery()
                .eq(ConvertTransferThirdPartDetailEntity::getBatch, batch)
                .list()
                .stream()
                .collect(Collectors.groupingBy(detail -> detail.getBatch() + "-" + detail.getOrgId()));
        LocalDate now = LocalDate.now();
        List<ConvertTransferThirdPartPaymentDetailEntity> paymentDetails = new ArrayList<>();
        // 生产转付明细
        for (Map.Entry<String, ConvertTransferThirdPartEntity> entry : transferMap.entrySet()) {
            String key = entry.getKey();
            ConvertTransferThirdPartEntity transfer = entry.getValue();
            List<ConvertTransferThirdPartDetailEntity> details = detailMap.get(key);
            if (details == null) {
                String orgName = orgCompanyService.lambdaQuery()
                        .eq(OrgCompanyEntity::getOrgId, transfer.getTransfereeParty())
                        .select(Collections.singletonList(OrgCompanyEntity::getOrgName))
                        .list()
                        .stream()
                        .findFirst()
                        .map(OrgCompanyEntity::getOrgName)
                        .orElse(transfer.getTransfereeParty());
                throw new ServiceException("[批次，受让方]未上传明细，[" + dto.getBatch() + "," + orgName + "]");
            }
            for (ConvertTransferThirdPartDetailEntity detail : details) {
                ConvertTransferThirdPartPaymentDetailEntity paymentDetail = convertTransferThirdPartPaymentDetail(detail);
                paymentDetail.setTradeDate(now);
//                paymentDetail.setPaymentDate(dto.getPaymentDate());
                paymentDetail.setAccountDate(dto.getPaymentDate());
                paymentDetail.setTransferParty(transfer.getTransferParty());
                paymentDetails.add(paymentDetail);
            }
        }
        // 创建支付信息汇总
        ConvertTransferThirdPartPaymentEntity payment = new ConvertTransferThirdPartPaymentEntity();
        payment.setBatch(dto.getBatch());
//        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAccountDate(dto.getPaymentDate());
        payment.setTradeDate(now);
        payment.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        save(payment);
        paymentDetails.forEach(detail -> detail.setPaymentId(payment.getId()));

        convertTransferThirdPartPaymentDetailService.saveBatch(paymentDetails);
    }

    @Override
    @Transactional
    public void importPartPayment(List<ConvertTransferThirdPartPaymentDTO> dtos) {

        Set<String> orgIds = new HashSet<>(), bankAccountCodes = new HashSet<>(), batchIds = new HashSet<>(), orgNames = new HashSet<>();
        Set<LocalDate> paymentDates = new HashSet<>();
        for (ConvertTransferThirdPartPaymentDTO dto : dtos) {
            orgIds.add(dto.getBatch());
            bankAccountCodes.add(dto.getBankAccountCode());
            batchIds.add(dto.getBatch());
            paymentDates.add(LocalDate.parse(dto.getPaymentDate()));
            orgNames.add(dto.getPaymentOrgId());
        }

        List<ConvertTransferThirdPartPaymentEntity> payments = lambdaQuery()
                .in(ConvertTransferThirdPartPaymentEntity::getBatch, batchIds)
                .list();

//        if (payments.isEmpty()) {
//            throw new ServiceException("上传的【批次，支付日期】没有生成支付信息");
//        }

        Map<String, List<BankAccountEntity>> banckAccounts = bankAccountService.lambdaQuery()
                .in(BankAccountEntity::getBankAccountCode, bankAccountCodes)
                .list()
                .stream()
                .collect(Collectors.groupingBy(BankAccountEntity::getBankAccountCode));
        for (String code : bankAccountCodes) {
            if (!banckAccounts.containsKey(code)) {
                throw new ServiceException("银行账户code【" + code + "】未查询到");
            }
        }
        for (ConvertTransferThirdPartPaymentEntity payment : payments) {
            if (!Objects.equals(payment.getProcessStatus(), ProcessStatusEnum.ENTERED.getCode())
                    && !Objects.equals(payment.getProcessStatus(), ProcessStatusEnum.REJECTED.getCode())) {
                throw new ServiceException("【批次，支付日期】不是已录入/已拒绝状态，不能上传，[" + payment.getBatch() + "," + payment.getPaymentDate() + "]");
            }
        }
        List<ConvertTransferThirdPartPaymentDetailEntity> paymentDetails = convertTransferThirdPartPaymentDetailService.lambdaQuery()
                .in(ConvertTransferThirdPartPaymentDetailEntity::getPaymentId, payments.stream().map(ConvertTransferThirdPartPaymentEntity::getId).collect(Collectors.toList()))
                .list();

        Map<String, String> orgNameIdMap = orgCompanyService.lambdaQuery()
                .in(OrgCompanyEntity::getOrgName, orgNames)
                .select(Arrays.asList(OrgCompanyEntity::getOrgName, OrgCompanyEntity::getOrgId))
                .list()
                .stream()
                .collect(Collectors.toMap(OrgCompanyEntity::getOrgName, OrgCompanyEntity::getOrgId, (l, r) -> l));

        List<ConvertTransferThirdPartPaymentEntity> updatePayments = new ArrayList<>();
        for (ConvertTransferThirdPartPaymentEntity payment : payments) {
            for (ConvertTransferThirdPartPaymentDTO dto : dtos) {
                if (match(payment, dto)) {
                    String paymentOrgId = Optional.ofNullable(orgNameIdMap.get(dto.getPaymentOrgId()))
                            .orElseThrow(() -> new ServiceException("支付主体【" + dto.getPaymentOrgId() + "】未找到"));
                    dto.setPaymentOrgId(paymentOrgId);
                    payment.setPaymentOrgId(paymentOrgId);
                    payment.setPaymentDate(LocalDate.parse(dto.getPaymentDate()));
                    payment.setPaymentAmount(dto.getPaymentAmount());
                    payment.setBankAccountCode(dto.getBankAccountCode());
                    updatePayments.add(payment);
                    break;
                }
            }
        }
        List<ConvertTransferThirdPartPaymentDetailEntity> updateDetails = new ArrayList<>();
        for (ConvertTransferThirdPartPaymentDetailEntity detail : paymentDetails) {
            for (ConvertTransferThirdPartPaymentDTO dto : dtos) {
                if (match(detail, dto)) {
                    detail.setPaymentOrgId(dto.getPaymentOrgId());
                    detail.setPaymentDate(LocalDate.parse(dto.getPaymentDate()));
                    detail.setPaymentAmount(dto.getPaymentAmount());
                    detail.setBankAccountCode(dto.getBankAccountCode());
                    updateDetails.add(detail);
                    break;
                }
            }
        }

        if (!updatePayments.isEmpty()) {
            updateBatchById(updatePayments);
        }

        if (!updateDetails.isEmpty()) {
            convertTransferThirdPartPaymentDetailService.updateBatchById(updateDetails);
        }
    }

    @Override
    @Transactional
    public void generateVoucher(List<Long> paymentIds) {
        generateVoucher(paymentIds, YesOrNoEnum.NO);
    }

    @Override
    @Transactional
    public void submit(List<Long> paymentIds) {
        generateVoucher(paymentIds, YesOrNoEnum.YES);
    }

    @Override
    public void withdraw(List<Long> paymentIds) {
        List<Long> approveIdList = lambdaQuery()
                .in(ConvertTransferThirdPartPaymentEntity::getProcessInstanceId, paymentIds)
                .eq(ConvertTransferThirdPartPaymentEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                .select(Collections.singletonList(ConvertTransferThirdPartPaymentEntity::getProcessInstanceId))
                .list()
                .stream()
                .map(ConvertTransferThirdPartPaymentEntity::getProcessInstanceId)
                .collect(Collectors.toList());
        if (approveIdList.size() != paymentIds.size()) {
            throw new ServiceException("只有已提交状态才能撤回");
        }
        Boolean withdraw = approveService.withdraw(paymentIds);
        if (withdraw) {
            lambdaUpdate()
                    .set(ConvertTransferThirdPartPaymentEntity::getProcessInstanceId, null)
                    .set(ConvertTransferThirdPartPaymentEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode())
                    .in(ConvertTransferThirdPartPaymentEntity::getId, paymentIds)
                    .update(new ConvertTransferThirdPartPaymentEntity());
        }
    }

    @Override
    public void updateProcessStatus(CommonApproveDTO dto) {
        Long documentId = dto.getDocumentId();
        ConvertTransferThirdPartPaymentEntity payment = getOptById(documentId)
                .orElseThrow(() -> new ServiceException("支付信息不存在"));
        // 修改凭证状态，通过和驳回都修改
        // 逗号拆分
        if (StringUtils.isNotEmpty(payment.getVoucherId())) {
            voucherService.updateStatusByids(Arrays.asList(payment.getVoucherId().split(",")), dto.getDocumentStatus(), dto.getApproverNum(), dto.getApproverName());
        }
        payment.setProcessStatus(dto.getDocumentStatus());
        payment.setErrorInfo("");
        updateById(payment);

    }

    @Override
    public IPage<ConvertTransferThirdPartPaymentDetailVO> detailPage(Long paymentId, int pageNum, int pageSize) {
        return convertTransferThirdPartPaymentDetailService.lambdaQuery()
                .eq(ConvertTransferThirdPartPaymentDetailEntity::getPaymentId, paymentId)
                .page(new Page<>(pageNum, pageSize))
                .convert(ConvertTransferThirdPartPaymentDetailVO::from);
    }

    @Override
    public List<ConvertTransferThirdPartPaymentDetailVO> detailExport(Long paymentId) {
        return convertTransferThirdPartPaymentDetailService.lambdaQuery()
                .eq(ConvertTransferThirdPartPaymentDetailEntity::getPaymentId, paymentId)
                .list()
                .stream()
                .map(ConvertTransferThirdPartPaymentDetailVO::from)
                .collect(Collectors.toList());
    }

    private void beforeGeneratePayment(ConvertTransferThirdPartPaymentGenerateDTO dto) {
        Long count = lambdaQuery()
                .eq(ConvertTransferThirdPartPaymentEntity::getBatch, dto.getBatch())
                .eq(ConvertTransferThirdPartPaymentEntity::getPaymentDate, dto.getPaymentDate())
                .in(ConvertTransferThirdPartPaymentEntity::getProcessStatus, Arrays.asList(ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.SUBMITTED.getCode(), ProcessStatusEnum.REJECTED.getCode()))
                .count();
        if (count > 0) {
            throw new ServiceException("当前批次得支付日期已存在[" + ProcessStatusEnum.ENTERED.getDesc()
                    + "/" + ProcessStatusEnum.SUBMITTED.getDesc() + "/" + ProcessStatusEnum.REJECTED.getDesc() + "]的记录，不能上传");
        }
    }


    private ConvertTransferThirdPartPaymentDetailEntity convertTransferThirdPartPaymentDetail(ConvertTransferThirdPartDetailEntity entity) {
        ConvertTransferThirdPartPaymentDetailEntity payment = new ConvertTransferThirdPartPaymentDetailEntity();
        payment.setBatch(entity.getBatch());
        payment.setOrgId(entity.getOrgId());
        payment.setContractCode(entity.getContractCode());
        payment.setClientCode(entity.getClientCode());
        payment.setClientName(entity.getClientName());
        return payment;
    }

    private boolean match(ConvertTransferThirdPartPaymentEntity payment, ConvertTransferThirdPartPaymentDTO dto) {
        return Objects.equals(payment.getBatch(), dto.getBatch());
    }

    private boolean match(ConvertTransferThirdPartPaymentDetailEntity payment, ConvertTransferThirdPartPaymentDTO dto) {
        return Objects.equals(payment.getBatch(), dto.getBatch())
                && Objects.equals(payment.getContractCode(), dto.getContractCode());
    }


    private boolean generateVoucher(List<Long> ids, YesOrNoEnum code) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请选中一行");
        }
        Pair<List<ConvertTransferThirdPartPaymentEntity>, List<ConvertTransferThirdPartPaymentDetailEntity>> pair = checkAndPrepare(ids);

        List<Map<String, Object>> voucherMapList = generateVoucherData(pair.getFirst(), pair.getSecond(), code);

        clearOldVoucher(pair, ids);

        List<VoucherInfoVO> vos = ruleService.batchExecuteRule(voucherMapList);

        return afterVoucherGenerate(pair, vos, code);
    }

    private Pair<List<ConvertTransferThirdPartPaymentEntity>, List<ConvertTransferThirdPartPaymentDetailEntity>> checkAndPrepare(List<Long> ids) {
        List<ConvertTransferThirdPartPaymentEntity> payments = lambdaQuery()
                .in(ConvertTransferThirdPartPaymentEntity::getId, ids)
                .in(ConvertTransferThirdPartPaymentEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode())
                .orderByAsc(ConvertTransferThirdPartPaymentEntity::getId)
                .list();
        if (payments.isEmpty() || payments.size() != ids.size()) {
            throw new ServiceException("选择的支付信息不存在或者不是[" + ProcessStatusEnum.ENTERED.getDesc() + "," + ProcessStatusEnum.REJECTED.getDesc() + "]");
        }

        List<ConvertTransferThirdPartPaymentDetailEntity> paymentDetails = convertTransferThirdPartPaymentDetailService.lambdaQuery()
                .in(ConvertTransferThirdPartPaymentDetailEntity::getPaymentId, ids)
                .orderByAsc(ConvertTransferThirdPartPaymentDetailEntity::getPaymentId)
                .list();

        return Pair.of(payments, paymentDetails);
    }

    private List<Map<String, Object>> generateVoucherData(
            List<ConvertTransferThirdPartPaymentEntity> payments,
            List<ConvertTransferThirdPartPaymentDetailEntity> paymentDetails,
            YesOrNoEnum code) {
        List<Map<String, Object>> voucherMapList = new ArrayList<>();
        Map<Long, ConvertTransferThirdPartPaymentEntity> paymentMap = payments.stream().collect(Collectors.toMap(ConvertTransferThirdPartPaymentEntity::getId, Function.identity()));

        for (ConvertTransferThirdPartPaymentDetailEntity detail : paymentDetails) {
            ConvertTransferThirdPartPaymentEntity payment = paymentMap.get(detail.getPaymentId());
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.DSFZF.getCode());
            executeCommonDTO.setSceneName(SceneEnum.DSFZF.getDesc());
            executeCommonDTO.setBatchId(payment.getId());
            executeCommonDTO.setBatchType(SceneEnum.DSFZF.getCode());
            executeCommonDTO.setOrderId(detail.getId().toString());
            executeCommonDTO.setBusinessDate(DateUtils.toDate(payment.getPaymentDate()));
            executeCommonDTO.setContractCode(detail.getContractCode());
            executeCommonDTO.setClientCode(detail.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setIsSubmit(code.getCode());

            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            dataMap.put("paymentAmount", detail.getPaymentAmount());
            for (BankAccountEntity entity : bankAccountService.selectByBankAccountCode(detail.getBankAccountCode())) {
                dataMap.put("ebankNum", entity.getBankAccountNumber());
            }

            voucherMapList.add(dataMap);
        }

        return voucherMapList;
    }

    private void clearOldVoucher(Pair<List<ConvertTransferThirdPartPaymentEntity>, List<ConvertTransferThirdPartPaymentDetailEntity>> pair, List<Long> ids) {
        List<Long> voucherIds = new ArrayList<>();
        for (ConvertTransferThirdPartPaymentEntity payment : pair.getFirst()) {
            Optional.ofNullable(payment.getVoucherId())
                    .map(v -> v.split(","))
                    .ifPresent(idArray -> Arrays.stream(idArray).map(Long::parseLong).forEach(voucherIds::add));
            payment.setVoucherId(null);
            payment.setErrorInfo(null);
        }
        for (ConvertTransferThirdPartPaymentDetailEntity paymentDetail : pair.getSecond()) {
            paymentDetail.setVoucherId(null);
            paymentDetail.setErrorInfo(null);
        }
        voucherService.deleteByIdList(voucherIds);

        lambdaUpdate()
                .set(ConvertTransferThirdPartPaymentEntity::getVoucherId, null)
                .set(ConvertTransferThirdPartPaymentEntity::getErrorInfo, null)
                .in(ConvertTransferThirdPartPaymentEntity::getId, ids)
                .update(new ConvertTransferThirdPartPaymentEntity());

        convertTransferThirdPartPaymentDetailService.lambdaUpdate()
                .set(ConvertTransferThirdPartPaymentDetailEntity::getVoucherId, null)
                .set(ConvertTransferThirdPartPaymentDetailEntity::getErrorInfo, null)
                .in(ConvertTransferThirdPartPaymentDetailEntity::getPaymentId, ids)
                .update(new ConvertTransferThirdPartPaymentDetailEntity());

    }

    private boolean afterVoucherGenerate(Pair<List<ConvertTransferThirdPartPaymentEntity>, List<ConvertTransferThirdPartPaymentDetailEntity>> pair, List<VoucherInfoVO> vos, YesOrNoEnum code) {
        boolean isExistVoucherError = vos.stream().allMatch(v -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(v.getErrorInfo()));
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

        List<ConvertTransferThirdPartPaymentEntity> payments = pair.getFirst();
        List<ConvertTransferThirdPartPaymentDetailEntity> paymentDetails = pair.getSecond();

        Map<Long, Pair<String, String>> paymentInfoMap = new HashMap<>(payments.size());
        for (VoucherInfoVO vo : vos) {
            long detailId = Long.parseLong(vo.getOrderId());

            String voucherIdString = vo.getVoucherDTOList()
                    .stream()
                    .map(VoucherDTO::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            for (ConvertTransferThirdPartPaymentDetailEntity detail : paymentDetails) {
                if (detail.getId() == detailId) {
                    detail.setVoucherId(voucherIdString);
                    Pair<String, String> payment = paymentInfoMap.get(detail.getPaymentId());
                    String e = Optional.ofNullable(vo.getErrorInfo())
                            .orElse("");
                    if (Objects.nonNull(payment)) {
                        String v = payment.getFirst() + "," + voucherIdString;
                        e = Stream.of(e, payment.getSecond())
                                .filter(StringUtils::isNotEmpty)
                                .collect(Collectors.joining(","));
                        paymentInfoMap.put(detail.getPaymentId(), Pair.of(v, e));
                    } else {
                        paymentInfoMap.put(detail.getPaymentId(), Pair.of(voucherIdString, e));
                    }
                    break;
                }
            }
        }
        List<ApproveDTO> dtos = new ArrayList<>();
        for (ConvertTransferThirdPartPaymentEntity payment : payments) {
            Pair<String, String> info = paymentInfoMap.get(payment.getId());
            payment.setVoucherId(info.getFirst());
            payment.setErrorInfo(info.getSecond());
            payment.setIsGenerateVoucher(StringUtils.isEmpty(info.getFirst()) ? YesOrNoEnum.NO.getCode() : YesOrNoEnum.YES.getCode());
            if (code == YesOrNoEnum.YES) {

                ApproveDTO dto = new ApproveDTO();
                dto.setDocumentId(payment.getId());
                dto.setDocumentType(SceneEnum.DSFZR.getCode());
                dto.setUrl(approveUrl + payment.getId());
                dtos.add(dto);
            }
        }
        if (CollectionUtils.isNotEmpty(dtos)) {
            Map<Long, Long> submit = approveService.submit(dtos);
            for (ConvertTransferThirdPartPaymentEntity payment : payments) {
                payment.setProcessInstanceId(submit.get(payment.getId()));
            }
        }

        updateBatchById(payments);
        convertTransferThirdPartPaymentDetailService.updateBatchById(paymentDetails);
        return true;
    }
}
