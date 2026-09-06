package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.AccrualMethodEnum;
import com.utfinancing.financehub.engine.enums.LeaseTypeEnum;
import com.utfinancing.financehub.engine.enums.RecaptureStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanPLMapper;
import com.utfinancing.financehub.engine.finance.mapper.VoucherAmountInitMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.utils.IRRUtils;
import com.utfinancing.financehub.engine.utils.UserUtils;
import com.utfinancing.financehub.engine.utils.XirrUtils;
import lombok.extern.slf4j.Slf4j;
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


@Service(value = "plAsync")
@Slf4j
public class RepaymentSeriviceForPLAsyncImpl extends ServiceImpl<RepaymentPlanMapper, RepaymentPlanEntity>
        implements IRepaymentAsyncSerivice {

    @Resource
    private RepaymentPlanPLMapper repaymentPlanPLMapper;

    @Resource
    private RepaymentPlanMapper repaymentPlanMapper;

    @Resource
    private VoucherAmountInitMapper voucherAmountInitMapper;

    @Resource
    private IRepaymentPlanService repaymentPlanService;

    @Resource
    private RedisService redisService;

    @Resource
    private IRepaymentPlanExceldataService repaymentPlanExceldataService;

    @Resource
    private IContractTaAmountService contractTaAmountService;

    @Resource
    private IVoucherAmountInitService voucherAmountInitService;

    @Resource
    private IServiceNoAmortizationService serviceNoAmortizationService;

    @Resource
    private IContractService contractService;

    @Resource
    public IOrgCompanyService orgCompanyService;
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
            Map<String, List<RepaymentPlanPLEntity>> dataMap = null;
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

            Iterator<Map.Entry<String, List<RepaymentPlanPLEntity>>> it = dataMap.entrySet().iterator();
            // 循环取出来的合同
            while (it.hasNext()){
                Map.Entry<String, List<RepaymentPlanPLEntity>> entry = it.next();
//                log.info(String.format("生成偿还计划，合同号：%s", entry.getKey()));
                List<RepaymentPlanPLEntity> repaymentPlanPLEntityList = entry.getValue().stream().
                        sorted(Comparator.comparing(RepaymentPlanPLEntity::getPlanDate)).collect(Collectors.toList());


                // 取出临时生成的现金流数据-不参与irr计算
                RepaymentPlanPLEntity tempCashFlowData = this.getGenerateCashflowObj(repaymentPlanPLEntityList);
                // 部分未生成现金流的数据，即期初之后起租的合同也需要生成偿还计划
                boolean isLeaseStart = false;
                Date initDate = DateUtil.endOfDay(DateUtil.endOfMonth(DateUtil.parse(params.getInitDate())));
                if (repaymentPlanPLEntityList.get(0).getPlanDate().compareTo(initDate) >= 0) {
                    isLeaseStart = true;
                }
                if (tempCashFlowData == null && !isLeaseStart) {
                    log.info(String.format("合同编号%s未实现收益不存在或者为0,不再参与收益计提!", entry.getKey()));
                    continue;
                }

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

                // 更新回笼数据
                updateRepaymentPlan(repaymentPlanPLEntityList, returnMoneyMap);

                // 计算irr并生成月底分摊数据
                repaymentPlanPLEntityList = this.businessProcess(repaymentPlanPLEntityList, contractDTO, outstandingAmountMap, false, null);
                // 临时存储第一次计算的分摊情况- 重新计算后，需要覆盖第二次计算后的部分金额
                Map<String, RepaymentPlanPLEntity> repaymentPlanPLEntityMap = repaymentPlanPLEntityList.stream().
                        collect(Collectors.toMap(e->this.getKey(e), (e)->e, (a,b)->b));
                // 判断irr和源数据irr相差是否超过0.01, 未超过不处理， 超过的情况重新将2023-11-30之后的数据重新计算
//                if (xirrRateCompare(repaymentPlanPLEntityList)) {
                // 重新计算irr rate
//                repaymentPlanPLEntityList = this.xirrRecalculation(contractDTO, repaymentPlanPLEntityList, tempCashFlowData,
//                            voucherAmountInitEntityMap);
//                }
                repaymentPlanPLEntityList = this.irrRecalculation(contractDTO, toBeAssessedAmount,
                        repaymentPlanPLEntityList, tempCashFlowData, outstandingAmountMap);
                // 服务费率计算， 并用第一次计算结果覆盖部分字段到第二次计算结果上
                this.serviceFeeRateCompute(repaymentPlanPLEntityList);
                List<RepaymentPlanEntity> result = BeanUtil.copyToList(repaymentPlanPLEntityList, RepaymentPlanEntity.class);

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
     * 取得RepaymentPlanPLEntity的业务主键
     */
    private String getKey(RepaymentPlanPLEntity entity) {
        try {
            StringBuffer result = new StringBuffer(entity.getContractCode());
            String planDateStr = DateUtil.format(entity.getPlanDate(), "yyyyMMdd");
            result.append("|").append(planDateStr);
            return result.toString();
        } catch (Exception e) {
            log.error(JSON.toJSONString(entity));
            e.printStackTrace();
            throw new ServiceException("error");
        }
    }

    /*][
     * 更新回笼的金额
     */
    private void updateRepaymentPlan(List<RepaymentPlanPLEntity> repaymentPlanPLEntityList,
                                     Map<String, BigDecimal> returnMoneyMap) {
        BigDecimal receivedMoneySum = returnMoneyMap.get(repaymentPlanPLEntityList.get(0).getContractCode());
        if (receivedMoneySum == null) {
            receivedMoneySum = BigDecimal.ZERO;
        }

        BigDecimal remainReceivedMoney = receivedMoneySum.add(new BigDecimal(0.1));
        for (RepaymentPlanPLEntity entity : repaymentPlanPLEntityList) {
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

//    private void setActualRepanymentAmount (RepaymentPlanPLEntity repaymentPlanPLEntity, BigDecimal receivedMoneySum) {
//        // 还款日期
//        repaymentPlanPLEntity.setActualRepaymentDate(DateUtils.getNowDate());
//        // 归还本金
//        repaymentPlanPLEntity.setActualRepaymentPrincipalAmount(entity.getActualRepaymentPrincipalAmount());
//        // 归还利息
//        repaymentPlanPLEntity.setActualRepaymentInteresAmount(entity.getActualRepaymentInteresAmount());
//        // 归还租金
//        repaymentPlanPLEntity.setActualRepaymentRentAmount(entity.getActualRepaymentRentAmount());
//        // 回笼状态
//        if (repaymentPlanPLEntity.getRentAmount() != null) {
//            if (entity.getActualRepaymentRentAmount() != null
//                    && entity.getActualRepaymentRentAmount().compareTo(repaymentPlanPLEntity.getRentAmount()) >= 0) {
//                repaymentPlanPLEntity.setRecaptureStatus(RecaptureStatusEnum.RETURNED.getCode());
//            } else if (entity.getActualRepaymentRentAmount() != null && entity.getActualRepaymentRentAmount().compareTo(BigDecimal.ZERO) > 0) {
//                repaymentPlanPLEntity.setRecaptureStatus(RecaptureStatusEnum.PARTIALRECOVERY.getCode());
//            } else {
//                repaymentPlanPLEntity.setRecaptureStatus(RecaptureStatusEnum.NOTRECOVERED.getCode());
//            }
//        }
//        // TA
//        repaymentPlanPLEntity.setTaReclassification(entity.getTa());
//    }

    /**
     * 取得合同的回笼数据
     */
    private Map<String, RepaymentPlanExceldataEntity> getReturnMoneyByContractCode(Set<String> contractCodeList) {
        LambdaQueryWrapper<RepaymentPlanExceldataEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(RepaymentPlanExceldataEntity::getContractCode, contractCodeList);
        queryWrapper.orderByAsc(RepaymentPlanExceldataEntity::getContractCode);
        queryWrapper.orderByAsc(RepaymentPlanExceldataEntity::getPlanDate);
        List<RepaymentPlanExceldataEntity> result = repaymentPlanExceldataService.getBaseMapper().selectList(queryWrapper);

        if (result != null && !result.isEmpty()) {
//            contractTaAmountService.saveTaAmountByExcelData(result);
            Map<String, List<RepaymentPlanExceldataEntity>> groupbyContractAndPeriodsMap = result.stream().collect(Collectors.groupingBy(
                    e -> e.getContractCode().concat("-").concat(e.getPeriods().toString())));
            Map<String, RepaymentPlanExceldataEntity> returnResult = new HashMap<>();
            for (String key : groupbyContractAndPeriodsMap.keySet()) {
                RepaymentPlanExceldataEntity entity = BeanUtil.copyProperties(groupbyContractAndPeriodsMap.get(key).get(0),
                        RepaymentPlanExceldataEntity.class);
                entity.setActualRepaymentRentAmount(groupbyContractAndPeriodsMap.get(key).stream().
                        map(RepaymentPlanExceldataEntity::getActualRepaymentRentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                entity.setActualRepaymentPrincipalAmount(groupbyContractAndPeriodsMap.get(key).stream().
                        map(RepaymentPlanExceldataEntity::getActualRepaymentPrincipalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                entity.setActualRepaymentInteresAmount(groupbyContractAndPeriodsMap.get(key).stream().
                        map(RepaymentPlanExceldataEntity::getActualRepaymentInteresAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                returnResult.put(key, entity);
            }

            return returnResult;
        }  else {
            return new HashMap<>();
        }
    }

//    private List<RepaymentPlanPLEntity> xirrRecalculation(ContractDTO contractDTO,
//            List<RepaymentPlanPLEntity> repaymentPlanPLEntityList, RepaymentPlanPLEntity tempCashFlowData,
//            Map<String, List<VoucherAmountInitEntity>> voucherAmountInitEntityMap) {
//        if (tempCashFlowData == null) {
//            return repaymentPlanPLEntityList;
//        }
//
//        List<RepaymentPlanPLEntity> result = new ArrayList<>();
//        List<RepaymentPlanPLEntity> recalculationList = new ArrayList<>();
//        recalculationList.add(tempCashFlowData);
//
//        // 收益数据
//        Map<String, BigDecimal> voucherAmountInitList = getVoucherAmountInitEntity(
//                voucherAmountInitEntityMap, repaymentPlanPLEntityList);
//
//        for (int i = 0; i < repaymentPlanPLEntityList.size(); i++) {
//            RepaymentPlanPLEntity e = repaymentPlanPLEntityList.get(i);
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

    private List<RepaymentPlanPLEntity> irrRecalculation(ContractDTO contractDTO, BigDecimal toBeAssessedAmount,
                                                          List<RepaymentPlanPLEntity> repaymentPlanPLEntityList,
                                                         RepaymentPlanPLEntity tempCashFlowData,
                                                         Map<String, BigDecimal> outstandingAmountMap) {
        if (tempCashFlowData == null) {
            return repaymentPlanPLEntityList;
        }

        // 租赁收入求和
        BigDecimal cashflowSum = repaymentPlanPLEntityList.stream().map(e->e.getCashFlow()).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RepaymentPlanPLEntity> result = new ArrayList<>();
        List<RepaymentPlanPLEntity> recalculationList = new ArrayList<>();
        List<RepaymentPlanPLEntity> periodsNullList = new ArrayList<>();
        recalculationList.add(BeanUtil.copyProperties(tempCashFlowData, RepaymentPlanPLEntity.class));

        BigDecimal rentalIncomeSum = BigDecimal.ZERO;
        BigDecimal recalculationRentalIncomeSum = BigDecimal.ZERO;
        boolean isSetTempCashFlow = false;
        for (int i = 0; i < repaymentPlanPLEntityList.size(); i++) {
            RepaymentPlanPLEntity e = repaymentPlanPLEntityList.get(i);
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
            Date lastPlanDate = repaymentPlanPLEntityList.get(repaymentPlanPLEntityList.size() - 1).getPlanDate();
            int compareValue = DateUtils.truncatedCompareTo(lastPlanDate, tempCashFlowData.getPlanDate(), Calendar.MONTH);

            // 最后一期日期年月等于期初的年月
            if (compareValue == 0) {
                result.stream().forEach(k -> {
                    k.setExceptionType("期初日期即为计划结束日期");
                });
            }

            // 最后一期日期年月大于期初的年月
            if (compareValue > 0) {
                result.get(result.size() - 1).setRentalIncome(toBeAssessedAmount == null ? BigDecimal.ZERO : toBeAssessedAmount);
            }
        }
        Date date = tempCashFlowData.getPlanDate();
        RepaymentPlanPLEntity lastEntity = result.get(result.size() - 1);
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
            Map<String, List<VoucherAmountInitEntity>> voucherAmountInitEntityMap,
            List<RepaymentPlanPLEntity> repaymentPlanPLEntityList) {

        String contractCode = repaymentPlanPLEntityList.get(0).getContractCode();
        List<VoucherAmountInitEntity> voucherAmountInitList = voucherAmountInitEntityMap.get(contractCode);
        // 如果一条数据都没有则重新从中间库取得收益数据
        if (voucherAmountInitList == null || voucherAmountInitList.isEmpty()) {
            String startPeriod = DateUtils.parseDateToStr("yyyyMM", repaymentPlanPLEntityList.get(0).getPlanDate());
            String endPeriod = DateUtils.parseDateToStr("yyyyMM", repaymentPlanPLEntityList.get(
                    repaymentPlanPLEntityList.size() - 1).getPlanDate());
            return voucherAmountInitService.getVoucherAmountInit(startPeriod, endPeriod, contractCode);
        } else {
            return voucherAmountInitList.stream().collect(Collectors.toMap(
                    e -> e.getPeriod(), VoucherAmountInitEntity::getDtAmount, (a, b) -> a.add(b)));
        }
    }

    /**
     * 算出来的xirrate与源利率比较
     */
    private boolean xirrRateCompare(List<RepaymentPlanPLEntity> repaymentPlanPLEntityList) {
        RepaymentPlanPLEntity repaymentPlanPLEntity = repaymentPlanPLEntityList.get(0);

        if (repaymentPlanPLEntity.getXirrRate() == null || repaymentPlanPLEntity.getXirrRate().doubleValue() == 0
            || repaymentPlanPLEntity.getSourceIrrRate() == null
            || repaymentPlanPLEntity.getSourceIrrRate().doubleValue() == 0) {
            return false;
        }

        Double xirrRateCompareValue = repaymentPlanPLEntity.getXirrRate().subtract(
                repaymentPlanPLEntity.getSourceIrrRate()).doubleValue();
        if (Math.abs(xirrRateCompareValue) > 0.01) {
            return true;
        }
        return false;
    }

    /**
     * 取得待处理数据
     */
    private Map<String, List<RepaymentPlanPLEntity>> selectHyInitDataByPage(int startIndex, int limit) {
        GetProcessDataDTO params = new GetProcessDataDTO();
        params.setPageNum(startIndex);
        params.setPageSize(limit);
        List<RepaymentPlanPLEntity> dbData = repaymentPlanPLMapper.selectHyInitDataByPage(params);
        Map<String, List<RepaymentPlanPLEntity>> dataMap = dbData.stream().collect(
                Collectors.groupingBy(e -> e.getContractCode()));
        return dataMap;
    }

    /**
     * 是否连续分月判断
     */
    private boolean isContinuousMonth(List<RepaymentPlanPLEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            RepaymentPlanPLEntity entity = dataList.get(i);
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
    private List<RepaymentPlanPLEntity> businessProcess(List<RepaymentPlanPLEntity> dataList, ContractDTO contractDTO,
                                                        Map<String, BigDecimal> outstandingAmountMap, boolean isRecompute,
                                                        List<RepaymentPlanPLEntity> periodsNullList) {
        // 偿还计划数据
        List<RepaymentPlanPLEntity> repayments = new ArrayList<>();

        // 1.补齐合同从起租到结清的每月数据
//        Date startPlanDate = dataList.get(0).getPlanDate();
        // 判断原数据是否为每月都有数据，如否，则需要补齐每月的数据对象，如是，则不用补齐
        if (this.isContinuousMonth(dataList)) {
            repayments.addAll(dataList);
        } else {
//            Iterator<RepaymentPlanPLEntity> leaseIterator = dataList.iterator();
//            RepaymentPlanPLEntity next = leaseIterator.next();
//            splitPlanDate(startPlanDate, next, repayments, 1, leaseIterator);
            repayments.addAll(this.createEmptyRepaymentsData(dataList));
        }

        // 2.通过IRR计算利率
        Double incomeRateDouble = 0.00;
        if (AccrualMethodEnum.XIRR.getCode().equals(contractDTO.getIncomeProvisionMethod())){
            List<Double> cashFlowList = dataList.stream().map(e->e.getCashFlow().doubleValue()).collect(Collectors.toList());
            List<Date> allPlanDate = dataList.stream().map(e->e.getPlanDate()).collect(Collectors.toList());
            incomeRateDouble = XirrUtils.xirr(cashFlowList, allPlanDate);
        } else {
            incomeRateDouble = IRRUtils.calculateIRRYear(
                    repayments.stream().map(e -> e.getCashFlow().doubleValue()).collect(Collectors.toList()));
        }
        if (Double.isNaN(incomeRateDouble) || incomeRateDouble.doubleValue() == 0) {
            if (CollUtil.isNotEmpty(periodsNullList)) {
                repayments.addAll(periodsNullList);
                // 通过时间排序
                repayments.sort(Comparator.comparing(RepaymentPlanPLEntity::getPlanDate));
            }
            for (int i = 0; i < repayments.size(); i++) {
                RepaymentPlanPLEntity e = repayments.get(i);
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
        }

        List<RepaymentPlanPLEntity> result = BeanUtil.copyToList(newRepayments, RepaymentPlanPLEntity.class);
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

    /**
     * 服务费率计算
     */
    private void serviceFeeRateCompute(List<RepaymentPlanPLEntity> repaymentPlanPLEntityList) {
        BigDecimal totalUnrealizedRevenue = repaymentPlanPLEntityList.stream().map(e -> e.getRentalIncome()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        for (int i = 0; i < repaymentPlanPLEntityList.size(); i++) {
            RepaymentPlanPLEntity saveDTO = repaymentPlanPLEntityList.get(i);
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
                        repaymentPlanPLEntityList.get(i - 1).getRentalIncomeBeforeTotal(),
                        repaymentPlanPLEntityList.get(i - 1).getRentalIncome());
                saveDTO.setRentalIncomeBeforeTotal(rentalIncomeBeforeTotal);
                BigDecimal rentalIncomeAfterTotal = totalUnrealizedRevenue.subtract(
                        saveDTO.getRentalIncomeBeforeTotal()).subtract(saveDTO.getRentalIncome());
                saveDTO.setRentalIncomeAfterTotal(rentalIncomeAfterTotal);
            }
        }
    }

    private List<RepaymentPlanSaveDTO> irrApportion(List<RepaymentPlanPLEntity> repayments, Double incomeRateDouble) {

        // 3.计算当月分摊金额
        for (int i = 0; i < repayments.size(); i++) {
            RepaymentPlanPLEntity saveDTO = repayments.get(i);

            if (i == 0) {
                saveDTO.setRentalIncome(BigDecimal.ZERO);
                saveDTO.setOpeningAmortizedCost(saveDTO.getCashFlow().negate());
                saveDTO.setEndingAmortizedCost(saveDTO.getOpeningAmortizedCost());
            } else {
                RepaymentPlanPLEntity lastSaveDTO = repayments.get(i - 1);
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
                RepaymentPlanPLEntity entity = initRepaymentPlan(DateUtil.beginOfDay(
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
    private RepaymentPlanPLEntity getGenerateCashflowObj(List<RepaymentPlanPLEntity> dataList) {
        List<RepaymentPlanPLEntity> initDataList = dataList.stream().filter(
                e -> e.getPeriods() != null && e.getPeriods().intValue() == -1).collect(Collectors.toList());
        if (initDataList != null && !initDataList.isEmpty()) {
            RepaymentPlanPLEntity initData = initDataList.get(0);
            dataList.remove(initData);
            return initData;
        }
        return null;
    }

    /**
     * 此方法递归太深 容易堆栈溢出
     */
    private void splitPlanDate(Date startDate, RepaymentPlanPLEntity plan, List<RepaymentPlanPLEntity> repayments,
                               int offsetMonth, Iterator<RepaymentPlanPLEntity> leaseIterator) {
        Date planDate = plan.getPlanDate();
        DateTime compareDate = DateUtil.offsetMonth(startDate, offsetMonth);
        if (DateUtil.isSameMonth(compareDate, planDate)) {
            // 新增planDate数据
            repayments.add(plan);
            // 是否还有下一期
            if (leaseIterator.hasNext()) {
                RepaymentPlanPLEntity next = leaseIterator.next();
                offsetMonth = offsetMonth + 1;
                splitPlanDate(startDate, next, repayments, offsetMonth, leaseIterator);
            }
        } else {
            // 新增compareDate数据
            RepaymentPlanPLEntity saveDTO = initRepaymentPlan(compareDate, plan.getContractCode());
            repayments.add(saveDTO);
            offsetMonth = offsetMonth + 1;
            splitPlanDate(startDate, plan, repayments, offsetMonth, leaseIterator);
        }
    }

    private List<RepaymentPlanPLEntity> createEmptyRepaymentsData(List<RepaymentPlanPLEntity> dataList) {
//        Map<String, Date> dataPlanDateMap = dataList.stream().collect(Collectors.toMap(e -> DateUtils.parseDateToStr(
//                DateUtils.YYYY_MM_DD, e.getPlanDate()),  (e) -> e.getPlanDate()));

        List<RepaymentPlanPLEntity> repayments = new ArrayList<>();
        Date lastDate = dataList.get(0).getPlanDate();
        Date compareDate = DateUtil.offsetMonth(lastDate, 1);

        for (int i = 0; i < dataList.size(); i++) {
            if (i == 0) {
                repayments.add(dataList.get(i));
            } else {
                while (true) {
                    if (DateUtil.isSameMonth(compareDate, dataList.get(i).getPlanDate())) {
                        repayments.add(dataList.get(i));
                        compareDate = DateUtil.offsetMonth(compareDate, 1);
                        break;
                    }  else if (DateUtil.compare(compareDate, dataList.get(i).getPlanDate()) > 0) {
                        break;
                    } else {
                        repayments.add(this.initRepaymentPlan(compareDate, dataList.get(0).getContractCode()));
                        compareDate = DateUtil.offsetMonth(compareDate, 1);
                    }
                }
            }
        }
        return repayments;
    }

    private RepaymentPlanPLEntity initRepaymentPlan(Date planDate, String contractCode) {
        RepaymentPlanPLEntity dto = new RepaymentPlanPLEntity();
//        dto.setId(redisService.nextId(CacheConstants.COMMON_PREFIX.concat("_")));
        dto.setId(IdWorker.getId());
        dto.setContractCode(contractCode);
        dto.setPlanDate(planDate);
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
