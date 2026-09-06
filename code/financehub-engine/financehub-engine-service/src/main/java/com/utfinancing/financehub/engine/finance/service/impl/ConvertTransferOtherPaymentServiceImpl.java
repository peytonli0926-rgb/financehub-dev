package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
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
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherPaymentEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferOtherPaymentMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPaymentDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferOtherPaymentService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferOtherService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ConvertTransferOtherPaymentServiceImpl extends ServiceImpl<ConvertTransferOtherPaymentMapper, ConvertTransferOtherPaymentEntity> implements IConvertTransferOtherPaymentService {

    private final IConvertTransferOtherService transferOtherService;
    private final IVoucherService voucherService;
    private final IRuleService ruleService;
    private final AsyncTaskExecutor asyncTaskExecutor;
    private final IApproveService approveService;

    public ConvertTransferOtherPaymentServiceImpl(IConvertTransferOtherService transferOtherService, IVoucherService voucherService, IRuleService ruleService, AsyncTaskExecutor asyncTaskExecutor, IApproveService approveService) {
        this.transferOtherService = transferOtherService;
        this.voucherService = voucherService;
        this.ruleService = ruleService;
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.approveService = approveService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importPlan(List<ConvertTransferOtherPaymentDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        Set<String> contractCodes = dtos.stream()
                .map(ConvertTransferOtherPaymentDTO::getContractCode)
                .collect(Collectors.toSet());
        //
        List<SFunction<ConvertTransferOtherPaymentEntity, ?>> contractCode = Collections.singletonList(ConvertTransferOtherPaymentEntity::getContractCode);
        Set<String> submited = lambdaQuery()
                .in(ConvertTransferOtherPaymentEntity::getContractCode, contractCodes)
                .eq(ConvertTransferOtherPaymentEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                .select(contractCode)
                .groupBy(contractCode)
                .list()
                .stream()
                .map(ConvertTransferOtherPaymentEntity::getContractCode)
                .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(submited)) {
            throw new ServiceException("合同编号" + submited + "存在已提交记录，不能上传实付计划");
        }

        lambdaUpdate()
                .in(ConvertTransferOtherPaymentEntity::getContractCode, contractCodes)
                .in(ConvertTransferOtherPaymentEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode())
                .remove();

        // 按合同号升序，创建时间升序,在后面循环设置值的时候 就是取到最新创建的转让计划
        Map<String, List<ConvertTransferOtherEntity>> transferMap = transferOtherService.lambdaQuery()
                .in(ConvertTransferOtherEntity::getContractCode, contractCodes)
                .orderByAsc(ConvertTransferOtherEntity::getContractCode)
                .orderByAsc(ConvertTransferOtherEntity::getCreateTime)
                .list()
                .stream()
                .collect(Collectors.groupingBy(ConvertTransferOtherEntity::getContractCode));
        List<ConvertTransferOtherPaymentEntity> payments = new ArrayList<>();
        for (ConvertTransferOtherPaymentDTO dto : dtos) {
            if (!transferMap.containsKey(dto.getContractCode())) {
                throw new ServiceException("合同号【" + dto.getContractCode() + "】不存在转让数据，不能上传");
            }
            ConvertTransferOtherPaymentEntity payment = dto.toEntity();
            for (ConvertTransferOtherEntity transfer : transferMap.get(dto.getContractCode())) {
                payment.setClientName(transfer.getClientName());
                payment.setClientCode(transfer.getClientCode());
                payment.setContractName(transfer.getContractName());
                payment.setAccountDate(transfer.getAccountDate().toLocalDate());
                payment.setInvoiceFlag(transfer.getInvoiceFlag());
            }

            // 转让方后续开票为“是”，实付利息/1.06*0.06
            if (YesOrNoEnum.YES.getCode().equals(payment.getInvoiceFlag()) && Objects.nonNull(payment.getInterestActual())) {
                payment.setCalculateDeductions(payment.getInterestActual().divide(BigDecimal.valueOf(1.06D)).subtract(BigDecimal.valueOf(0.06D)));
            }else {
                payment.setCalculateDeductions(BigDecimal.ZERO);
            }
            payments.add(payment);
        }

        saveBatch(payments);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importPayment(List<ConvertTransferOtherPaymentDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        Set<String> contractCodes = dtos.stream()
                .map(ConvertTransferOtherPaymentDTO::getContractCode)
                .collect(Collectors.toSet());
        List<ConvertTransferOtherPaymentEntity> payments = lambdaQuery()
                .in(ConvertTransferOtherPaymentEntity::getContractCode, contractCodes)
                .in(ConvertTransferOtherPaymentEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode(), ProcessStatusEnum.REJECTED.getCode())
                .list();
        List<ConvertTransferOtherPaymentEntity> update = new ArrayList<>();
        for (ConvertTransferOtherPaymentDTO dto : dtos) {
            boolean updateFlag = false;
            for (ConvertTransferOtherPaymentEntity payment : payments) {
                if (dto.getContractCode().equals(payment.getContractCode())
                        && dto.getPeriod().equals(payment.getPeriod())) {
                    payment.setInterestActual(dto.getInterestActual());
                    payment.setRentActual(dto.getRentActual());
                    payment.setPrincipalActual(dto.getPrincipalActual());
                    payment.setBankAccountCode(dto.getBankAccountCode());
                    payment.setActualDate(LocalDate.parse(dto.getPaymentDate()));
                    update.add(payment);
                    updateFlag = true;
                    break;
                }
                // 转让方后续开票为“是”，实付利息/1.06*0.06
                if (YesOrNoEnum.YES.getCode().equals(payment.getInvoiceFlag()) && Objects.nonNull(payment.getInterestActual())) {
                    payment.setCalculateDeductions(payment.getInterestActual().divide(BigDecimal.valueOf(1.06D)).subtract(BigDecimal.valueOf(0.06D)));
                }else {
                    payment.setCalculateDeductions(BigDecimal.ZERO);
                }
            }
            if (!updateFlag) {
                throw new ServiceException("【合同编号，期数】【" + dto.getContractCode() + "," + dto.getPeriod() + "】不存在已录入/已拒绝的转付计划，不能上传实付金额");
            }
        }
        updateBatchById(update);
    }

    @Override
    @Transactional
    public Boolean submit(List<Long> ids) {
        return doGenerateVoucher(ids, YesOrNoEnum.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<ConvertTransferOtherPaymentEntity> entities = lambdaQuery()
                .eq(ConvertTransferOtherPaymentEntity::getProcessStatus, ProcessStatusEnum.SUBMITTED.getCode())
                .in(ConvertTransferOtherPaymentEntity::getId, ids)
                .list();

        if (entities.size() < ids.size()) {
            throw new ServiceException("只有处理状态为已提交的才可以撤回");
        }
        List<Long> voucherIds = new ArrayList<>();
        List<Long> processInstanceId = new ArrayList<>();
        for (ConvertTransferOtherPaymentEntity entity : entities) {
            parseVoucherId(entity::getVoucherId, voucherIds);
            entity.setVoucherId(null);
            entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
            entity.setIsGenerateVoucher(YesOrNoEnum.NO.getCode());
            processInstanceId.add(entity.getProcessInstanceId());
        }
        updateBatchById(entities);

        approveService.withdraw(processInstanceId);
        voucherService.deleteByIdList(voucherIds);
        return true;
    }

    private void parseVoucherId(Supplier<String> supplier, List<Long> voucherIds) {
        Optional.ofNullable(supplier.get())
                .filter(StringUtils::isNotBlank)
                .map(v -> v.split(","))
                .ifPresent(v -> Arrays.stream(v)
                        .map(Long::valueOf)
                        .forEach(voucherIds::add)
                );
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据删除");
        }

        List<ConvertTransferOtherPaymentEntity> entityList = lambdaQuery()
                .in(ConvertTransferOtherPaymentEntity::getId, ids)
                .select(Collections.singletonList(ConvertTransferOtherPaymentEntity::getProcessStatus))
                .list();

        List<Long> voucherIds = new ArrayList<>();
        for (ConvertTransferOtherPaymentEntity entity : entityList) {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus())
                    || ProcessStatusEnum.REJECTED.getCode().equals(entity.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
            parseVoucherId(entity::getVoucherId, voucherIds);
        }
        removeBatchByIds(ids);

        // 删除凭证
        voucherService.deleteByIdList(voucherIds);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateProcessStatus(CommonApproveDTO approveDTO) {
        ConvertTransferOtherPaymentEntity payment = getById(approveDTO.getDocumentId());
        if (StringUtils.isNotBlank(payment.getVoucherId())){
            voucherService.updateStatusByids(
                    Arrays.asList(payment.getVoucherId().split(","))
                    , approveDTO.getDocumentStatus()
                    , approveDTO.getApproverNum()
                    , approveDTO.getApproverName()
            );
        }

        payment.setProcessStatus(approveDTO.getDocumentStatus());
        payment.setErrorInfo(approveDTO.getRemark());
        updateById(payment);

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean generateVoucher(List<Long> ids) {
        return doGenerateVoucher(ids, YesOrNoEnum.NO);
    }

    private boolean doGenerateVoucher(List<Long> ids, YesOrNoEnum yesOrNoEnum) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请选中一行");
        }
        List<ConvertTransferOtherPaymentEntity> payments = listByIds(ids);
        List<Map<String, Object>> voucherDataList = new ArrayList<>();
        for (ConvertTransferOtherPaymentEntity payment : payments) {
            ExecuteCommonDTO executeCommonDTO = new ExecuteCommonDTO();
            executeCommonDTO.setSystemCode(SystemEnum.CWZT.getCode());
            executeCommonDTO.setSystemName(SystemEnum.CWZT.getDesc());
            executeCommonDTO.setSceneCode(SceneEnum.QTZF.getCode());
            executeCommonDTO.setSceneName(SceneEnum.QTZF.getDesc());
            executeCommonDTO.setBatchId(payment.getId());
            executeCommonDTO.setBatchType(SceneEnum.QTZF.getCode());
            executeCommonDTO.setOrderId(payment.getId().toString());
            executeCommonDTO.setBusinessDate(DateUtils.toDate(payment.getAccountDate()));
            executeCommonDTO.setContractCode(payment.getContractCode());
            executeCommonDTO.setClientCode(payment.getClientCode());
            executeCommonDTO.setAccountDate(new Date());
            executeCommonDTO.setIsSubmit(yesOrNoEnum.getCode());


            Map<String, Object> dataMap = BeanUtil.beanToMap(executeCommonDTO);
            // 默认：折价转让
            dataMap.put("contractCode", payment.getContractCode());
            dataMap.put("accountDate", payment.getAccountDate());
            dataMap.put("planDate", payment.getPlanDate());
            dataMap.put("period", payment.getPeriod());
            dataMap.put("rentAmount", payment.getRentAmount());
            dataMap.put("principalAmount", payment.getPrincipalAmount());
            dataMap.put("interestAmount", payment.getInterestAmount());
            dataMap.put("rentActual", payment.getRentActual());
            dataMap.put("principalActual", payment.getPrincipalActual());
            dataMap.put("interestActual", payment.getInterestActual());
            dataMap.put("bankAccountCode", payment.getBankAccountCode());
            voucherDataList.add(dataMap);
        }

        List<VoucherInfoVO> voucherInfoVOList = ruleService.batchExecuteRule(voucherDataList);
        // 查验数据结果
        boolean isExistVoucherError = voucherInfoVOList.stream().allMatch(v -> StringUtils.isNotEmpty(v.getErrorInfo()));
        if (isExistVoucherError) {
            List<Long> voucherIdList = Lists.newArrayList();
            for (VoucherInfoVO voucherInfoVO : voucherInfoVOList) {
                if (CollectionUtils.isNotEmpty(voucherInfoVO.getVoucherDTOList())) {
                    voucherInfoVO.getVoucherDTOList().stream().map(VoucherDTO::getId).forEach(voucherIdList::add);
                }
            }
            // 异步删除已生成的凭证
            if (CollectionUtils.isNotEmpty(voucherIdList)) {
                CompletableFuture.runAsync(() -> voucherService.deleteByIdList(voucherIdList), asyncTaskExecutor);
                return false;
            }
        }
        if (YesOrNoEnum.YES == yesOrNoEnum) {
            doSubmit(payments);
        }
        saveVoucher(voucherInfoVOList, payments);
        updateBatchById(payments);
        return true;
    }

    private void doSubmit(List<ConvertTransferOtherPaymentEntity> payments) {
        List<ApproveDTO> dtos = new ArrayList<>();
        for (ConvertTransferOtherPaymentEntity payment : payments) {
            ApproveDTO dto = new ApproveDTO();
            dto.setDocumentType(SceneEnum.QTZF.getCode());
            dto.setDocumentId(payment.getId());
            dto.setUrl("");
            dtos.add(dto);
        }
        Map<Long, Long> submit = approveService.submit(dtos);
        for (ConvertTransferOtherPaymentEntity payment : payments) {
            payment.setProcessInstanceId(submit.get(payment.getId()));
            payment.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        }
    }

    private void saveVoucher(List<VoucherInfoVO> voucherInfoVOList, List<ConvertTransferOtherPaymentEntity> payments) {
        if (voucherInfoVOList == null || voucherInfoVOList.isEmpty()) {
            return;
        }

        for (VoucherInfoVO vo : voucherInfoVOList) {
            String orderId = vo.getOrderId();
            String voucherId = Optional.ofNullable(vo.getVoucherDTOList())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(VoucherDTO::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            for (ConvertTransferOtherPaymentEntity payment : payments) {
                if (payment.getId().toString().equals(orderId)) {
                    payment.setIsGenerateVoucher(YesOrNoEnum.YES.getCode());
                    payment.setVoucherId(voucherId);
                    payment.setErrorInfo(vo.getErrorInfo());
                    payment.setActualDate(payment.getAccountDate());
                    break;
                }
            }

        }
    }
}
