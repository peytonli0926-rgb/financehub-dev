package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.AccrualMethodEnum;
import com.utfinancing.financehub.engine.enums.RecaptureStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanXWMapper;
import com.utfinancing.financehub.engine.finance.mapper.VoucherAmountInitMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.utils.IRRUtils;
import com.utfinancing.financehub.engine.utils.UserUtils;
import com.utfinancing.financehub.engine.utils.XirrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service(value = "xwAsync")
@Slf4j
public class RepaymentSeriviceForXWAsyncImpl extends ServiceImpl<RepaymentPlanMapper, RepaymentPlanEntity>
        implements IRepaymentAsyncSerivice {

    @Resource
    private RepaymentPlanXWMapper repaymentPlanXWMapper;

    @Resource
    private RepaymentPlanMapper repaymentPlanMapper;

    @Resource
    private VoucherAmountInitMapper voucherAmountInitMapper;

    @Resource
    private IRepaymentPlanService repaymentPlanService;

    @Resource
    private RedisService redisService;

    @Resource
    private IServiceNoAmortizationService serviceNoAmortizationService;

    @Resource
    private IRepaymentPlanExceldataService repaymentPlanExceldataService;

    @Resource
    private IContractTaAmountService contractTaAmountService;

    @Resource
    private IVoucherAmountInitService voucherAmountInitService;

    @Resource
    public IOrgCompanyService orgCompanyService;

    @Resource
    private IContractService contractService;

    @Resource
    private IOutstandingAmountInitService outstandingAmountInitService;

    @Override
    @Async
    public void generatePaymentDataProcess(GeneratePaymentDataProcessDTO params) {
        log.info(Thread.currentThread().getName() + "start:" + params.getDataStartIndex() + "; count:" + params.getDataCount());
        // 根据设定的每次循环处理的数据量，计算循环次数
        int eachLoopProcessCount = Constants.GENERATE_PAYMENT_PLAN_PROCESS_NUMBER;
        int loopCount = params.getDataCount() / eachLoopProcessCount;
        if (params.getDataCount() % eachLoopProcessCount != 0) {
            loopCount++;
        }

        // 处理次数循环
        for (int i = 0; i < loopCount; i++) {
            int startIndex = i * eachLoopProcessCount + params.getDataStartIndex();
            Map<String, List<RepaymentPlanXWEntity>> dataMap = null;
            if (i == loopCount - 1) {
                int limit = params.getDataCount() - i * eachLoopProcessCount;
                log.info(Thread.currentThread().getName() + "start:" + startIndex + "; count:" + limit + "; i:"+ i);
                dataMap = this.selectHyInitDataByPage(startIndex, limit);
            } else {
                log.info(Thread.currentThread().getName() + "start:" + startIndex + "; count:" + "100"+ "; i:"+ i);
                dataMap = this.selectHyInitDataByPage(startIndex, Constants.GENERATE_PAYMENT_PLAN_PROCESS_NUMBER);
            }

            // 取得回笼数据
            List<ContractDTO> contractDTOList = contractService.listContractDTOByCodeList(
                    dataMap.keySet().stream().collect(Collectors.toList()));
            Map<String, ContractDTO> contractDTOMap = contractDTOList.stream().collect(
                    Collectors.toMap(e->e.getContractCode(), (e)->e, (a,b)->b));
            Map<String, BigDecimal> returnMoneyMap = repaymentPlanExceldataService.
                    getReceivedMoneyByContract(dataMap.keySet(), contractDTOMap);

            // 根据合同查询收益数据列表
//            Map<String, List<VoucherAmountInitEntity>> voucherAmountInitEntityMap =
//                    voucherAmountInitService.getVoucherAmountByContract(dataMap.keySet());
            // 咨询服务费
            Map<String, BigDecimal> serviceNoAmortizationMap = serviceNoAmortizationService.
                    getServiceNoAmortizationAmount(dataMap.keySet());
            // 未实现收益
            Map<String, BigDecimal> outstandingAmountMap = outstandingAmountInitService.selectEndBalFor(new ArrayList<>(dataMap.keySet()));

            Iterator<Map.Entry<String, List<RepaymentPlanXWEntity>>> it = dataMap.entrySet().iterator();
            // 循环取出来的合同
            while (it.hasNext()){
                Map.Entry<String, List<RepaymentPlanXWEntity>> entry = it.next();
//                log.info(String.format("生成偿还计划，合同号：%s", entry.getKey()));
                List<RepaymentPlanXWEntity> repaymentPlanXWEntityList = entry.getValue().stream().
                        sorted(Comparator.comparing(RepaymentPlanXWEntity::getPlanDate)).collect(Collectors.toList());;
                // 取出临时生成的现金流数据-不参与irr计算
                RepaymentPlanXWEntity tempCashFlowData = this.getGenerateCashflowObj(repaymentPlanXWEntityList);

                // 部分未生成现金流的数据，即期初之后起租的合同也需要生成偿还计划
                boolean isLeaseStart = false;
                Date initDate = DateUtil.endOfDay(DateUtil.endOfMonth(DateUtil.parse(params.getInitDate())));
                if (repaymentPlanXWEntityList.get(0).getPlanDate().compareTo(initDate) >= 0) {
                    isLeaseStart = true;
                }
                if (tempCashFlowData == null && !isLeaseStart) {
                    log.info(String.format("合同编号%s未实现收益不存在或者为0,不再参与收益计提!", entry.getKey()));
                    continue;
                }

                // 更新回笼数据
                updateRepaymentPlan(repaymentPlanXWEntityList, returnMoneyMap);
                ContractDTO contractDTO = contractDTOMap.get(entry.getKey());
                if (contractDTO == null) {
                    log.error(String.format("合同编号%s不存在合同信息!", entry.getKey()));
                    continue;
                }

                // 待摊销的金额
                BigDecimal toBeAssessedAmount = serviceNoAmortizationMap.get(entry.getKey());
                if (toBeAssessedAmount == null) {
                    toBeAssessedAmount = BigDecimal.ZERO;
                }

                // 计算irr并生成月底分摊数据
                repaymentPlanXWEntityList = this.businessProcess(repaymentPlanXWEntityList, contractDTO, outstandingAmountMap,false, null);
                // 临时存储第一次计算的分摊情况- 重新计算后，需要覆盖第二次计算后的部分金额
                Map<String, RepaymentPlanXWEntity> repaymentPlanXWEntityMap = repaymentPlanXWEntityList.stream().
                        collect(Collectors.toMap(e->this.getKey(e), (e)->e, (a,b)->b));

                // 判断irr和源数据irr相差是否超过0.01, 未超过不处理， 超过的情况重新将2023-11-30之后的数据重新计算
//                if (xirrRateCompare(repaymentPlanXWEntityList)) {
//                    repaymentPlanXWEntityList = this.xirrRecalculation(contractDTO, repaymentPlanXWEntityList, tempCashFlowData,
//                            voucherAmountInitEntityMap);
//                }

                if (tempCashFlowData != null) {
                    repaymentPlanXWEntityList = this.irrRecalculation(
                            contractDTO, toBeAssessedAmount, repaymentPlanXWEntityList, tempCashFlowData, outstandingAmountMap);
                }
                // 服务费率计算， 并用第一次计算结果覆盖部分字段到第二次计算结果上
                this.serviceFeeRateCompute(repaymentPlanXWEntityList);
                List<RepaymentPlanEntity> result = BeanUtil.copyToList(repaymentPlanXWEntityList, RepaymentPlanEntity.class);

                // 如果是实收则需要重新计算差异
                if (AccrualMethodEnum.RECEIPT.getCode().equals(contractDTO.getIncomeProvisionMethod())) {
                    LeaseIncomeImport incomeImport = new LeaseIncomeImport();
                    try {
                        incomeImport.setBusinessDate(DateUtils.parseDate(params.getInitDate().concat("01"), "yyyyMMdd"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    result = repaymentPlanService.irrTransferToReceipt(incomeImport, result);
                }
                this.saveBatch(result);
            }
        }
        log.info(Thread.currentThread().getName() + "生成偿还计划线程终止!");
    }

    /**
     * 取得RepaymentPlanXWEntity的业务主键
     */
    private String getKey(RepaymentPlanXWEntity entity) {
        StringBuffer result = new StringBuffer(entity.getContractCode());
        String planDateStr = DateUtil.format(entity.getPlanDate(), "yyyyMMdd");
        result.append("|").append(planDateStr);
        return result.toString();
    }

    /**
     * 取得合同的回笼数据
     */
    private Map<String, List<RepaymentPlanExceldataEntity>> getReturnMoneyByContractCode(Set<String> contractCodeList) {
        LambdaQueryWrapper<RepaymentPlanExceldataEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(RepaymentPlanExceldataEntity::getContractCode, contractCodeList);
        queryWrapper.orderByAsc(RepaymentPlanExceldataEntity::getContractCode);
        queryWrapper.orderByAsc(RepaymentPlanExceldataEntity::getPlanDate);
        List<RepaymentPlanExceldataEntity> result = repaymentPlanExceldataService.getBaseMapper().selectList(queryWrapper);

        // 保存ta金额
        if (result != null && !result.isEmpty()) {
//            contractTaAmountService.saveTaAmountByExcelData(result);
            return result.stream().collect(Collectors.groupingBy(e -> e.getContractCode()));
        } else {
            return new HashMap<>();
        }
    }

    /**
     * 服务费率计算
     */
    private void serviceFeeRateCompute(List<RepaymentPlanXWEntity> repaymentPlanXWEntityList) {
        BigDecimal totalUnrealizedRevenue = repaymentPlanXWEntityList.stream().map(e -> e.getRentalIncome()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        for (int i = 0; i < repaymentPlanXWEntityList.size(); i++) {
            RepaymentPlanXWEntity saveDTO = repaymentPlanXWEntityList.get(i);
            if (saveDTO.getRentalIncome() != null && saveDTO.getRentalIncome().doubleValue() != 0
                    && totalUnrealizedRevenue.doubleValue() != 0) {
                saveDTO.setServiceFeeAmortizationRate(saveDTO.getRentalIncome().
                        divide(totalUnrealizedRevenue, 6, RoundingMode.HALF_UP));
            } else {
                saveDTO.setServiceFeeAmortizationRate(BigDecimal.ZERO);
            }

            // 重新计算未实现收益总额/本月以前/本月之后
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            if (i == 0) {
                saveDTO.setRentalIncomeBeforeTotal(BigDecimal.ZERO);
                saveDTO.setRentalIncomeAfterTotal(totalUnrealizedRevenue.subtract(saveDTO.getRentalIncome()));
            } else {
                BigDecimal rentalIncomeBeforeTotal = NumberUtil.add(
                        repaymentPlanXWEntityList.get(i - 1).getRentalIncomeBeforeTotal(),
                        repaymentPlanXWEntityList.get(i - 1).getRentalIncome());
                saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
                BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue.subtract(
                        saveDTO.getRentalIncomeBeforeTotal()).subtract(saveDTO.getRentalIncome());
                saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            }
        }
    }

    /**
     * 更新回笼的金额
     */
    private void updateRepaymentPlan(List<RepaymentPlanXWEntity> repaymentPlanXWEntityList,
                                     Map<String, BigDecimal> returnMoneyMap) {
        BigDecimal receivedMoneySum = returnMoneyMap.get(repaymentPlanXWEntityList.get(0).getContractCode());
        if (receivedMoneySum == null) {
            receivedMoneySum = BigDecimal.ZERO;
        }

        BigDecimal remainReceivedMoney = receivedMoneySum.add(new BigDecimal(0.1));
        for (RepaymentPlanXWEntity entity : repaymentPlanXWEntityList) {
            if (entity.getRentAmount() == null) {
                entity.setRentAmount(BigDecimal.ZERO);
            }

            if (entity.getPeriods() != null) {
                if (remainReceivedMoney.compareTo(entity.getRentAmount()) >= 0) {
                    // 还款日期
                    entity.setActualRepaymentDate(DateUtils.getNowDate());
                    // 归还本金
                    entity.setActualRepaymentPrincipalAmount(entity.getPrincipalAmount());
                    // 归还利息
                    entity.setActualRepaymentInteresAmount(entity.getInterestAmount());
                    // 归还租金
                    entity.setActualRepaymentRentAmount(entity.getRentAmount());
                    // 回笼状态
                    entity.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
                    remainReceivedMoney = remainReceivedMoney.subtract(entity.getRentAmount());
                } else {
                    // 还款日期
                    entity.setActualRepaymentDate(DateUtils.getNowDate());
                    // 归还本金
                    entity.setActualRepaymentPrincipalAmount(remainReceivedMoney);
                    // 归还利息
                    entity.setActualRepaymentInteresAmount(BigDecimal.ZERO);
                    // 归还租金
                    entity.setActualRepaymentRentAmount(remainReceivedMoney);
                    // 回笼状态
                    if (remainReceivedMoney.compareTo(BigDecimal.ZERO) > 0) {
                        entity.setRecaptureStatus(RecaptureStatusEnum.PARTIALRECOVERY.getCode());
                    } else {
                        entity.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
                    }
                    break;
                }
            }
        }
    }

//    private void setActualRepanymentAmount (RepaymentPlanXWEntity repaymentPlanXWEntity,
//                                            RepaymentPlanExceldataDTO entity) {
//        // 还款日期
//        repaymentPlanXWEntity.setActualRepaymentDate(entity.getPlanDate());
//        // 归还本金
//        repaymentPlanXWEntity.setActualRepaymentPrincipalAmount(entity.getActualRepaymentPrincipalAmount());
//        // 归还利息
//        repaymentPlanXWEntity.setActualRepaymentInteresAmount(entity.getActualRepaymentInteresAmount());
//        // 归还租金
//        repaymentPlanXWEntity.setActualRepaymentRentAmount(entity.getActualRepaymentRentAmount());
//        // TA
//        repaymentPlanXWEntity.setTaReclassification(entity.getTa());
//        // 回笼状态
//        if (repaymentPlanXWEntity.getRentAmount() != null) {
//            if (entity.getActualRepaymentRentAmount() != null
//                    && entity.getActualRepaymentRentAmount().compareTo(repaymentPlanXWEntity.getRentAmount()) >= 0) {
//                repaymentPlanXWEntity.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
//            } else if (entity.getActualRepaymentRentAmount() != null && entity.getActualRepaymentRentAmount().compareTo(BigDecimal.ZERO) > 0) {
//                repaymentPlanXWEntity.setRecaptureStatus(RecaptureStatusEnum.PARTIALRECOVERY.getCode());
//            } else {
//                repaymentPlanXWEntity.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
//            }
//        }
//    }

//    private List<RepaymentPlanXWEntity> xirrRecalculation(ContractDTO contractDTO,
//            List<RepaymentPlanXWEntity> repaymentPlanXWEntityList, RepaymentPlanXWEntity tempCashFlowData,
//            Map<String, List<VoucherAmountInitEntity>> voucherAmountInitEntityMap) {
//        if (tempCashFlowData == null) {
//            return repaymentPlanXWEntityList;
//        }
//
//        List<RepaymentPlanXWEntity> result = new ArrayList<>();
//        List<RepaymentPlanXWEntity> recalculationList = new ArrayList<>();
//        recalculationList.add(tempCashFlowData);
//
//        // 收益数据
//        Map<String, BigDecimal> voucherAmountInitList = getVoucherAmountInitEntity(
//                voucherAmountInitEntityMap, repaymentPlanXWEntityList);
//
//        for (int i = 0; i < repaymentPlanXWEntityList.size(); i++) {
//            RepaymentPlanXWEntity e = repaymentPlanXWEntityList.get(i);
//            // 偿还日期是否小于2023-11-30
//            int compareValue = DateUtils.truncatedCompareTo(e.getPlanDate(),
//                    tempCashFlowData.getPlanDate(), Calendar.DATE);
//            // 判断是否为月底的日期
//            boolean isLastDayOfMonth = DateUtil.isLastDayOfMonth(e.getPlanDate());
//
//            if (compareValue <= 0) {
//                e.setRentalIncome(new BigDecimal(0));
//
//                if (isLastDayOfMonth) {
//
//                    // 将2023年11月30日以前的rental income更新为0，月底的rentail income更新为金蝶过来的数据
//                    String planDateYM = DateUtils.parseDateToStr("yyyyMM", e.getPlanDate());
//                    BigDecimal dtAmount = voucherAmountInitList.get(planDateYM);
//
//                    if (dtAmount == null) {
//                        e.setRentalIncome(new BigDecimal(0));
//                        e.setExceptionType("未从金蝶系统中取得该合同的未偿还数据!");
//                    } else {
//                        e.setRentalIncome(dtAmount);
//                    }
//                }
//                result.add(e);
//            } else {
//                if (e.getPeriods() != null) {
//                    recalculationList.add(e);
//                }
//            }
//        }
//
//        // 再计算
//        result.addAll(this.businessProcess(recalculationList, contractDTO));
//
//        // 删除临时生成的现金流数据
//        result = result.stream().filter(e -> e.getPeriods() == null || e.getPeriods().intValue() != -1).
//                collect(Collectors.toList());
//        return result;
//    }


    private List<RepaymentPlanXWEntity> irrRecalculation(ContractDTO contractDTO, BigDecimal toBeAssessedAmount,
                                                         List<RepaymentPlanXWEntity> repaymentPlanXWEntityList,
                                                         RepaymentPlanXWEntity tempCashFlowData,
                                                         Map<String, BigDecimal> outstandingAmountMap) {
        if (tempCashFlowData == null) {
            return repaymentPlanXWEntityList;
        }

        // 租赁收入求和
        BigDecimal cashflowSum = repaymentPlanXWEntityList.stream().map(e->e.getCashFlow()).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RepaymentPlanXWEntity> result = new ArrayList<>();
        List<RepaymentPlanXWEntity> recalculationList = new ArrayList<>();
        List<RepaymentPlanXWEntity> periodsNullList = new ArrayList<>();
        recalculationList.add(BeanUtil.copyProperties(tempCashFlowData, RepaymentPlanXWEntity.class));

        BigDecimal rentalIncomeSum = BigDecimal.ZERO;
        BigDecimal recalculationRentalIncomeSum = BigDecimal.ZERO;
        boolean isSetTempCashFlow = false;
        for (int i = 0; i < repaymentPlanXWEntityList.size(); i++) {
            RepaymentPlanXWEntity e = repaymentPlanXWEntityList.get(i);
            // 偿还日期是否小于2023-11-30
            int compareValue = DateUtils.truncatedCompareTo(e.getPlanDate(),
                    tempCashFlowData.getPlanDate(), Calendar.DATE);

            if (compareValue <= 0) {
                // 租赁收入求和
                rentalIncomeSum = rentalIncomeSum.add(e.getRentalIncome());
                result.add(e);
            } else {
                if (e.getPeriods() != null) {
                    if (recalculationList.size() == 1 && !isSetTempCashFlow) {
                        result.add(e);
                        recalculationList.get(0).setCashFlow(recalculationList.get(0).getCashFlow().
                                subtract(e.getRentalIncome()).subtract(recalculationRentalIncomeSum).add(e.getCashFlow()));
                        recalculationList.get(0).setPlanDate(e.getPlanDate());
                        isSetTempCashFlow = true;
                    } else {
                        recalculationList.add(e);
                    }
                } else {
                    recalculationRentalIncomeSum = recalculationRentalIncomeSum.add(e.getRentalIncome());
                    periodsNullList.add(e);
                }
            }
        }

        // 再计算
        if (recalculationList.size() > 1) {
            // 再计算的未实现收益调整
            BigDecimal outstandingAmount = outstandingAmountMap.get(contractDTO.getContractCode());
            outstandingAmountMap.put("non_confirm_lease", outstandingAmount.multiply(new BigDecimal(-1))
                    .subtract(result.get(result.size() - 1).getRentalIncome()));
            result.addAll(this.businessProcess(recalculationList, contractDTO, outstandingAmountMap, true, periodsNullList));
        } else if (recalculationList.size() == 1) {
            Date lastPlanDate = repaymentPlanXWEntityList.get(repaymentPlanXWEntityList.size() - 1).getPlanDate();
            int compareValue = DateUtils.truncatedCompareTo(lastPlanDate, tempCashFlowData.getPlanDate(), Calendar.MONTH);

            // 最后一期日期年月等于期初的年月
            if (compareValue == 0) {
                result.stream().forEach(e -> {
                    e.setExceptionType("期初日期即为计划结束日期");
                });
            }

            // 最后一期日期年月大于期初的年月
            if (compareValue > 0) {
                result.get(result.size() - 1).setRentalIncome(toBeAssessedAmount == null ? BigDecimal.ZERO : toBeAssessedAmount);
            }
        }
        Date date = tempCashFlowData.getPlanDate();
        RepaymentPlanXWEntity lastEntity = result.get(result.size() - 1);
        // 判断最后一条数据的 planDate 是否大于给定的 date
        if (ObjectUtil.isNotNull(lastEntity.getPlanDate()) && lastEntity.getPlanDate().after(date)) {
            // 计算金蝶的下的未实现收益与偿还计划本身的未实现收益差异； 如果是偿还计划最后一条，则不再计算差异(没有意义)
            BigDecimal diffAmount = cashflowSum.subtract(toBeAssessedAmount).subtract(rentalIncomeSum);
            result.get(1).setRentalIncome(result.get(1).getRentalIncome().add(diffAmount));
            // 后续咨询服务费会重新计算之前 之后的金额(此处之后的金额不对)
            result.get(1).setRentalIncomeAfterTotal(result.get(1).getRentalIncomeAfterTotal().subtract(diffAmount));
        }

        // 删除临时生成的现金流数据
        result = result.stream().filter(e -> e.getPeriods() == null || e.getPeriods().intValue() != -1).
                collect(Collectors.toList());
        return result;
    }


    private Map<String, BigDecimal> getVoucherAmountInitEntity(
            Map<String, List<VoucherAmountInitEntity>> VoucherAmountInitEntityMap,
            List<RepaymentPlanXWEntity> repaymentPlanXWEntityList) {

        String contractCode = repaymentPlanXWEntityList.get(0).getContractCode();
        List<VoucherAmountInitEntity> voucherAmountInitList = VoucherAmountInitEntityMap.get(contractCode);
        // 如果一条数据都没有则重新从中间库取得收益数据
        if (voucherAmountInitList == null || voucherAmountInitList.isEmpty()) {
            String startPeriod = DateUtils.parseDateToStr("yyyyMM", repaymentPlanXWEntityList.get(0).getPlanDate());
            String endPeriod = DateUtils.parseDateToStr("yyyyMM", repaymentPlanXWEntityList.get(
                    repaymentPlanXWEntityList.size() - 1).getPlanDate());
            return voucherAmountInitService.getVoucherAmountInit(startPeriod, endPeriod, contractCode);
        } else {
            return voucherAmountInitList.stream().collect(Collectors.toMap(
                    e -> e.getPeriod(), VoucherAmountInitEntity::getDtAmount, (a, b) -> a.add(b)));
        }
    }

    /**
     * 算出来的xirrate与源利率比较
     */
    private boolean xirrRateCompare(List<RepaymentPlanXWEntity> repaymentPlanXWEntityList) {
        RepaymentPlanXWEntity repaymentPlanXWEntity = repaymentPlanXWEntityList.get(0);

        if (repaymentPlanXWEntity.getXirrRate() == null || repaymentPlanXWEntity.getXirrRate().doubleValue() == 0
            || repaymentPlanXWEntity.getSourceIrrRate() == null
                || repaymentPlanXWEntity.getSourceIrrRate().doubleValue() == 0) {
            return false;
        }

        Double xirrRateCompareValue = repaymentPlanXWEntity.getXirrRate().subtract(
                repaymentPlanXWEntity.getSourceIrrRate()).doubleValue();
        if (Math.abs(xirrRateCompareValue) > 0.01) {
            return true;
        }
        return false;
    }

    /**
     * 取得待处理数据
     */
    private Map<String, List<RepaymentPlanXWEntity>> selectHyInitDataByPage(int startIndex, int limit) {
        GetProcessDataDTO params = new GetProcessDataDTO();
        params.setPageNum(startIndex);
        params.setPageSize(limit);
        List<RepaymentPlanXWEntity> dbData = repaymentPlanXWMapper.selectHyInitDataByPage(params);
        Map<String, List<RepaymentPlanXWEntity>> dataMap = dbData.stream().collect(
                Collectors.groupingBy(e -> e.getContractCode()));
        return dataMap;
    }


    /**
     * 是否连续分月判断
     */
    private boolean isContinuousMonth(List<RepaymentPlanXWEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            RepaymentPlanXWEntity entity = dataList.get(i);
            if (i == dataList.size() - 1) {
                return true;
            } else {
                if (!DateUtil.isSameMonth(DateUtil.offsetMonth(entity.getPlanDate(), 1), dataList.get(i+1).getPlanDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 生成偿还计划数据
     */
    private List<RepaymentPlanXWEntity> businessProcess(List<RepaymentPlanXWEntity> dataList, ContractDTO contractDTO,
                                                        Map<String, BigDecimal> outstandingAmountMap, boolean isRecompute,
                                                        List<RepaymentPlanXWEntity> periodsNullList) {
        // 偿还计划数据
        List<RepaymentPlanXWEntity> repayments = new ArrayList<>();

        // 1.补齐合同从起租到结清的每月数据
        Date startPlanDate = dataList.get(0).getPlanDate();
        // 判断原数据是否为每月都有数据，如否，则需要补齐每月的数据对象，如是，则不用补齐
        if (this.isContinuousMonth(dataList)) {
            repayments.addAll(dataList);
        } else {
            Iterator<RepaymentPlanXWEntity> leaseIterator = dataList.iterator();
            RepaymentPlanXWEntity next = leaseIterator.next();
            splitPlanDate(startPlanDate, next, repayments, 1, leaseIterator);
        }

        // 2.通过IRR计算利率
        Double incomeRateDouble = 0.00;
        if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())){
            List<Double> cashFlowList = dataList.stream().map(e->e.getCashFlow().doubleValue()).collect(Collectors.toList());
            List<Date> allPlanDate = dataList.stream().map(e->e.getPlanDate()).collect(Collectors.toList());
            incomeRateDouble = XirrUtils.xirr(cashFlowList, allPlanDate);
        } else {
            incomeRateDouble = IRRUtils.calculateIRRYear(
                    repayments.stream().filter(e->e.getCashFlow() != null).
                            map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList()));
        }
        if (Double.isNaN(incomeRateDouble) || incomeRateDouble.doubleValue() == 0) {
            if (CollUtil.isNotEmpty(periodsNullList)) {
                repayments.addAll(periodsNullList);
                // 通过时间排序
                repayments.sort(Comparator.comparing(RepaymentPlanXWEntity::getPlanDate));
            }
            for (int i = 0; i < repayments.size(); i++) {
                RepaymentPlanXWEntity e = repayments.get(i);
                if (StringUtils.isEmpty(e.getSystemCode())) {
                    e.setSystemCode(repayments.get(0).getSystemCode());
                }
                e.setExceptionType("irr计算错误");
                if (i == 1) {
                    if (isRecompute) {
                        e.setRentalIncome(outstandingAmountMap.get("non_confirm_lease"));
                    } else {
                        BigDecimal outstandingAmount = outstandingAmountMap.get(e.getContractCode());
                        e.setRentalIncome(outstandingAmount == null ? BigDecimal.ZERO : outstandingAmount.multiply(new BigDecimal(-1)));
                    }
                } else {
                    e.setRentalIncome(BigDecimal.ZERO);
                }
            }
            return repayments;
        }

        List<RepaymentPlanSaveDTO> newRepayments = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())){
            newRepayments = BeanUtil.copyToList(repayments, RepaymentPlanSaveDTO.class);
            newRepayments = repaymentPlanService.generateXirrRepayments(newRepayments, incomeRateDouble);
        } else {
            newRepayments = this.irrApportion(repayments, incomeRateDouble);
            if (newRepayments == null || newRepayments.isEmpty()) {
                return repayments;
            }
        }

        // 5 数据处理
        BigDecimal totalUnrealizedRevenue = newRepayments.stream().map(e -> e.getRentalIncome()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal rentalIncomeBeforeTotal = BigDecimal.ZERO;
        BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue;

        for (int i = 0; i < newRepayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO = newRepayments.get(i);
            saveDTO.setXirrRate(NumberUtil.mul(incomeRateDouble, new BigDecimal(100)).
                    setScale(5, RoundingMode.HALF_UP));
            saveDTO.setUnrealizedRevenue(totalUnrealizedRevenue);
            saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
            rentalIncomeBeforeTotal = NumberUtil.add(rentalIncomeBeforeTotal, saveDTO.getRentalIncome());
            rentalIncomeAfterTotal = NumberUtil.sub(rentalIncomeAfterTotal, saveDTO.getRentalIncome());
            saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            saveDTO.setAccrued(YesOrNoEnum.YES.getCode());
            saveDTO.setIncomeProvisionMethod(contractDTO.getIncomeProvisionMethod());
            if (saveDTO.getRentalIncome() == null || totalUnrealizedRevenue.doubleValue() == 0) {
                saveDTO.setServiceFeeAmortizationRate(BigDecimal.ZERO);
            } else {
                saveDTO.setServiceFeeAmortizationRate(saveDTO.getRentalIncome().divide(totalUnrealizedRevenue, 6, RoundingMode.HALF_UP));
            }
        }

        List<RepaymentPlanXWEntity> result = BeanUtil.copyToList(newRepayments, RepaymentPlanXWEntity.class);
        result.parallelStream().forEach(e -> {
            if (e.getRentalIncome() == null) {
                e.setRentalIncome(BigDecimal.ZERO);
            }
            e.setContractCode(repayments.get(0).getContractCode());
            e.setOrgId(repayments.get(0).getOrgId());
            e.setClientCode(repayments.get(0).getClientCode());
            e.setClientName(repayments.get(0).getClientName());
            e.setMessageId(repayments.get(0).getMessageId());
            e.setSystemCode(repayments.get(0).getSystemCode());
            e.setSourceIrrRate(repayments.get(0).getSourceIrrRate());
            e.setPlanDatePeriod(Integer.valueOf(DateUtil.format(e.getPlanDate(), DatePattern.SIMPLE_MONTH_PATTERN)));
            e.setCreateBy(UserUtils.getStaffCode());
            e.setUpdateBy(UserUtils.getStaffCode());
            e.setCreateTime(LocalDateTime.now());
            e.setUpdateTime(LocalDateTime.now());
            e.setDelFlag(YesOrNoEnum.NO.getCode());
        });
        return result;
    }

    private List<RepaymentPlanSaveDTO> irrApportion(List<RepaymentPlanXWEntity> repayments, Double incomeRateDouble) {

        // 3.计算当月分摊金额
        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanXWEntity saveDTO = repayments.get(i);

            if (i == 0) {
                saveDTO.setRentalIncome(BigDecimal.ZERO);
                saveDTO.setOpeningAmortizedCost(saveDTO.getCashFlow().negate());
                saveDTO.setEndingAmortizedCost(saveDTO.getOpeningAmortizedCost());
            } else {
                RepaymentPlanXWEntity lastSaveDTO = repayments.get(i - 1);
                saveDTO.setOpeningAmortizedCost(lastSaveDTO.getEndingAmortizedCost());
                BigDecimal rentalIncome = NumberUtil.mul(saveDTO.getOpeningAmortizedCost(), incomeRateDouble / 12).
                        setScale(2, RoundingMode.HALF_UP);
                saveDTO.setRentalIncome(rentalIncome);
                saveDTO.setEndingAmortizedCost(
                        NumberUtil.add(saveDTO.getOpeningAmortizedCost(), rentalIncome, saveDTO.getCashFlow().negate()));
                if (i == repayments.size() - 1) {
                    // 最后一期差补
                    BigDecimal compensation = saveDTO.getEndingAmortizedCost().negate();
                    saveDTO.setRentalIncome(NumberUtil.add(saveDTO.getRentalIncome(), compensation));
                    saveDTO.setEndingAmortizedCost(NumberUtil.add(saveDTO.getEndingAmortizedCost(), compensation));
                }
            }
        }

        // 4.继续拆分(是月底,不拆分)
        List<RepaymentPlanSaveDTO> newRepayments = new ArrayList<>();
        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanSaveDTO saveDTO =  new RepaymentPlanSaveDTO();
            BeanUtils.copyProperties(repayments.get(i), saveDTO);
            saveDTO.setIrrMark(true);
            newRepayments.add(saveDTO);
            // 最后一期不拆分
            if (i == repayments.size() - 1) {
                continue;
            }
            // 判断是否月末,不是则拆分
            if (!DateUtil.isLastDayOfMonth(saveDTO.getPlanDate())) {
                RepaymentPlanSaveDTO endOfMonthSaveDTO =  new RepaymentPlanSaveDTO();
                RepaymentPlanXWEntity entity = initRepaymentPlan(DateUtil.beginOfDay(
                        DateUtil.endOfMonth(saveDTO.getPlanDate())), saveDTO.getContractCode());
                BeanUtils.copyProperties(entity, endOfMonthSaveDTO);
                endOfMonthSaveDTO.setOpeningAmortizedCost(saveDTO.getEndingAmortizedCost());
                endOfMonthSaveDTO.setEndingAmortizedCost(saveDTO.getEndingAmortizedCost());
                endOfMonthSaveDTO.setIrrMark(false);
                newRepayments.add(endOfMonthSaveDTO);
            }
        }

        // 拆分分摊金额
        for (int i = 1; i < newRepayments.size(); i++) {
            RepaymentPlanSaveDTO thisPlan = newRepayments.get(i);
            if (!thisPlan.getIrrMark()) {
                // 拆分的数据
                RepaymentPlanSaveDTO lastPlan = newRepayments.get(i - 1);
                RepaymentPlanSaveDTO nextPlan = newRepayments.get(i + 1);
                long betweenDay = DateUtil.betweenDay(nextPlan.getPlanDate(), lastPlan.getPlanDate(), true);
                if (betweenDay == 0) {
                    repayments.forEach(e -> e.setExceptionType("存在不同期数相同时间"));
                    return null;
                }
                BigDecimal newRentalIncome = NumberUtil.mul(nextPlan.getRentalIncome(),
                        NumberUtil.div(DateUtil.betweenDay(thisPlan.getPlanDate(), lastPlan.getPlanDate(), true),
                                betweenDay)).setScale(2, RoundingMode.HALF_UP);
                thisPlan.setRentalIncome(newRentalIncome);
            } else {
                // 非拆分的数据 上一期为非拆分的数据，则不处理，上一期为拆分的数据则处理
                RepaymentPlanSaveDTO lastPlan = newRepayments.get(i - 1);
                if (!lastPlan.getIrrMark()) {
                    RepaymentPlanSaveDTO lastTwoPlan = newRepayments.get(i - 2);
                    long betweenDay = DateUtil.betweenDay(thisPlan.getPlanDate(), lastTwoPlan.getPlanDate(), true);
                    if (betweenDay == 0) {
                        repayments.forEach(e -> e.setExceptionType("存在不同期数相同时间"));
                        return null;
                    }
                    BigDecimal newRentalIncome = NumberUtil.mul(thisPlan.getRentalIncome(),
                            NumberUtil.div(DateUtil.betweenDay(thisPlan.getPlanDate(), lastPlan.getPlanDate(), true),
                                    betweenDay)).setScale(2, RoundingMode.HALF_UP);
                    thisPlan.setRentalIncome(newRentalIncome);
                }
            }
        }

        // 尾差计算调整
        BigDecimal totalUnrealizedRevenue = newRepayments.stream().map(e -> e.getRentalIncome()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cashflowSum = newRepayments.stream().map(e -> e.getCashFlow()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal compensation = cashflowSum.subtract(totalUnrealizedRevenue);
        BigDecimal lastRentalIncome = newRepayments.get(newRepayments.size() - 1).getRentalIncome().add(compensation);
        newRepayments.get(newRepayments.size() - 1).setRentalIncome(lastRentalIncome);

        return newRepayments;
    }

    /**
     * 取得临时生成的现金流数据
     */
    private RepaymentPlanXWEntity getGenerateCashflowObj(List<RepaymentPlanXWEntity> dataList) {
        List<RepaymentPlanXWEntity> initDataList = dataList.stream().filter(
                e -> e.getPeriods() != null && e.getPeriods().intValue() == -1).collect(Collectors.toList());
        if (initDataList != null && !initDataList.isEmpty()) {
            RepaymentPlanXWEntity initData = initDataList.get(0);
            dataList.remove(initData);
            return initData;
        }
        return null;
    }

    private void splitPlanDate(Date startDate, RepaymentPlanXWEntity plan, List<RepaymentPlanXWEntity> repayments,
                               int offsetMonth, Iterator<RepaymentPlanXWEntity> leaseIterator) {
        Date planDate = plan.getPlanDate();
        DateTime compareDate = DateUtil.offsetMonth(startDate, offsetMonth);
        if (DateUtil.isSameMonth(compareDate, planDate)) {
            // 新增planDate数据
            repayments.add(plan);
            // 是否还有下一期
            if (leaseIterator.hasNext()) {
                RepaymentPlanXWEntity next = leaseIterator.next();
                offsetMonth = offsetMonth + 1;
                splitPlanDate(startDate, next, repayments, offsetMonth, leaseIterator);
            }
        } else {
            // 新增compareDate数据
            RepaymentPlanXWEntity saveDTO = initRepaymentPlan(compareDate, plan.getContractCode());
            repayments.add(saveDTO);
            offsetMonth = offsetMonth + 1;
            splitPlanDate(startDate, plan, repayments, offsetMonth, leaseIterator);
        }
    }

    private RepaymentPlanXWEntity initRepaymentPlan(Date planDate, String contractCode) {
        RepaymentPlanXWEntity dto = new RepaymentPlanXWEntity();
//        dto.setId(redisService.nextId(CacheConstants.COMMON_PREFIX.concat("_")));
        dto.setId(IdWorker.getId());
        dto.setPlanDate(planDate);
        dto.setContractCode(contractCode);
        dto.setRentAmount(BigDecimal.ZERO);
        dto.setPrincipalAmount(BigDecimal.ZERO);
        dto.setInterestAmount(BigDecimal.ZERO);
        dto.setPrincipalTax(BigDecimal.ZERO);
        dto.setInterestTax(BigDecimal.ZERO);
        dto.setOutflowAmount(BigDecimal.ZERO);
        dto.setPlannedInterest(BigDecimal.ZERO);
        dto.setPlannedPrincipal(BigDecimal.ZERO);
        dto.setCashFlow(BigDecimal.ZERO);
        dto.setOpeningAmortizedCost(BigDecimal.ZERO);
        dto.setEndingAmortizedCost(BigDecimal.ZERO);
        dto.setActualDailyRate(BigDecimal.ZERO);
        dto.setRentalIncome(BigDecimal.ZERO);
        dto.setServiceFeeAmortizationIncome(BigDecimal.ZERO);
        return dto;
    }
}
