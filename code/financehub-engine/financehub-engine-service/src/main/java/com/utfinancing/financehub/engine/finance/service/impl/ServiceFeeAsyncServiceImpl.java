package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.FinhubAmountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceFeeAsyncServiceImpl extends ServiceImpl<ServiceFeeMapper, ServiceFeeEntity> implements IServiceFeeAsyncService {

    private final IContractService contractService;
    private final IContractMonthService contractMonthService;
    private final IServiceFeePlanService iServiceFeePlanService;
    private final IServiceFeeDetailsService detailsService;
    private final IVoucherService voucherService;
    private final IRepaymentPlanService repaymentPlanService;
    private final IRepaymentPlanSnapshotService repaymentPlanSnapshotService;
    private IRuleService ruleService;
    private final RemoteDictService remoteDictService;
    @Value("${serviceFeeAllocationStart}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date serviceFeeAllocationStart;


    public R measurementAsync(ServiceFeeQueryDTO queryDTO) {
        BigDecimal allocationRatio = queryDTO.getAllocationRatio();
        Date queryDate = queryDTO.getBusinessDate();

        BigDecimal allocationRatioPer = NumberUtil.div(allocationRatio, 100);
        DateTime endOfMonth = DateUtil.endOfMonth(queryDate);
        DateTime beginOfMonth = DateUtil.beginOfMonth(queryDate);
        queryDate = DateUtil.beginOfDay(endOfMonth);

        // 获取现有数据 排除已经审批的公司
        List<String> notGenerateOrgIds = deleteNotSubmitAndFilterSubmitedOrg(queryDate);
        // 查主合同数据
        try {
            List<ContractMonthEntity> list = contractMonthService.list(new LambdaQueryWrapper<ContractMonthEntity>().
                    isNotNull(ContractMonthEntity::getContractCodeM)
                    .ne(ContractMonthEntity::getContractCodeM, "").and(wrapper -> wrapper.notIn(ContractMonthEntity::getFinancialContractStatus,
                            "亏损结清", "亏损结清、资产处置结束（第三方转让）", "债务重组",
                            "入库后处置", "固定资产入库", "小微子转入", "未入库处置", "正常核销", "正常核销、资产处置结束（第三方转让）",
                            "财务入库", "资产出表（ABS）", "资产处置结束（保理出表）", "资产处置结束（内部转让）",
                            "资产处置结束（第三方转让）", "资产处置结束（资产交易）").or().isNull(ContractMonthEntity::getFinancialContractStatus))
                    .eq(ContractMonthEntity::getEndSharingServiceFeeFlag, false)
//                    .ge(ContractMonthEntity::getLeaseDateEnd, beginOfMonth)
//                    .ge(ContractEntity::getLeaseDateStart,DateUtil.beginOfMonth(DateUtil.parseDate("2025-01-01")))
                    .eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode()));
            List<String> contractCodeMList = list.stream().map(ContractMonthEntity::getContractCodeM).collect(Collectors.toList());

            // 查询合同信息
            List<ContractMonthEntity> contractEntities = contractMonthService.getBaseMapper().selectList(Wrappers.<ContractMonthEntity>lambdaQuery()
                    .in(ContractMonthEntity::getContractCode, contractCodeMList)
                    .notIn(CollectionUtils.isNotEmpty(notGenerateOrgIds), ContractMonthEntity::getOrgId, notGenerateOrgIds)
            );
            // 找出主合同
            List<ContractMonthEntity> mainContractEntityList = Lists.newArrayList();
            Map<String, List<ContractMonthEntity>> contractMap = contractEntities.stream().collect(Collectors.groupingBy(ContractMonthEntity::getContractCode));
            for (String contractCode : contractMap.keySet()) {
                List<ContractMonthEntity> contractEntityList = contractMap.get(contractCode);
                if (contractEntityList.size() == 1) {
                    mainContractEntityList.addAll(contractEntityList);
                } else {
                    ContractMonthEntity contractEntity = contractEntityList.stream().filter(a -> ObjectUtil.isEmpty(a.getContractCodeM())).findFirst().orElse(null);
                    if (ObjectUtil.isNotEmpty(contractEntity)) {
                        // 主合同编号为空的 是主合同
                        mainContractEntityList.add(contractEntity);
                    } else {
                        //都不为空，就取其中的任一个合同
                        mainContractEntityList.add(contractEntityList.get(0));
                    }
                }
            }

            // 重新分摊
            List<ServiceFeePlanEntity> serviceFeePlanEntityList = handleRepaymentPlanOfServiceFeePart(
                    mainContractEntityList, allocationRatioPer, beginOfMonth, queryDate, queryDTO);

            // 过滤出本月的数据
            List<ServiceFeePlanEntity> thisMonthServicePlanList = serviceFeePlanEntityList.stream().filter(
                    e -> DateUtil.isIn(e.getPlanDate(), beginOfMonth, endOfMonth)).collect(Collectors.toList());

            // 生成服务费分摊明细
            List<String> engShareContractCodes = new ArrayList<>();
            List<ServiceFeeDetailsEntity> serviceFeeDetailsEntities = generateServiceFeeDetails(
                    thisMonthServicePlanList, mainContractEntityList, queryDate, allocationRatio, serviceFeePlanEntityList, contractEntities, engShareContractCodes);
            // 服务费分摊详情生成汇总
            generateServiceFee(serviceFeeDetailsEntities, queryDate);
            updateContractEndShareFlag(engShareContractCodes, Integer.parseInt(DateUtil.format(queryDate, "yyyyMM")));
            log.info("咨询服务费测算结束!");
        } catch (Exception e) {
            log.error("measurementAsync fail!", e);
            throw new RuntimeException(e);
        }

        return R.ok();
    }

    private void updateContractEndShareFlag(List<String> engShareContractCodes, Integer period) {
        if (CollectionUtils.isEmpty(engShareContractCodes)) {
            return;
        }
        LambdaUpdateWrapper<ContractMonthEntity> updateContractMonthWrapper = new LambdaUpdateWrapper<>();
        updateContractMonthWrapper.in(ContractMonthEntity::getContractCode, engShareContractCodes);
        contractMonthService.update(ContractMonthEntity.builder().endSharingServiceFeeFlag(true).setEndSharingServiceFeePeriod(period).build(), updateContractMonthWrapper);
        LambdaUpdateWrapper<ContractEntity> updateContractWrapper = new LambdaUpdateWrapper<>();
        updateContractWrapper.in(ContractEntity::getContractCode, engShareContractCodes);
        contractService.update(ContractEntity.builder().endSharingServiceFeeFlag(true).setEndSharingServiceFeePeriod(period).build(), updateContractWrapper);
    }

    private List<String> deleteNotSubmitAndFilterSubmitedOrg(Date queryDate) {
        List<String> notGenerateOrgIds = new ArrayList<>();
        List<ServiceFeeEntity> serviceFeeEntities = getBaseMapper().selectList(
                Wrappers.<ServiceFeeEntity>lambdaQuery().eq(ServiceFeeEntity::getBusinessDate, queryDate));
        if (CollectionUtils.isNotEmpty(serviceFeeEntities)) {
            // 删除未提交数据
            List<Long> deleteIds = serviceFeeEntities.stream().filter(e -> ProcessStatusEnum.getInvalidCode().
                    contains(e.getProcessStatus())).map(ServiceFeeEntity::getId).distinct().collect(Collectors.toList());
            List<Long> logicDeleteIds = serviceFeeEntities.stream().filter(e -> ProcessStatusEnum.WRITEOFF.getCode().equals(e.getProcessStatus())
            ).map(e -> e.getId()).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteIds)) {
                // 删除凭证
                List<String> voucherIdList = detailsService.getBaseMapper().selectList(
                                Wrappers.<ServiceFeeDetailsEntity>lambdaQuery().in(ServiceFeeDetailsEntity::getServiceFeeId, deleteIds))
                        .stream().map(ServiceFeeDetailsEntity::getVoucherId).filter(Objects::nonNull).collect(Collectors.toList());
                List<Long> newVoucherIdList = new ArrayList<>();
                if (!voucherIdList.isEmpty()) {
                    for (String voucherIds : voucherIdList) {
                        if (StringUtils.isNotEmpty(voucherIds)) {
                            newVoucherIdList.addAll(Arrays.asList(voucherIds.split(",")).stream().
                                    map(e -> Long.parseLong(e.trim())).collect(Collectors.toList()));
                        }
                    }
                    voucherService.deleteByIdList(newVoucherIdList);
                }

                LambdaQueryWrapper<ServiceFeeDetailsEntity> detailUpdateChainWrapper = new LambdaQueryWrapper<>();
                detailUpdateChainWrapper.in(ServiceFeeDetailsEntity::getServiceFeeId, deleteIds);
                detailsService.remove(detailUpdateChainWrapper);
                this.removeByIds(deleteIds);
            }
            //已红冲的逻辑删除
            if (CollectionUtils.isNotEmpty(logicDeleteIds)) {
                detailsService.lambdaUpdate().set(ServiceFeeDetailsEntity::getDelFlag, "1").in(ServiceFeeDetailsEntity::getServiceFeeId, logicDeleteIds).update();
                this.lambdaUpdate().set(ServiceFeeEntity::getDelFlag, "1").in(ServiceFeeEntity::getId, logicDeleteIds).update();
            }
            // 获取不生成数据的公司
            notGenerateOrgIds = serviceFeeEntities.stream().filter(e -> ProcessStatusEnum.getCannotModifyCode().
                    contains(e.getProcessStatus())).map(e -> e.getOrgId()).distinct().collect(Collectors.toList());
        }
        return notGenerateOrgIds;
    }

    private List<ServiceFeePlanEntity> handleRepaymentPlanOfServiceFeePart(
            List<ContractMonthEntity> contractEntities, BigDecimal allocationRatioPer, DateTime beginOfMonth, Date queryDate, ServiceFeeQueryDTO queryDTO) {
        List<ServiceFeePlanEntity> list = Lists.newArrayList();
        List<String> noDateList = contractEntities.stream().filter(a -> ObjectUtil.isEmpty(a.getLeaseDateStart())).
                map(ContractMonthEntity::getContractCode).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(noDateList)) {
            throw new ServiceException("合同[" + noDateList.stream().
                    collect(Collectors.joining(",")) + "]无起租日期，不能判断是否当年合同");
        }
        List<String> contractCodeList = contractEntities.stream().map(ContractMonthEntity::getContractCode).
                distinct().collect(Collectors.toList());
        //根据主合同查询出服务费计划表
        List<ServiceFeePlanEntity> serviceFeePlanEntityS = iServiceFeePlanService.list(
                new LambdaQueryWrapper<ServiceFeePlanEntity>().in(ServiceFeePlanEntity::getContractCode, contractCodeList));
        List<ContractMonthEntity> serviceContractS = contractMonthService.list(
                new LambdaQueryWrapper<ContractMonthEntity>().in(ContractMonthEntity::getContractCodeM, contractCodeList));
        List<SysDictData> data = remoteDictService.listDictData(DictTypeEnum.SERVICE_FEE_SPECIAL_CONTRACT_STATUS.getCode()).getData();
        List<String> serviceFeeSpecialContractStatus = data.stream().map(SysDictData::getDictValue).collect(Collectors.toList());
        List<SysDictData> data1 = remoteDictService.listDictData(DictTypeEnum.ALLOCATE_ACROSS_RATIO.getCode()).getData();
        String AllocateAcross = data1.stream().map(SysDictData::getDictValue).findFirst().get();

        for (ContractMonthEntity contractEntity : contractEntities) {
            log.info("handleRepaymentPlanOfServiceFeePart contract code:{}", contractEntity.getContractCode());
            // 查询服务费计划
            List<ServiceFeePlanEntity> serviceFeePlanEntityList = serviceFeePlanEntityS.stream()
                    .filter(a -> ObjectUtil.equals(a.getContractCode(), contractEntity.getContractCode()))
                    .sorted(Comparator.comparingInt(ServiceFeePlanEntity::getPeriods))
                    .collect(Collectors.toList());
            Date contractDate = contractEntity.getLeaseDateStart();
            boolean endSharingServiceFeeFlag = contractEntity.getEndSharingServiceFeeFlag();
            boolean sharingServiceFeeFlag = contractEntity.getSharingServiceFeeFlag();
            boolean initContractFlag = Boolean.FALSE;
            // 查询服务费合同
            List<ContractMonthEntity> serviceContractList = serviceContractS.stream().filter(
                    a -> ObjectUtil.equals(a.getContractCodeM(), contractEntity.getContractCode())).collect(Collectors.toList());
            //跨主体标识
            boolean AllocateAcrossNotPrincipalsFlag = Boolean.FALSE;
            int endSharingServiceFeePeriod = contractEntity.getSetEndSharingServiceFeePeriod();
            if (ObjectUtil.isEmpty(contractDate)) {
                throw new ServiceException("合同[" + contractEntity.getContractCode() + "]无起租日期，不能判断是否当年合同");
            }
            // 判断是否历年合同
//            if (DateUtil.beginOfYear(beginOfMonth).after(DateUtil.beginOfYear(contractDate))) {
//                // 是往年合同，对于往年的服务费，不受当年参数的影响，按照原计划进行分摊
//                list.addAll(serviceFeePlanEntityList);
//                continue;
//            }


            // 判断是否当月起租的合同
            boolean isCurMonthLease = false;
            DateTime endOfMonth = DateUtil.endOfMonth(queryDate);
            if (beginOfMonth.before(contractDate) && endOfMonth.after(contractDate)) {
                isCurMonthLease = true;
            }

            //合同结束直接结转
            Optional<String> serviceOrgId = serviceFeePlanEntityList.stream().map(ServiceFeePlanEntity::getServiceOrgId).distinct().findFirst();
            if ((ContractStatusEnum.HTJS.getDesc().equals(contractEntity.getContractStatus()))
                    && !contractEntity.getEndSharingServiceFeeFlag()
                    && contractEntity.getSetEndSharingServiceFeePeriod() == 0) {
                updateEndSharingFlag(queryDate, contractEntity, null);
                endSharingServiceFeePeriod = Integer.parseInt(DateUtil.format(queryDate, "yyyyMM"));
                endSharingServiceFeeFlag = Boolean.TRUE;
            }

            //参数月份之前起租的合同，均按底稿分摊，系统不参与计算
            if (DateUtil.compare(contractDate, serviceFeeAllocationStart) <= 0) {
                //是往年合同，对于往年的服务费，不受当年参数的影响，按照原计划进行分摊
                Map<String, List<ServiceFeePlanEntity>> collect = serviceFeePlanEntityList.stream().collect(Collectors.groupingBy(v -> v.getServiceOrgId().concat(v.getServiceFeeNo())));
                for (Map.Entry<String, List<ServiceFeePlanEntity>> entry : collect.entrySet()) {
                    List<ServiceFeePlanEntity> v = entry.getValue();
                    BigDecimal beforeCurrentPeriodActualAccruedAmountTotal = BigDecimal.ZERO;
                    BigDecimal currentPeriodPlanAmountTotal = BigDecimal.ZERO;
                    for (ServiceFeePlanEntity serviceFeePlanEntity : v) {
                        currentPeriodPlanAmountTotal = currentPeriodPlanAmountTotal.add(serviceFeePlanEntity.getPlanAmount());
                        if (ObjectUtil.equals(serviceFeePlanEntity.getPlanDate(), queryDate)) {
                            if (endSharingServiceFeeFlag && endSharingServiceFeePeriod == Integer.parseInt(DateUtil.format(queryDate, "yyyyMM"))) {
                                serviceFeePlanEntity.setActualAccruedAmount(serviceFeePlanEntity.getServiceFeeTotalNoTax().subtract(beforeCurrentPeriodActualAccruedAmountTotal));
                            } else if (!endSharingServiceFeeFlag) {
                                if (ContractStatusEnum.HTJS.getDesc().equals(contractEntity.getContractStatus())) {
                                    serviceFeePlanEntity.setActualAccruedAmount(serviceFeePlanEntity.getServiceFeeTotalNoTax().subtract(beforeCurrentPeriodActualAccruedAmountTotal));
                                } else {
                                    serviceFeePlanEntity.setActualAccruedAmount(FinhubAmountUtils.amountNoTax(currentPeriodPlanAmountTotal).subtract(beforeCurrentPeriodActualAccruedAmountTotal));
                                }
                            } else {
                                serviceFeePlanEntity.setActualAccruedAmount(BigDecimal.ZERO);
                            }
                        }
                        serviceFeePlanEntity.setAdjustAmount(serviceFeePlanEntity.getActualAccruedAmount());
                        beforeCurrentPeriodActualAccruedAmountTotal = beforeCurrentPeriodActualAccruedAmountTotal.add(serviceFeePlanEntity.getActualAccruedAmount());
                    }
                    list.addAll(v);
                }
                continue;
            }

            //是否特殊状态调整
            boolean specialContractStatuFlag = contractEntity.getSpecialStatusAdjustmentFlag();
            if (serviceFeeSpecialContractStatus.contains(contractEntity.getFinancialContractStatus())) {
                specialContractStatuFlag = Boolean.TRUE;
                ContractEntity updateContractVo = new ContractEntity();
                updateContractVo.setContractCodeM(StringUtils.isNotEmpty(contractEntity.getContractCodeM()) ? contractEntity.getContractCodeM() : contractEntity.getContractCode());
                updateContractVo.setSpecialStatusAdjustmentFlag(true);
                contractService.updateByContractCodeM(updateContractVo);
                ContractMonthEntity updateMonthContractVo = new ContractMonthEntity();
                updateMonthContractVo.setContractCodeM(StringUtils.isNotEmpty(contractEntity.getContractCodeM()) ? contractEntity.getContractCodeM() : contractEntity.getContractCode());
                updateMonthContractVo.setSpecialStatusAdjustmentFlag(true);
                contractMonthService.updateByContractCodeM(updateMonthContractVo);
            }

            //查询当期以前所有已分摊金额
            List<ServiceFeePlanEntity> beforeCurrentPeriodServiceFeePlanEntityList = serviceFeePlanEntityList.stream().filter(a -> a.getPlanDate().before(queryDate)).collect(Collectors.toList());

            // 查询偿还计划
            List<RepaymentPlanEntity> repayments = repaymentPlanService.selectListPrioritySnapshot(Lists.newArrayList(contractEntity.getContractCode()));

            if (CollUtil.isEmpty(repayments)) {
                // 没有偿还计划，跳过
                log.info("合同没有偿还计划：{}", contractEntity.getContractCode());
                continue;
            }
            // 将偿还计划
            Map<Date, BigDecimal> repaymentMap = repayments.stream().collect(Collectors.toMap(
                    a -> DateUtil.beginOfDay(DateUtil.endOfMonth(a.getPlanDate())),
                    RepaymentPlanEntity::getServiceFeeAmortizationRate, BigDecimal::add, LinkedHashMap::new));

            String noActualServiceAmtContract = serviceContractList.stream().filter(
                    a -> ObjectUtil.isEmpty(a.getActualServiceAmount())).map(ContractMonthEntity::getContractCode).collect(Collectors.joining(","));
            if (ObjectUtil.isNotEmpty(noActualServiceAmtContract)) {
                log.info("服务费合同[" + noActualServiceAmtContract + "]没有实收服务费金额，不能计算计提金额");
            }
            if (CollUtil.isNotEmpty(serviceContractList)) {
                // 有服务费协议合同
                serviceContractList = serviceContractList.stream().sorted(
                        Comparator.comparing(ContractMonthEntity::getContractCode)).collect(Collectors.toList());
            } else {
                // 没有服务费合同，只有一个合同
                serviceContractList.add(contractEntity);
            }

            List<ServiceFeePlanEntity> insertList = Lists.newArrayList();
            List<ServiceFeePlanEntity> updateList = Lists.newArrayList();
            if (CollUtil.isEmpty(serviceFeePlanEntityList) || isCurMonthLease) {
                // 没有生成过服务费，新增
                BigDecimal serviceFeeTotal = serviceContractList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getActualServiceAmount()))
                        .map(ContractMonthEntity::getActualServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal receivableServiceAmountTotal = serviceContractList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getReceivableServiceAmount()))
                        .map(ContractMonthEntity::getReceivableServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, ContractMonthEntity> serviceContractMap = serviceContractList.stream().collect(Collectors.toMap(ContractMonthEntity::getContractCode, a -> a));
                BigDecimal contractDeviceAmount = contractEntity.getPayableDeviceAmount();
                BigDecimal contractConfirmedAmount = NumberUtil.min(NumberUtil.mul(contractDeviceAmount, allocationRatioPer), serviceFeeTotal);
                BigDecimal actualReceiveServiceFeeTotal = BigDecimal.ZERO;
                BigDecimal agreedConfirmedAmountTotal = BigDecimal.ZERO;
                for (ContractMonthEntity serviceContract : serviceContractList) {
                    //同一主体是否分摊, 当月新增合同更新合同表中跨主体分摊标识
                    if (extracted(queryDate, contractEntity, isCurMonthLease, queryDTO, serviceContract)) {
                        AllocateAcrossNotPrincipalsFlag = Boolean.TRUE;
                    }
                    //如果选了跨主体分摊且分摊比例小于配置则不进行分摊
                    if (!AllocateAcrossNotPrincipalsFlag && (serviceFeeTotal.divide(contractDeviceAmount)).compareTo(new BigDecimal(AllocateAcross))>=0){
                        break;
                    }
                    ServiceFeePlanEntity planEntity = new ServiceFeePlanEntity();
                    planEntity.setContractCode(contractEntity.getContractCode());
                    planEntity.setOrgId(contractEntity.getOrgId());
                    planEntity.setServiceFeeNo(serviceContract.getContractCode());
                    planEntity.setServiceOrgId(serviceContract.getOrgId());
                    planEntity.setContractDeviceAmount(contractDeviceAmount);
                    planEntity.setServiceFeeAgreedAmount(serviceContract.getReceivableServiceAmount());
                    planEntity.setActualReceiveServiceFee(serviceContract.getActualServiceAmount());
                    planEntity.setActualReceiveNoTax(FinhubAmountUtils.amountNoTax(serviceContract.getActualServiceAmount()));
                    planEntity.setContractConfirmedAmount(contractConfirmedAmount);
                    actualReceiveServiceFeeTotal = NumberUtil.add(actualReceiveServiceFeeTotal, planEntity.getActualReceiveServiceFee());
                    // 协议一次性确认金额=（实收服务费金额之和与合同一次性确认金额的较小值）-协议一次性确认金额之和
                    BigDecimal agreedConfirmedAmount = NumberUtil.sub(NumberUtil.min(actualReceiveServiceFeeTotal, contractConfirmedAmount), agreedConfirmedAmountTotal);
                    planEntity.setAgreedConfirmedAmount(agreedConfirmedAmount);
                    agreedConfirmedAmountTotal = NumberUtil.add(agreedConfirmedAmountTotal, planEntity.getAgreedConfirmedAmount());
                    planEntity.setAgreedApportionAmount(NumberUtil.sub(planEntity.getActualReceiveServiceFee(), planEntity.getAgreedConfirmedAmount()));
                    planEntity.setServiceFeeTotal(planEntity.getAgreedApportionAmount());
                    planEntity.setServiceFeeTotalNoTax(FinhubAmountUtils.amountNoTax(planEntity.getAgreedApportionAmount()));
                    int periods = 1;
                    List<Date> collect = repaymentMap.keySet().stream().sorted(Comparator.comparing(Date::getTime)).collect(Collectors.toList());
                    BigDecimal planAmountTotal = BigDecimal.ZERO;
                    BigDecimal actualAccruedAmountTotal = BigDecimal.ZERO;
                    for (int i = 0; i < collect.size(); i++) {
                        Date planDate = collect.get(i);
                        BigDecimal rate = repaymentMap.get(planDate);
                        ServiceFeePlanEntity plan = BeanUtil.copyProperties(planEntity, ServiceFeePlanEntity.class);
                        plan.setPlanDate(planDate);
                        plan.setServiceFeeAmortizationRate(rate);
                        plan.setPlanApportionAmount(NumberUtil.mul(planEntity.getAgreedApportionAmount(), plan.getServiceFeeAmortizationRate()));
                        plan.setPlanAmount(NumberUtil.mul(planEntity.getAgreedApportionAmount(), plan.getServiceFeeAmortizationRate()));
                        plan.setPlanAmountNoTax(FinhubAmountUtils.amountNoTax(plan.getPlanAmount()));
                        if (ObjectUtil.equals(planDate, queryDate) && sharingServiceFeeFlag) {
                            // 当月计划，计算金额
                            plan.setActualAccruedAmount(FinhubAmountUtils.amountNoTax(plan.getPlanApportionAmount()));
                            if (AllocateAcrossNotPrincipalsFlag) {
                                plan.setActualAccruedAmount(actualAccruedAmountTotal.multiply(new BigDecimal("-1")));
                            }
                        } else {
                            plan.setActualAccruedAmount(BigDecimal.ZERO);
                        }
                        //特殊状态调整
                        //跨主体不分摊
                        if (specialContractStatuFlag) {
                            plan.setActualAccruedAmount(BigDecimal.ZERO);
                        }

                        plan.setAdjustAmount(plan.getActualAccruedAmount());
                        plan.setPeriods(periods++);
                        insertList.add(plan);
                        list.add(plan);
                        //最后一期处理尾差问题
                        if (i == collect.size() - 1) {
                            plan.setPlanAmount(plan.getAgreedApportionAmount().subtract(planAmountTotal));
                        }
                        planAmountTotal = planAmountTotal.add(plan.getPlanAmount().setScale(2, RoundingMode.HALF_UP));
                        actualAccruedAmountTotal = actualAccruedAmountTotal.add(plan.getActualAccruedAmount());
                    }
                }
                //保存偿还计划快照
                repaymentPlanSnapshotService.remove(new QueryWrapper<>(RepaymentPlanSnapshotEntity.builder().contractCode(contractEntity.getContractCode()).build()));
                repaymentPlanSnapshotService.saveBatch(BeanUtil.copyToList(repayments, RepaymentPlanSnapshotEntity.class));
            } else {
                // 已经生成过服务费，更新
                BigDecimal serviceFeeTotal = serviceContractList.stream().filter(a -> ObjectUtil.isNotEmpty(a.getActualServiceAmount()))
                        .map(ContractMonthEntity::getActualServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal contractDeviceAmount = contractEntity.getPayableDeviceAmount();
                BigDecimal contractConfirmedAmount = NumberUtil.min(NumberUtil.mul(contractDeviceAmount, allocationRatioPer), serviceFeeTotal);
                BigDecimal actualReceiveServiceFeeTotal = BigDecimal.ZERO;
                BigDecimal agreedConfirmedAmountTotal = BigDecimal.ZERO;
                for (ContractMonthEntity serviceContract : serviceContractList) {
                    BigDecimal beforeCurrentPeriodActualAccruedAmountTotal = beforeCurrentPeriodServiceFeePlanEntityList.stream().filter(v -> v.getServiceFeeNo().equals(serviceContract.getContractCode())).map(ServiceFeePlanEntity::getActualAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    ServiceFeePlanEntity planEntity = new ServiceFeePlanEntity();
                    planEntity.setContractCode(contractEntity.getContractCode());
                    planEntity.setOrgId(contractEntity.getOrgId());
                    planEntity.setServiceFeeNo(serviceContract.getContractCode());
                    planEntity.setServiceOrgId(serviceContract.getOrgId());
                    planEntity.setServiceFeeTotal(serviceContract.getReceivableServiceAmount());
                    planEntity.setServiceFeeTotalNoTax(FinhubAmountUtils.amountNoTax(serviceContract.getReceivableServiceAmount()));
                    planEntity.setContractDeviceAmount(contractDeviceAmount);
                    planEntity.setServiceFeeAgreedAmount(serviceContract.getReceivableServiceAmount());
                    planEntity.setActualReceiveServiceFee(serviceContract.getActualServiceAmount());
                    planEntity.setActualReceiveNoTax(FinhubAmountUtils.amountNoTax(serviceContract.getActualServiceAmount()));
                    actualReceiveServiceFeeTotal = NumberUtil.add(actualReceiveServiceFeeTotal, planEntity.getActualReceiveServiceFee());
                    // 协议一次性确认金额=（实收服务费金额之和与合同一次性确认金额的较小值）-协议一次性确认金额之和
                    BigDecimal agreedConfirmedAmount = NumberUtil.sub(NumberUtil.min(actualReceiveServiceFeeTotal, contractConfirmedAmount), agreedConfirmedAmountTotal);
                    planEntity.setContractConfirmedAmount(contractConfirmedAmount);
                    planEntity.setAgreedConfirmedAmount(agreedConfirmedAmount);
                    agreedConfirmedAmountTotal = NumberUtil.add(agreedConfirmedAmountTotal, planEntity.getAgreedConfirmedAmount());
                    planEntity.setAgreedApportionAmount(NumberUtil.sub(planEntity.getActualReceiveServiceFee(), planEntity.getAgreedConfirmedAmount()));
                    BigDecimal adjustAmountTotal = BigDecimal.ZERO;
                    BigDecimal serviceFeeAmortizationRateTotal = BigDecimal.ZERO;
                    BigDecimal actualAccruedAmountTotal = BigDecimal.ZERO;
                    List<ServiceFeePlanEntity> collect = serviceFeePlanEntityList.stream().filter(v -> v.getServiceFeeNo().equals(serviceContract.getContractCode())).collect(Collectors.toList());
                    for (ServiceFeePlanEntity plan : collect) {
                        //累计利率用于回溯计算
                        serviceFeeAmortizationRateTotal = NumberUtil.add(serviceFeeAmortizationRateTotal, plan.getServiceFeeAmortizationRate());
                        if (ObjectUtil.equals(plan.getPlanDate(), queryDate)) {
                            plan.setContractConfirmedAmount(contractConfirmedAmount);
                            plan.setAgreedConfirmedAmount(agreedConfirmedAmount);
                            plan.setAgreedApportionAmount(NumberUtil.sub(planEntity.getActualReceiveServiceFee(), agreedConfirmedAmount));
                            plan.setPlanAmount(NumberUtil.mul(planEntity.getAgreedApportionAmount(), plan.getServiceFeeAmortizationRate()));
                            plan.setPlanAmountNoTax(FinhubAmountUtils.amountNoTax(plan.getPlanAmount()));
                            //是否在当月设置合同结束
                            if (endSharingServiceFeeFlag && endSharingServiceFeePeriod == Integer.parseInt(DateUtil.format(queryDate, "yyyyMM"))) {
                                plan.setActualAccruedAmount(FinhubAmountUtils.amountNoTax(planEntity.getAgreedApportionAmount()).subtract(beforeCurrentPeriodActualAccruedAmountTotal));
                            } else if (!endSharingServiceFeeFlag) {
                                plan.setActualAccruedAmount(NumberUtil.mul(FinhubAmountUtils.amountNoTax(planEntity.getAgreedApportionAmount()), serviceFeeAmortizationRateTotal).subtract(beforeCurrentPeriodActualAccruedAmountTotal));
                            } else {
                                plan.setActualAccruedAmount(BigDecimal.ZERO);
                            }
                            if (plan.getActualAccruedAmount().compareTo(plan.getPlanAmountNoTax()) != 0) {
                                plan.setAccrualType(AccrualTypeEnum.ADJUST.getCode());
                            }
                            //特殊状态调整
                            //跨主体不分摊
                            if (specialContractStatuFlag || AllocateAcrossNotPrincipalsFlag) {
                                plan.setActualAccruedAmount(BigDecimal.ZERO);
                            }

                            plan.setAdjustAmount(plan.getActualAccruedAmount());
                            //单独更新变更的一条
                            updateList.add(plan);
                            list.add(plan);
                        } else {
                            list.add(plan);
                        }
                        actualAccruedAmountTotal = NumberUtil.add(actualAccruedAmountTotal, plan.getActualAccruedAmount());
                        adjustAmountTotal = NumberUtil.add(adjustAmountTotal, plan.getAdjustAmount());
                    }
                }
            }
            // 保存服务费计划
            if (CollUtil.isNotEmpty(insertList)) {
                iServiceFeePlanService.physicalDeleteByContractCode(contractEntity.getContractCode());
                iServiceFeePlanService.saveBatch(insertList);
            }
            if (CollUtil.isNotEmpty(updateList)) {
                iServiceFeePlanService.updateBatchById(updateList);
            }
        }
        log.info("handleRepaymentPlanOfServiceFeePart done!");
        String serviceFeeNoS = list.stream().filter(a -> ObjectUtil.isEmpty(a.getServiceOrgId())).map(ServiceFeePlanEntity::getServiceFeeNo).distinct().collect(Collectors.joining(","));
        if (ObjectUtil.isNotEmpty(serviceFeeNoS)) {
            throw new ServiceException("服务费计划[" + serviceFeeNoS + "] 无服务费签约主体");
        }
        return list;
    }

    private Boolean extracted(Date queryDate, ContractMonthEntity contractEntity, boolean isCurMonthLease, ServiceFeeQueryDTO queryDTO, ContractMonthEntity serviceContract) {
        if (!CommonDateUtils.isPreviousYears(contractEntity.getLeaseDateStart()) && queryDTO.getAllocateAcrossPrincipals().equals(YesOrNoEnum.NO.getCode()) && !StringUtils.equals(serviceContract.getOrgId(), contractEntity.getOrgId())) {
            updateEndSharingFlag(queryDate, contractEntity, Boolean.FALSE);
            return true;
        }
        return false;
    }

    private void updateEndSharingFlag(Date queryDate, ContractMonthEntity contractEntity, Boolean sharingServiceFeeFlag) {
        ContractEntity updateContractVo = new ContractEntity();
        updateContractVo.setContractCodeM(StringUtils.isNotEmpty(contractEntity.getContractCodeM()) ? contractEntity.getContractCodeM() : contractEntity.getContractCode());
        updateContractVo.setEndSharingServiceFeeFlag(true);
        if (sharingServiceFeeFlag != null) updateContractVo.setSharingServiceFeeFlag(sharingServiceFeeFlag);
        updateContractVo.setSetEndSharingServiceFeePeriod(Integer.parseInt(DateUtil.format(queryDate, "yyyyMM")));
        contractService.updateByContractCodeM(updateContractVo);
        ContractMonthEntity updateContractMonthVo = new ContractMonthEntity();
        updateContractMonthVo.setContractCodeM(StringUtils.isNotEmpty(contractEntity.getContractCodeM()) ? contractEntity.getContractCodeM() : contractEntity.getContractCode());
        updateContractMonthVo.setEndSharingServiceFeeFlag(true);
        if (sharingServiceFeeFlag != null) updateContractMonthVo.setSharingServiceFeeFlag(sharingServiceFeeFlag);
        updateContractMonthVo.setSetEndSharingServiceFeePeriod(Integer.parseInt(DateUtil.format(queryDate, "yyyyMM")));
        contractMonthService.updateByContractCodeM(updateContractMonthVo);
    }


    private List<ServiceFeeDetailsEntity> generateServiceFeeDetails(
            List<ServiceFeePlanEntity> thisMonthServicePlanList, List<ContractMonthEntity> contractEntityList, Date queryDate,
            BigDecimal allocationRatio, List<ServiceFeePlanEntity> allPlanEntityList, List<ContractMonthEntity> contractEntities, List<String> engShareContractCodes) {
        List<ServiceFeeDetailsEntity> serviceFeeDetailsEntities = new ArrayList<>();
        thisMonthServicePlanList.forEach(a -> {
            List<ServiceFeePlanEntity> serviceNoList = allPlanEntityList.stream().
                    filter(b -> ObjectUtil.equals(a.getServiceFeeNo(), b.getServiceFeeNo()) &&
                            ObjectUtil.equals(a.getServiceOrgId(), b.getServiceOrgId())).collect(Collectors.toList());
            //上期服务费计划
            Optional<ServiceFeePlanEntity> lastServiceFeePlanEntity = allPlanEntityList.stream()
                    .filter(v -> v.getServiceFeeNo().equals(a.getServiceFeeNo())
                            && ObjectUtil.equals(v.getServiceOrgId(), a.getServiceOrgId()))
                    .filter(v -> v.getPeriods().equals(a.getPeriods() - 1)).findFirst();
            ContractMonthEntity contractEntity = contractEntityList.stream().
                    filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode()) &&
                            ObjectUtil.equals(a.getOrgId(), b.getOrgId())).findFirst().orElse(null);
            ServiceFeeDetailsEntity entity = new ServiceFeeDetailsEntity();
            if (ObjectUtil.isEmpty(contractEntity)) {
                //存在转让的合同
                contractEntity = contractEntities.stream().
                        filter(b -> ObjectUtil.equals(a.getContractCode(), b.getContractCode()) &&
                                ObjectUtil.equals(a.getOrgId(), b.getOrgId())).findFirst().orElse(null);
                if (ObjectUtil.isEmpty(contractEntity)) {
                    log.info("咨询服务费测算时没有找到主合同：{}，{}", a.getContractCode(), a.getOrgId());
                    return;
                }
            }
            String exceptionType = "";
            entity.setBusinessDate(queryDate);
            entity.setContractCode(a.getContractCode());
            // 服务费协议号
            entity.setServiceFeeNo(a.getServiceFeeNo());
            entity.setClientCode(contractEntity.getClientCode());
            entity.setClientName(contractEntity.getClientName());
            entity.setOrgId(contractEntity.getOrgId());
            entity.setServiceOrgId(a.getServiceOrgId());
            entity.setContractStatus(contractEntity.getContractStatus());
            entity.setFinancialContractStatus(contractEntity.getFinancialContractStatus());
            entity.setBusinessCode(contractEntity.getBusinessCode());
            entity.setBusinessName(BusinessEnum.getDescByCode(contractEntity.getBusinessCode()));
            entity.setLeaseDateStart(DateUtil.date(contractEntity.getLeaseDateStart()));
            entity.setLeaseDateEnd(DateUtil.date(contractEntity.getLeaseDateEnd()));
            entity.setAllocationMethod(ObjectUtil.equals(a.getOrgId(), a.getServiceOrgId()) ? "0" : "1");
            entity.setAllocationRatio(allocationRatio);
            entity.setServiceFeeReceived(a.getActualReceiveNoTax());
            entity.setServiceFeeReceivedTaxIncluded(a.getActualReceiveServiceFee());
            entity.setServiceFeeAllocationTaxIncluded(a.getServiceFeeTotal());
            entity.setServiceFeeAllocationNoTax(a.getServiceFeeTotalNoTax());
            // 本月之前所有计提金额之和
            BigDecimal beforeAccruedAmountTotal = serviceNoList.stream().
                    filter(b -> DateUtil.compare(b.getPlanDate(), a.getPlanDate()) < 0).
                    map(ServiceFeePlanEntity::getActualAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (lastServiceFeePlanEntity.isPresent()) {
                entity.setLastMonthServiceFeeAllocationTaxIncluded(lastServiceFeePlanEntity.get().getAgreedApportionAmount());
                entity.setLastMonthServiceFeeAllocationNoTax(FinhubAmountUtils.amountNoTax(lastServiceFeePlanEntity.get().getAgreedApportionAmount()));
                exceptionType += lastServiceFeePlanEntity.get().getActualReceiveServiceFee().compareTo(a.getActualReceiveServiceFee()) != 0 ? "服务费实收变化" : "";
                entity.setReclassificationAdjustmentTaxIncluded(entity.getServiceFeeAllocationTaxIncluded().subtract(entity.getLastMonthServiceFeeAllocationTaxIncluded()));
                entity.setReclassificationAdjustmentNoTaxAmount(entity.getServiceFeeAllocationNoTax().subtract(entity.getLastMonthServiceFeeAllocationNoTax()));
            } else {
                entity.setLastMonthServiceFeeAllocationTaxIncluded(BigDecimal.ZERO);
                entity.setLastMonthServiceFeeAllocationNoTax(BigDecimal.ZERO);
                entity.setReclassificationAdjustmentTaxIncluded(entity.getServiceFeeAllocationTaxIncluded().subtract(entity.getLastMonthServiceFeeAllocationTaxIncluded()));
                entity.setReclassificationAdjustmentNoTaxAmount(entity.getServiceFeeAllocationNoTax().subtract(entity.getLastMonthServiceFeeAllocationNoTax()));
            }
            // 比例调整带来的历史数据调整的金额

            entity.setBeforeXYearMonthAmount(FinhubAmountUtils.amountNoTax(beforeAccruedAmountTotal));
            entity.setXYearMonthAdjustmentAmount(a.getPlanApportionAmount());
            // 本月之后计提金额之和
            BigDecimal laterAccruedAmountTotal = serviceNoList.stream().
                    filter(b -> DateUtil.compare(b.getPlanDate(), a.getPlanDate()) > 0).
                    map(ServiceFeePlanEntity::getActualAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setAllocationAfterXYearMonthBalance(laterAccruedAmountTotal);
            entity.setNotAccruedAmount(laterAccruedAmountTotal);

            // 累计计提金额之和
            BigDecimal accumulatedAccruedAmountTotal = serviceNoList.stream().
                    filter(b -> DateUtil.compare(b.getPlanDate(), a.getPlanDate()) <= 0).
                    map(ServiceFeePlanEntity::getActualAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setAccumulatedAccruedAmount(accumulatedAccruedAmountTotal);
            entity.setPeriods(a.getPeriods());
            entity.setAccrualType(a.getAccrualType());
            entity.setBeforeThisMonthAmountTaxIncluded(FinhubAmountUtils.amountTaxIncluded(beforeAccruedAmountTotal));
            entity.setThisMonthAdjustmentAmountTaxIncluded(a.getPlanApportionAmount());
            entity.setBeforeThisMonthAmountNoTax(beforeAccruedAmountTotal);
            entity.setThisMonthAdjustmentAmountNoTax(a.getAdjustAmount());
            entity.setAllocationBeforeThisMonthBalanceNoTax(entity.getServiceFeeAllocationNoTax().subtract(accumulatedAccruedAmountTotal));
            entity.setAllocationBeforeThisMonthBalanceTaxIncluded(FinhubAmountUtils.amountTaxIncluded(entity.getAllocationBeforeThisMonthBalanceNoTax()));
            exceptionType += entity.getServiceFeeReceived().compareTo(entity.getServiceFeeAllocationNoTax()) < 0 ? "服务费实收小于应分摊服务费收入" : "";
            entity.setExceptionType(exceptionType);
            serviceFeeDetailsEntities.add(entity);
            if (entity.getAllocationBeforeThisMonthBalanceNoTax().compareTo(BigDecimal.ZERO) == 0) {
                engShareContractCodes.add(entity.getContractCode());
            }
        });
        return serviceFeeDetailsEntities;
    }

    private void generateServiceFee(List<ServiceFeeDetailsEntity> serviceFeeDetailsEntities, Date queryDate) {
        List<ServiceFeeEntity> saveServiceFeeEntities = new ArrayList<>();
        List<ServiceFeeDetailsEntity> detailsEntities = new ArrayList<>();

        Map<String, List<ServiceFeeDetailsEntity>> orgGroup = serviceFeeDetailsEntities.stream().collect(Collectors.groupingBy(e -> e.getServiceOrgId()));
        orgGroup.forEach((orgId, feeDetailsEntities) -> {
            Long id = IdWorker.getId();
            ServiceFeeEntity entity = new ServiceFeeEntity();
            entity.setBusinessDate(queryDate);
            entity.setServiceFeeAmortizationIncome(feeDetailsEntities.stream().map(ServiceFeeDetailsEntity::getThisMonthAdjustmentAmountNoTax).reduce(BigDecimal.ZERO, BigDecimal::add));
            entity.setOrgId(orgId);
            entity.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
            entity.setId(id);
            saveServiceFeeEntities.add(entity);
            feeDetailsEntities.forEach(e -> e.setServiceFeeId(id));
            detailsEntities.addAll(feeDetailsEntities);
        });

        this.saveBatch(saveServiceFeeEntities);
        saveServiceFeeEntities.clear();
        detailsService.saveBatch(detailsEntities);
        detailsEntities.clear();
    }

}
