package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeNewMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.service.IDataExecutionTaskService;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ServiceFeeMeasurementServiceImpl extends ServiceImpl<ServiceFeeNewMapper, ServiceFeeNewEntity> implements IServiceFeeMeasurementService {

    private final IContractService contractService;
    private final IContractMonthService contractMonthService;
    private final IServiceFeePlanNewService serviceFeePlanNewService;
    private final IServiceFeeNewService serviceFeeNewService;
    private final IServiceFeeDetailsNewService detailsNewService;
    private final RemoteDictService remoteDictService;
    private final IVoucherService voucherService;
    private final IDataExecutionTaskService dataExecutionTaskService;
    @Value("${serviceFeeAllocationStart}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date serviceFeeAllocationStart;
    @Value("${unapportionsDate}")
    private String unapportionsDate;

    @Override
    public R measurement(ServiceFeeQueryDTO queryDTO, Long taskId) {
        try {
            // 获取现有数据 排除已经审批的公司
            log.info("0.删除数据");
            queryDTO.setAllocationRatio(NumberUtil.div(queryDTO.getAllocationRatio(), 100));
            List<String> notGenerateOrgIds = deleteNotSubmitAndFilterSubmitedOrg(queryDTO.getBusinessDate());
            // 1.计算咨询服务费计划
            log.info("1、计算咨询服务费计划");
            DateTime endOfMonth = DateUtil.endOfMonth(queryDTO.getBusinessDate());
            serviceFeePlanNewService.calculationServiceFeePlanNew(queryDTO);
            // 2.计算计提金额
            log.info("2、计算计提金额");
            List<ServiceFeePlanNewEntity> feePlanNewEntities = serviceFeePlanNewService.list();
            // 2.1查询所有主合同数据
            log.info("2.1、查询所有主合同数据");
            List<String> contractNos = feePlanNewEntities.stream().map(ServiceFeePlanNewEntity::getContractCode).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<ContractMonthEntity> getMainContractCodeWrapper = new LambdaQueryWrapper<>();
            getMainContractCodeWrapper.in(ContractMonthEntity::getContractCategory, Lists.newArrayList("1", "3"));
            getMainContractCodeWrapper.in(ContractMonthEntity::getContractCode, contractNos);
            getMainContractCodeWrapper.eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            getMainContractCodeWrapper.notIn(CollectionUtils.isNotEmpty(notGenerateOrgIds), ContractMonthEntity::getOrgId, notGenerateOrgIds);
            List<ContractMonthEntity> contractMonthEntities = contractMonthService.list(getMainContractCodeWrapper);
            Map<String, ContractMonthEntity> contractMap = contractMonthEntities.stream().collect(Collectors.toMap(ContractMonthEntity::getContractCode, v -> v, (existing, replacement) -> existing));
            // 2.2查询上期分摊的数据
            log.info("2.2、查询上期分摊的数据");
            LambdaQueryWrapper<ServiceFeeDetailsNewEntity> serviceFeeDetailsWrapper = new LambdaQueryWrapper<>();
            LocalDate lastMonth = CommonDateUtils.getLastMonth(queryDTO.getBusinessDate());
            serviceFeeDetailsWrapper.eq(ServiceFeeDetailsNewEntity::getAccrualYear, lastMonth.getYear());
            serviceFeeDetailsWrapper.eq(ServiceFeeDetailsNewEntity::getAccrualMonth, lastMonth.getMonthValue());
            List<ServiceFeeDetailsNewEntity> lastServiceFeeDetail = detailsNewService.list(serviceFeeDetailsWrapper);
            Map<String, ServiceFeeDetailsNewEntity> lastServerviceFeeDetailsMap = lastServiceFeeDetail.stream().collect(Collectors.toMap(v -> v.getServiceOrgId().concat("@").concat(v.getServiceFeeNo()), v -> v));
            // 2.3查询特殊合同状态
            log.info("2.3、查询特殊合同状态");
            List<SysDictData> data = remoteDictService.listDictData(DictTypeEnum.SERVICE_FEE_SPECIAL_CONTRACT_STATUS.getCode()).getData();
            List<String> serviceFeeSpecialContractStatus = data.stream().map(SysDictData::getDictValue).collect(Collectors.toList());
            List<SysDictData> data1 = remoteDictService.listDictData(DictTypeEnum.ALLOCATE_ACROSS_RATIO.getCode()).getData();
            String allocateAcrossRatio = data1.stream().map(SysDictData::getDictValue).findFirst().get();
            // 2.4计算每期计提数
            log.info("2.4、计算每期计提数");
            List<ServiceFeeDetailsNewEntity> detailsNewEntities = new ArrayList<>();
            Map<String, List<ServiceFeePlanNewEntity>> feePlanNewGbContractCodeMap = feePlanNewEntities.stream().collect(Collectors.groupingBy(ServiceFeePlanNewEntity::getContractCode));
            List<ContractEntity> updateContractEntities = new ArrayList<>();
            int total = feePlanNewGbContractCodeMap.entrySet().size();
            int count = 0;
            for (Map.Entry<String, List<ServiceFeePlanNewEntity>> entry : feePlanNewGbContractCodeMap.entrySet()) {
                ContractEntity updateContractEntitie = new ContractEntity();

                //是否特殊状态调整
                updateContractEntitie.setId(contractMap.get(entry.getKey()).getId());
                updateContractEntitie.setContractCode(entry.getKey());
                boolean specialContractStatuFlag = contractMap.get(entry.getKey()).getSpecialStatusAdjustmentFlag();
                if (serviceFeeSpecialContractStatus.contains(contractMap.get(entry.getKey()).getFinancialContractStatus())) {
                    specialContractStatuFlag = Boolean.TRUE;
                    updateContractEntitie.setSpecialStatusAdjustmentFlag(true);
                }
                boolean finalSpecialContractStatuFlag = specialContractStatuFlag;
                entry.getValue().stream()
                        .collect(Collectors.groupingBy(v -> v.getServiceOrgId().concat("@").concat(v.getServiceFeeNo())))
                        .forEach((serviceFeeNoAndServiceOrgId, serviceFeePlanNewEntities) -> {
                            BigDecimal receivableServiceFeeTaxIncludeTotal = serviceFeePlanNewEntities.stream().filter(a -> ObjectUtil.isNotEmpty(a.getReceivableServiceFeeTaxInclude()))
                                    .map(ServiceFeePlanNewEntity::getReceivableServiceFeeTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                            Optional<ServiceFeePlanNewEntity> first = serviceFeePlanNewEntities.stream().filter(v -> v.getPlanDate().equals(DateUtil.beginOfDay(endOfMonth))).findFirst();
                            Integer lastPeriod = serviceFeePlanNewEntities.stream().max(Comparator.comparingInt(ServiceFeePlanNewEntity::getPeriods)).get().getPeriods();
                            ContractMonthEntity contractMonthEntity = contractMap.get(entry.getKey());
                            if (first.isPresent()) {
                                ServiceFeePlanNewEntity thisPeriodServiceFeePlan = first.get();
//                                ServiceFeePlanNewEntity lastPeriodServiceFeePlan = serviceFeePlanNewEntities.stream().filter(v -> v.getPeriods().equals(thisPeriodServiceFeePlan.getPeriods() - 1)).findFirst().get();
                                //是否最后一期
                                boolean isLastPeriod = Objects.equals(lastPeriod, thisPeriodServiceFeePlan.getPeriods());
                                boolean isHtjs = ContractStatusEnum.HTJS.getDesc().equals(contractMonthEntity.getContractStatus());
                                if (CommonDateUtils.isPreviousYears(contractMonthEntity.getLeaseDateStart()) || YesOrNoEnum.YES.getCode().equals(contractMonthEntity.getServiceFeeHistoryFlag())) {
                                    ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, endOfMonth, lastServerviceFeeDetailsMap.get(serviceFeeNoAndServiceOrgId), contractMonthEntity, isLastPeriod, finalSpecialContractStatuFlag);
                                    setServiceFeeFlag(serviceFeeDetailsNewEntity, YesOrNoEnum.getCodeByBool(contractMonthEntity.getSharingServiceFeeFlag()), finalSpecialContractStatuFlag, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                                    detailsNewEntities.add(serviceFeeDetailsNewEntity);
                                    //记录合同分摊状态变更
                                    setContractFlag(updateContractEntitie, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0, finalSpecialContractStatuFlag, true);
                                    updateContractEntities.add(updateContractEntitie);
                                } else {
                                    ServiceFeeDetailsNewEntity lastServiceFeeDetailsNewEntity = lastServerviceFeeDetailsMap.get(serviceFeeNoAndServiceOrgId);
                                    //同一主体分摊
                                    if (thisPeriodServiceFeePlan.getServiceOrgId().equals(thisPeriodServiceFeePlan.getOrgId())) {
                                        ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, endOfMonth, lastServiceFeeDetailsNewEntity, contractMonthEntity, isLastPeriod, finalSpecialContractStatuFlag);
                                        setServiceFeeFlag(serviceFeeDetailsNewEntity, YesOrNoEnum.getCodeByBool(contractMonthEntity.getSharingServiceFeeFlag()), finalSpecialContractStatuFlag, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                                        detailsNewEntities.add(serviceFeeDetailsNewEntity);
                                        //记录合同分摊状态变更
                                        setContractFlag(updateContractEntitie, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0, finalSpecialContractStatuFlag, true);
                                        updateContractEntities.add(updateContractEntitie);
                                    } else {
                                        //跨主体逻辑
                                        //跨主体是否分摊
                                        if (queryDTO.getAllocateAcrossPrincipals().equals(YesOrNoEnum.YES.getCode())) {
                                            //选择跨主体分摊逻辑
                                            if (lastServiceFeeDetailsNewEntity != null && lastServiceFeeDetailsNewEntity.getAllocateAcrossPrincipals().equals(YesOrNoEnum.NO.getCode())) {
                                                ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, endOfMonth, lastServiceFeeDetailsNewEntity, contractMonthEntity, isLastPeriod, finalSpecialContractStatuFlag);
                                                //重新累计计提数
                                                BigDecimal accrualAmount = serviceFeeDetailsNewEntity.getAccruedAmount();
                                                resetAccrualAmount(serviceFeeDetailsNewEntity, accrualAmount, lastServiceFeeDetailsNewEntity, thisPeriodServiceFeePlan, isLastPeriod || isHtjs, finalSpecialContractStatuFlag);
                                                setServiceFeeFlag(serviceFeeDetailsNewEntity, YesOrNoEnum.getCodeByBool(contractMonthEntity.getSharingServiceFeeFlag()), finalSpecialContractStatuFlag, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                                                detailsNewEntities.add(serviceFeeDetailsNewEntity);
                                                updateContractEntitie.setEndSharingServiceFeeFlag(serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0);
                                            } else {
                                                //上一期仍为是或是第一期,继续原分摊逻辑
                                                ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, endOfMonth, lastServiceFeeDetailsNewEntity, contractMonthEntity, isLastPeriod, finalSpecialContractStatuFlag);
                                                serviceFeeDetailsNewEntity.setSharingServiceFeeFlag(YesOrNoEnum.YES.getCode());
                                                serviceFeeDetailsNewEntity.setEndSharingServiceFeeFlag(serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                                                detailsNewEntities.add(serviceFeeDetailsNewEntity);
                                                updateContractEntitie.setEndSharingServiceFeeFlag(serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0);
                                            }
                                            //记录合同分摊状态变更
                                            updateContractEntitie.setSharingServiceFeeFlag(true);
                                            updateContractEntitie.setSpecialStatusAdjustmentFlag(finalSpecialContractStatuFlag);
                                            updateContractEntities.add(updateContractEntitie);
                                        } else {
                                            if (   //跨主体分摊比例大于配置，继续计提
                                                    contractMonthEntity.getReceivableServiceAmount().divide(contractMonthEntity.getPayableDeviceAmount(), 8, java.math.RoundingMode.HALF_UP).compareTo(new BigDecimal(allocateAcrossRatio)) >= 0) {
                                                //上一期仍为是或是第一期,继续原分摊逻辑
                                                ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, endOfMonth, lastServiceFeeDetailsNewEntity, contractMonthEntity, isLastPeriod, finalSpecialContractStatuFlag);
                                                setServiceFeeFlag(serviceFeeDetailsNewEntity, YesOrNoEnum.getCodeByBool(contractMonthEntity.getSharingServiceFeeFlag()), finalSpecialContractStatuFlag, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0 ? "1" : "0");
                                                detailsNewEntities.add(serviceFeeDetailsNewEntity);
                                                //记录合同分摊状态变更
                                                setContractFlag(updateContractEntitie, serviceFeeDetailsNewEntity.getAfterAccruedAmount().compareTo(BigDecimal.ZERO) == 0, finalSpecialContractStatuFlag, true);
                                                updateContractEntities.add(updateContractEntitie);
                                            } else {
                                                //选择跨主体不分摊逻辑
                                                if (lastServiceFeeDetailsNewEntity != null && lastServiceFeeDetailsNewEntity.getAllocateAcrossPrincipals().equals(YesOrNoEnum.YES.getCode())) {
                                                    ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, endOfMonth, lastServiceFeeDetailsNewEntity, contractMonthEntity, isLastPeriod, finalSpecialContractStatuFlag);
                                                    //重新累计计提数
                                                    BigDecimal accrualAmount = serviceFeeDetailsNewEntity.getAccrualAmountTotalNoTax().multiply(new BigDecimal("-1"));
                                                    resetAccrualAmount(serviceFeeDetailsNewEntity, accrualAmount, lastServiceFeeDetailsNewEntity, thisPeriodServiceFeePlan, isLastPeriod || isHtjs, finalSpecialContractStatuFlag);
                                                    serviceFeeDetailsNewEntity.setAfterAccruedAmount(BigDecimal.ZERO);
                                                    serviceFeeDetailsNewEntity.setBeforeAccruedAmount(serviceFeeDetailsNewEntity.getAccruedAmount().multiply(new BigDecimal("-1")));
                                                    serviceFeeDetailsNewEntity.setShouldApportionmentAmountTaxInclude(BigDecimal.ZERO);
                                                    serviceFeeDetailsNewEntity.setShouldApportionmentAmountNoTax(BigDecimal.ZERO);
                                                    if (lastServiceFeeDetailsNewEntity != null) {
                                                        serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountTaxInclude(serviceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude().subtract(lastServiceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude()));
                                                        serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountNoTax(FinhubAmountUtils.amountNoTax(serviceFeeDetailsNewEntity.getThisMonthReclassificationAdjustmentAmountTaxInclude()));
                                                    }
                                                    setServiceFeeFlag(serviceFeeDetailsNewEntity, YesOrNoEnum.NO.getCode(), finalSpecialContractStatuFlag, YesOrNoEnum.YES.getCode());
                                                    detailsNewEntities.add(serviceFeeDetailsNewEntity);
                                                    //记录合同分摊状态变更
                                                    setContractFlag(updateContractEntitie, true, finalSpecialContractStatuFlag, false);
                                                    updateContractEntities.add(updateContractEntitie);
                                                } else {
                                                    //首期跨主体不分摊
                                                    setContractFlag(updateContractEntitie, true, finalSpecialContractStatuFlag, false);
                                                    updateContractEntities.add(updateContractEntitie);
                                                }
                                            }

                                        }
                                    }
                                }
                            } else {
                                //没有分摊计划是否还有尾差没处理
//                                ServiceFeePlanNewEntity serviceFeePlanNewEntity = serviceFeePlanNewEntities.get(serviceFeePlanNewEntities.size() - 1);
//                                if (lastServerviceFeeDetailsMap.get(serviceFeeNoAndServiceOrgId) != null) {
//                                    ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = buildServiceFeeDetailsNewEntity(queryDTO, serviceFeePlanNewEntities, new DateTime(serviceFeePlanNewEntity.getPlanDate()), lastServerviceFeeDetailsMap.get(serviceFeeNoAndServiceOrgId), contractMonthEntity, true);
//
//                                    serviceFeeDetailsNewEntity.setBusinessDate(endOfMonth);
//                                    if (serviceFeePlanNewEntity.getAccruedAmount().compareTo(BigDecimal.ZERO) != 0) {
//                                        detailsNewEntities.add(serviceFeeDetailsNewEntity);
//                                    }
//                                }
                            }
                        });
                //计算进度
                if (count % 100 == 0) {
                    log.info("计提进度：{}", String.format("%.2f%%", (double) count / total * 100));
                }
                count++;
            }
            //4.计算没有计划且未分摊完结数据
            log.info("2.5、计算没有计划且未分摊完结数据");
            List<String> allocateContractCodes = detailsNewEntities.stream().map(ServiceFeeDetailsNewEntity::getContractCode).distinct().collect(Collectors.toList());
//            calculateUnapportionData(queryDTO, detailsNewEntities, notGenerateOrgIds, endOfMonth);
            List<ServiceFeeDetailsNewEntity> unapportionedAndNoPlanDatas = detailsNewService.getUnapportionedAndNoPlanData(unapportionsDate, allocateContractCodes);
            buildUnapportionData(queryDTO, detailsNewEntities, notGenerateOrgIds, endOfMonth, unapportionedAndNoPlanDatas);
            log.info("2.6、计算完成");
            // 3.落库
            log.info("3、落库");
            List<ServiceFeeNewEntity> serviceFeeNewEntities = new ArrayList<>();
            Map<String, List<ServiceFeeDetailsNewEntity>> collect1 = detailsNewEntities.stream().collect(Collectors.groupingBy(ServiceFeeDetailsNewEntity::getServiceOrgId));
            collect1.forEach((orgId, feeDetailsNewEntities) -> {
                Long id = IdWorker.getId();
                ServiceFeeNewEntity entity = new ServiceFeeNewEntity();
                entity.setId(id);
                entity.setBusinessDate(DateUtil.beginOfDay(endOfMonth));
                entity.setOrgId(orgId);
                entity.setAccruedAmount(feeDetailsNewEntities.stream().map(ServiceFeeDetailsNewEntity::getAccruedAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                entity.setProcessStatus(MarginStatusEnum.ENTERED.getCode());
                serviceFeeNewEntities.add(entity);
                feeDetailsNewEntities.forEach(e -> e.setServiceFeeId(id));
            });
            serviceFeeNewService.saveBatch(serviceFeeNewEntities);
            detailsNewService.saveBatch(detailsNewEntities);
            // 4.更新合同标识
            log.info("4、更新合同标识");
            List<Long> htjsIds = contractMonthEntities.stream().filter(e -> ContractStatusEnum.HTJS.getDesc().equals(e.getContractStatus())).map(ContractMonthEntity::getId).collect(Collectors.toList());
            contractService.update(new UpdateWrapper<ContractEntity>().lambda().set(ContractEntity::getEndSharingServiceFeeFlag, true).in(ContractEntity::getId, htjsIds));
            contractMonthService.update(new UpdateWrapper<ContractMonthEntity>().lambda().set(ContractMonthEntity::getEndSharingServiceFeeFlag, true).in(ContractMonthEntity::getId, htjsIds));
            contractService.updateBatchByContractCode(updateContractEntities);
            List<ContractMonthEntity> updateContractMonthEntities = BeanUtil.copyToList(updateContractEntities, ContractMonthEntity.class);
            contractMonthService.updateBatchByContractCode(updateContractMonthEntities);
            log.info("5、咨询服务费测算结束!");
        } catch (Exception e) {
            log.error("measurementAsync fail!", e);
            dataExecutionTaskService.errorTask(taskId, DataExecutionTaskStatusEnum.FAILED.getCode(), 0, 0, e.getMessage());
            throw new RuntimeException(e);
        }
        dataExecutionTaskService.finishedTask(taskId, DataExecutionTaskStatusEnum.SUCCESS.getCode(), 0, 0);
        return R.ok("咨询服务费测算中，请稍后查看结果!");
    }

    /**
     * 设置合同标识
     * @param updateContractEntitie
     * @param endSharingServiceFeeFlag 结束分摊标识
     * @param finalSpecialContractStatuFlag 特殊合同状态标识
     * @param sharingServiceFeeFlag 是否分摊标识
     */
    private static void setContractFlag(ContractEntity updateContractEntitie, boolean endSharingServiceFeeFlag, boolean finalSpecialContractStatuFlag, boolean sharingServiceFeeFlag) {
        updateContractEntitie.setEndSharingServiceFeeFlag(endSharingServiceFeeFlag);
        updateContractEntitie.setSpecialStatusAdjustmentFlag(finalSpecialContractStatuFlag);
        updateContractEntitie.setSharingServiceFeeFlag(sharingServiceFeeFlag);
    }


    /**
     * 设置服务费标识
     * @param serviceFeeDetailsNewEntity
     * @param sharingServiceFeeFlag  是否分摊标识
     * @param finalSpecialContractStatuFlag 特殊合同状态标识
     * @param endSharingServiceFeeFlag 结束分摊标识
     */
    private static void setServiceFeeFlag(ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity, String sharingServiceFeeFlag, boolean finalSpecialContractStatuFlag, String endSharingServiceFeeFlag) {
        serviceFeeDetailsNewEntity.setSharingServiceFeeFlag(sharingServiceFeeFlag);
        serviceFeeDetailsNewEntity.setSpecialStatusAdjustmentFlag(finalSpecialContractStatuFlag ? "1" : "0");
        serviceFeeDetailsNewEntity.setEndSharingServiceFeeFlag(endSharingServiceFeeFlag);
    }

    private void calculateUnapportionData(ServiceFeeQueryDTO queryDTO, List<ServiceFeeDetailsNewEntity> detailsNewEntities, List<String> notGenerateOrgIds, DateTime endOfMonth) {
        List<String> allocateContractCodes = detailsNewEntities.stream().map(ServiceFeeDetailsNewEntity::getContractCode).distinct().collect(Collectors.toList());
        List<ServiceFeeDetailsNewEntity> unapportionedAndNoPlanDatas = detailsNewService.getUnapportionedAndNoPlanData(queryDTO, allocateContractCodes);
        buildUnapportionData(queryDTO, detailsNewEntities, notGenerateOrgIds, endOfMonth, unapportionedAndNoPlanDatas);
    }

    private void buildUnapportionData(ServiceFeeQueryDTO queryDTO, List<ServiceFeeDetailsNewEntity> detailsNewEntities, List<String> notGenerateOrgIds, DateTime endOfMonth, List<ServiceFeeDetailsNewEntity> unapportionedAndNoPlanDatas) {
        if (CollectionUtils.isNotEmpty(unapportionedAndNoPlanDatas)) {
            List<String> allocatecontractcodes = unapportionedAndNoPlanDatas.stream().map(ServiceFeeDetailsNewEntity::getContractCode).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<ContractMonthEntity> contractMonthEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            contractMonthEntityLambdaQueryWrapper.in(ContractMonthEntity::getContractCategory, Lists.newArrayList("1", "3"));
            contractMonthEntityLambdaQueryWrapper.in(ContractMonthEntity::getContractCode, allocatecontractcodes);
            contractMonthEntityLambdaQueryWrapper.eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            contractMonthEntityLambdaQueryWrapper.notIn(CollectionUtils.isNotEmpty(notGenerateOrgIds), ContractMonthEntity::getOrgId, notGenerateOrgIds);
            List<ContractMonthEntity> monthEntities = contractMonthService.list(contractMonthEntityLambdaQueryWrapper);
            Map<String, ContractMonthEntity> monthEntityMap = monthEntities.stream().collect(Collectors.toMap(ContractMonthEntity::getContractCode, v -> v, (existing, replacement) -> existing));
            for (ServiceFeeDetailsNewEntity unapportionedAndNoPlanData : unapportionedAndNoPlanDatas) {
                ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = new ServiceFeeDetailsNewEntity();
                BeanUtil.copyProperties(unapportionedAndNoPlanData, serviceFeeDetailsNewEntity);
                ContractMonthEntity contractMonthEntity = monthEntityMap.get(unapportionedAndNoPlanData.getContractCode());
                serviceFeeDetailsNewEntity.setId(IdWorker.getId());
                serviceFeeDetailsNewEntity.setBusinessCode(contractMonthEntity.getBusinessCode());
                serviceFeeDetailsNewEntity.setPeriods(serviceFeeDetailsNewEntity.getPeriods() + 1);
                serviceFeeDetailsNewEntity.setFinancialContractStatus(contractMonthEntity.getFinancialContractStatus());
                serviceFeeDetailsNewEntity.setAccrualYear(CommonDateUtils.getYearValue(queryDTO.getBusinessDate()));
                serviceFeeDetailsNewEntity.setAccrualMonth(CommonDateUtils.getMonthValue(queryDTO.getBusinessDate()));
                serviceFeeDetailsNewEntity.setBusinessDate(DateUtil.beginOfDay(endOfMonth));
                serviceFeeDetailsNewEntity.setClientCode(contractMonthEntity.getClientCode());
                serviceFeeDetailsNewEntity.setClientName(contractMonthEntity.getClientName());
                serviceFeeDetailsNewEntity.setContractStatus(contractMonthEntity.getContractStatus());
                serviceFeeDetailsNewEntity.setBusinessCode(contractMonthEntity.getBusinessCode());
                serviceFeeDetailsNewEntity.setBusinessName(contractMonthEntity.getBusinessName());
                serviceFeeDetailsNewEntity.setLeaseDateStart(contractMonthEntity.getLeaseDateStart());
                serviceFeeDetailsNewEntity.setLeaseDateEnd(contractMonthEntity.getLeaseDateEnd());
                serviceFeeDetailsNewEntity.setPlanAmountTaxInclude(BigDecimal.ZERO);
                serviceFeeDetailsNewEntity.setPlanAmountNoTax(BigDecimal.ZERO);
                serviceFeeDetailsNewEntity.setAllocateAcrossPrincipals(queryDTO.getAllocateAcrossPrincipals());
                serviceFeeDetailsNewEntity.setCurrentPeriodPlanAmountTaxInclude(BigDecimal.ZERO);
                serviceFeeDetailsNewEntity.setBeforeCurrentPeriodPlanAmountTaxInclude(serviceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude());
                serviceFeeDetailsNewEntity.setAfterCurrentPeriodPlanAmountTaxInclude(BigDecimal.ZERO);
                BigDecimal beforeAccrualAmount = unapportionedAndNoPlanData.getBeforeAccruedAmount().add(unapportionedAndNoPlanData.getAccruedAmount());
                BigDecimal accrualAmount = unapportionedAndNoPlanData.getShouldApportionmentAmountNoTax().subtract(beforeAccrualAmount);
                serviceFeeDetailsNewEntity.setAccruedAmount(accrualAmount);
                serviceFeeDetailsNewEntity.setBeforeAccruedAmount(beforeAccrualAmount);
                serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(beforeAccrualAmount);
                serviceFeeDetailsNewEntity.setAfterAccruedAmount(BigDecimal.ZERO);
                serviceFeeDetailsNewEntity.setEndSharingServiceFeeFlag(YesOrNoEnum.YES.getCode());
                String exceptionType = serviceFeeDetailsNewEntity.getReceivedServiceFeeTaxInclude().compareTo(serviceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude()) < 0 ? "服务费实收小于应分摊服务费收入" : "";
                serviceFeeDetailsNewEntity.setExceptionType(exceptionType);
                detailsNewEntities.add(serviceFeeDetailsNewEntity);
            }
        }
    }

    private static void resetAccrualAmount(ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity, BigDecimal accrualAmount, ServiceFeeDetailsNewEntity lastServiceFeeDetailsNewEntity,
                                           ServiceFeePlanNewEntity thisPeriodServiceFeePlan, boolean isLastPeriod, boolean specialContractStatuFlag) {
        serviceFeeDetailsNewEntity.setAccruedAmount(accrualAmount);
        if (lastServiceFeeDetailsNewEntity == null) {
            serviceFeeDetailsNewEntity.setBeforeAccruedAmount(BigDecimal.ZERO);
            serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(BigDecimal.ZERO);
        } else {
            serviceFeeDetailsNewEntity.setBeforeAccruedAmount(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax().add(lastServiceFeeDetailsNewEntity.getAccruedAmount()));
            serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax().add(lastServiceFeeDetailsNewEntity.getAccruedAmount()));
        }
        serviceFeeDetailsNewEntity.setAfterAccruedAmount(thisPeriodServiceFeePlan.getShouldApportionmentAmountNoTax().subtract(serviceFeeDetailsNewEntity.getAccrualAmountTotalNoTax()).subtract(serviceFeeDetailsNewEntity.getAccruedAmount()));
        //尾差处理
        if (isLastPeriod) {
            BigDecimal subtract = serviceFeeDetailsNewEntity.getShouldApportionmentAmountNoTax().subtract(serviceFeeDetailsNewEntity.getAccrualAmountTotalNoTax()).subtract(serviceFeeDetailsNewEntity.getAccruedAmount());
            if (subtract.compareTo(BigDecimal.ZERO) != 0) {
                accrualAmount = accrualAmount.add(subtract);
                serviceFeeDetailsNewEntity.setAccruedAmount(accrualAmount);
                serviceFeeDetailsNewEntity.setBeforeAccruedAmount(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax().add(lastServiceFeeDetailsNewEntity.getAccruedAmount()));
                serviceFeeDetailsNewEntity.setAfterAccruedAmount(BigDecimal.ZERO);
                serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax().add(lastServiceFeeDetailsNewEntity.getAccruedAmount()));
            }
        }
        //处理提前计提完的数据
        if (lastServiceFeeDetailsNewEntity != null && lastServiceFeeDetailsNewEntity.getShouldApportionmentAmountNoTax().compareTo(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax()) == 0) {
            accrualAmount = BigDecimal.ZERO;
            serviceFeeDetailsNewEntity.setAccruedAmount(accrualAmount);
            serviceFeeDetailsNewEntity.setBeforeAccruedAmount(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax());
            serviceFeeDetailsNewEntity.setAfterAccruedAmount(BigDecimal.ZERO);
            serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax());
        }
        //特殊状态调整
        if (lastServiceFeeDetailsNewEntity != null && lastServiceFeeDetailsNewEntity.getShouldApportionmentAmountNoTax().compareTo(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax()) == 0) {
            serviceFeeDetailsNewEntity.setAccruedAmount(BigDecimal.ZERO);
            serviceFeeDetailsNewEntity.setBeforeAccruedAmount(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax());
            serviceFeeDetailsNewEntity.setAfterAccruedAmount(BigDecimal.ZERO);
            serviceFeeDetailsNewEntity.setAccrualAmountTotalNoTax(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax());
        }
    }

    /**
     * 组装服务费分摊详情对象
     *
     * @param queryDTO
     * @param serviceFeePlanNewEntities
     * @param endOfMonth
     * @param lastServiceFeeDetailsNewEntity
     * @param contractMonthEntity
     * @param isLastPeriod
     * @return
     */
    private static ServiceFeeDetailsNewEntity buildServiceFeeDetailsNewEntity(ServiceFeeQueryDTO queryDTO, List<ServiceFeePlanNewEntity> serviceFeePlanNewEntities, DateTime endOfMonth,
                                                                              ServiceFeeDetailsNewEntity lastServiceFeeDetailsNewEntity, ContractMonthEntity contractMonthEntity, boolean isLastPeriod, boolean specialContractStatuFlag) {
        BigDecimal planAmountTotalTaxInclude = serviceFeePlanNewEntities.stream().filter(v -> !v.getPlanDate().after(DateUtil.beginOfDay(endOfMonth))).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal planAmountTotalNoTax = FinhubAmountUtils.amountNoTax(planAmountTotalTaxInclude);
        ServiceFeePlanNewEntity thisPeriodServiceFeePlan = serviceFeePlanNewEntities.stream().filter(v -> v.getPlanDate().equals(DateUtil.beginOfDay(endOfMonth))).findFirst().get();
        BigDecimal accrualAmount = planAmountTotalNoTax;
        if (lastServiceFeeDetailsNewEntity != null) {
            accrualAmount = planAmountTotalNoTax.subtract(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax()).subtract(lastServiceFeeDetailsNewEntity.getAccruedAmount());
        }
        if (ContractStatusEnum.HTJS.getDesc().equals(contractMonthEntity.getContractStatus())) {
            accrualAmount = planAmountTotalNoTax.subtract(lastServiceFeeDetailsNewEntity.getAccrualAmountTotalNoTax());
        }
        String exceptionType = "";
        ServiceFeeDetailsNewEntity serviceFeeDetailsNewEntity = new ServiceFeeDetailsNewEntity();
        serviceFeeDetailsNewEntity.setPeriods(thisPeriodServiceFeePlan.getPeriods());
        serviceFeeDetailsNewEntity.setFinancialContractStatus(contractMonthEntity.getFinancialContractStatus());
        serviceFeeDetailsNewEntity.setAccrualYear(CommonDateUtils.getYearValue(queryDTO.getBusinessDate()));
        serviceFeeDetailsNewEntity.setAccrualMonth(CommonDateUtils.getMonthValue(queryDTO.getBusinessDate()));
        serviceFeeDetailsNewEntity.setBusinessDate(DateUtil.beginOfDay(endOfMonth));
        serviceFeeDetailsNewEntity.setServiceFeePlanId(thisPeriodServiceFeePlan.getId());
        serviceFeeDetailsNewEntity.setContractCode(thisPeriodServiceFeePlan.getContractCode());
        serviceFeeDetailsNewEntity.setOrgId(thisPeriodServiceFeePlan.getOrgId());
        serviceFeeDetailsNewEntity.setServiceOrgId(thisPeriodServiceFeePlan.getServiceOrgId());
        serviceFeeDetailsNewEntity.setServiceFeeNo(thisPeriodServiceFeePlan.getServiceFeeNo());
        serviceFeeDetailsNewEntity.setClientCode(contractMonthEntity.getClientCode());
        serviceFeeDetailsNewEntity.setClientName(contractMonthEntity.getClientName());
        serviceFeeDetailsNewEntity.setContractStatus(contractMonthEntity.getContractStatus());
        serviceFeeDetailsNewEntity.setBusinessCode(contractMonthEntity.getBusinessCode());
        serviceFeeDetailsNewEntity.setBusinessName(contractMonthEntity.getBusinessName());
        serviceFeeDetailsNewEntity.setLeaseDateStart(contractMonthEntity.getLeaseDateStart());
        serviceFeeDetailsNewEntity.setLeaseDateEnd(contractMonthEntity.getLeaseDateEnd());
        serviceFeeDetailsNewEntity.setAllocationMethod(ObjectUtil.equals(serviceFeeDetailsNewEntity.getOrgId(), serviceFeeDetailsNewEntity.getServiceOrgId()) ? "0" : "1");
        serviceFeeDetailsNewEntity.setReceivedServiceFeeTaxInclude(thisPeriodServiceFeePlan.getReceivedServiceFeeTaxInclude());
        serviceFeeDetailsNewEntity.setReceivedServiceFeeNoTax(thisPeriodServiceFeePlan.getReceivedServiceFeeNoTax());
        serviceFeeDetailsNewEntity.setShouldApportionmentAmountTaxInclude(thisPeriodServiceFeePlan.getShouldApportionmentAmountTaxInclude());
        serviceFeeDetailsNewEntity.setShouldApportionmentAmountNoTax(thisPeriodServiceFeePlan.getShouldApportionmentAmountNoTax());
        serviceFeeDetailsNewEntity.setPlanAmountTaxInclude(thisPeriodServiceFeePlan.getPlanAmountTaxInclude());
        serviceFeeDetailsNewEntity.setPlanAmountNoTax(thisPeriodServiceFeePlan.getPlanAmountNoTax());
        serviceFeeDetailsNewEntity.setAllocateAcrossPrincipals(queryDTO.getAllocateAcrossPrincipals());

        if (lastServiceFeeDetailsNewEntity != null) {
            serviceFeeDetailsNewEntity.setLastMonthShouldApportionmentAmountTaxInclude(lastServiceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude());
            serviceFeeDetailsNewEntity.setLastMonthShouldApportionmentAmountNoTax(lastServiceFeeDetailsNewEntity.getShouldApportionmentAmountNoTax());
            serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountTaxInclude(serviceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude().subtract(lastServiceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude()));
            serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountNoTax(FinhubAmountUtils.amountNoTax(serviceFeeDetailsNewEntity.getThisMonthReclassificationAdjustmentAmountTaxInclude()));
            exceptionType += lastServiceFeeDetailsNewEntity.getReceivedServiceFeeTaxInclude().compareTo(serviceFeeDetailsNewEntity.getReceivedServiceFeeTaxInclude()) != 0 ? "服务费实收变化，" : "";
        } else {
            serviceFeeDetailsNewEntity.setLastMonthShouldApportionmentAmountTaxInclude(BigDecimal.ZERO);
            serviceFeeDetailsNewEntity.setLastMonthShouldApportionmentAmountNoTax(BigDecimal.ZERO);
            serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountTaxInclude(serviceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude());
            serviceFeeDetailsNewEntity.setThisMonthReclassificationAdjustmentAmountNoTax(serviceFeeDetailsNewEntity.getShouldApportionmentAmountNoTax());
        }
        serviceFeeDetailsNewEntity.setCurrentPeriodPlanAmountTaxInclude(planAmountTotalTaxInclude);
        serviceFeeDetailsNewEntity.setBeforeCurrentPeriodPlanAmountTaxInclude(thisPeriodServiceFeePlan.getPlanAmountTaxInclude());
        serviceFeeDetailsNewEntity.setAfterCurrentPeriodPlanAmountTaxInclude(thisPeriodServiceFeePlan.getShouldApportionmentAmountTaxInclude().subtract(thisPeriodServiceFeePlan.getPlanAmountTaxInclude()).subtract(planAmountTotalTaxInclude));
        resetAccrualAmount(serviceFeeDetailsNewEntity, accrualAmount, lastServiceFeeDetailsNewEntity, thisPeriodServiceFeePlan, isLastPeriod || ContractStatusEnum.HTJS.getDesc().equals(contractMonthEntity.getContractStatus()), specialContractStatuFlag);
        exceptionType += serviceFeeDetailsNewEntity.getReceivedServiceFeeTaxInclude().compareTo(serviceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude()) < 0 ? "服务费实收小于应分摊服务费收入，" : "";
        serviceFeeDetailsNewEntity.setExceptionType(exceptionType);
        return serviceFeeDetailsNewEntity;
    }

    private List<String> deleteNotSubmitAndFilterSubmitedOrg(Date queryDate) {
        List<String> notGenerateOrgIds = new ArrayList<>();
        List<ServiceFeeNewEntity> serviceFeeEntities = serviceFeeNewService.getBaseMapper().selectList(
                Wrappers.<ServiceFeeNewEntity>lambdaQuery().eq(ServiceFeeNewEntity::getBusinessDate, DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate))));
        if (CollectionUtils.isNotEmpty(serviceFeeEntities)) {
            // 删除未提交数据
            List<Long> deleteIds = serviceFeeEntities.stream().filter(e -> ProcessStatusEnum.getInvalidCode().
                    contains(e.getProcessStatus())).map(ServiceFeeNewEntity::getId).distinct().collect(Collectors.toList());
            List<Long> logicDeleteIds = serviceFeeEntities.stream().filter(e -> ProcessStatusEnum.WRITEOFF.getCode().equals(e.getProcessStatus())
            ).map(ServiceFeeNewEntity::getId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteIds)) {
                // 删除凭证
                List<String> voucherIdList = detailsNewService.getBaseMapper().selectList(
                                Wrappers.<ServiceFeeDetailsNewEntity>lambdaQuery().in(ServiceFeeDetailsNewEntity::getServiceFeeId, deleteIds))
                        .stream().map(ServiceFeeDetailsNewEntity::getVoucherId).filter(Objects::nonNull).collect(Collectors.toList());
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

                LambdaQueryWrapper<ServiceFeeDetailsNewEntity> detailUpdateChainWrapper = new LambdaQueryWrapper<>();
                detailUpdateChainWrapper.in(ServiceFeeDetailsNewEntity::getServiceFeeId, deleteIds);
                detailsNewService.remove(detailUpdateChainWrapper);
                this.removeByIds(deleteIds);
            }
            //已红冲的逻辑删除
            if (CollectionUtils.isNotEmpty(logicDeleteIds)) {
                detailsNewService.lambdaUpdate().set(ServiceFeeDetailsNewEntity::getDelFlag, "1").in(ServiceFeeDetailsNewEntity::getServiceFeeId, logicDeleteIds).update();
                this.lambdaUpdate().set(ServiceFeeNewEntity::getDelFlag, "1").in(ServiceFeeNewEntity::getId, logicDeleteIds).update();
            }
            // 获取不生成数据的公司
            notGenerateOrgIds = serviceFeeEntities.stream().filter(e -> ProcessStatusEnum.getCannotModifyCode().
                    contains(e.getProcessStatus())).map(e -> e.getOrgId()).distinct().collect(Collectors.toList());
        }
        return notGenerateOrgIds;
    }
}
