package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.BusinessEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeDetailsNewMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanNewQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanNewDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeMonthlyData;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanNewVO;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeePlanNewMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import com.utfinancing.financehub.engine.utils.FinhubAmountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :  ServiceFeePlanNew服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceFeePlanNewServiceImpl extends ServiceImpl<ServiceFeePlanNewMapper, ServiceFeePlanNewEntity> implements IServiceFeePlanNewService {

    private final ServiceFeePlanNewMapper serviceFeePlanNewMapper;
    private final ServiceFeeDetailsNewMapper serviceFeeDetailsNewMapper;
    private final IContractMonthService contractMonthService;
    private final IRepaymentPlanService repaymentPlanService;
    private final IRepaymentPlanSnapshotService repaymentPlanSnapshotService;

    @Override
    public Long saveServiceFeePlanNew(ServiceFeePlanNewDTO dto) {
        ServiceFeePlanNewEntity entity = BeanUtil.copyProperties(dto, ServiceFeePlanNewEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateServiceFeePlanNew(Long id, ServiceFeePlanNewDTO dto) {
        ServiceFeePlanNewEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ServiceFeePlanNewDTO getServiceFeePlanNewDTOById(Long id) {
        ServiceFeePlanNewEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ServiceFeePlanNewDTO.class);
    }

    @Override
    public IPage<ServiceFeePlanNewVO> selectPage(ServiceFeePlanNewQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeePlanNewEntity> queryWrapper = Wrappers.<ServiceFeePlanNewEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ServiceFeePlanNewEntity> entityIPage = serviceFeePlanNewMapper.selectPage(new Page<ServiceFeePlanNewEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ServiceFeePlanNewVO.class);
    }


    @Override
    public void calculationServiceFeePlanNew(ServiceFeeQueryDTO queryDTO) {
        //查询主合同
        LambdaQueryWrapper<ContractMonthEntity> getMainContractCodeWrapper = new LambdaQueryWrapper<>();
        getMainContractCodeWrapper.in(ContractMonthEntity::getContractCategory, Lists.newArrayList("1", "3"));
        getMainContractCodeWrapper.eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        getMainContractCodeWrapper.eq(ContractMonthEntity::getSharingServiceFeeFlag, true);
        List<ContractMonthEntity> list = contractMonthService.list(getMainContractCodeWrapper);
        List<String> mainContractCodeList = list.stream().map(ContractMonthEntity::getContractCode).collect(Collectors.toList());
        List<String> leaseStartDateIsNullList = list.stream().filter(v -> v.getLeaseDateStart() == null).map(ContractMonthEntity::getContractCode).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(leaseStartDateIsNullList)) {
            throw new ServiceException("合同[" + leaseStartDateIsNullList.stream().
                    collect(Collectors.joining(",")) + "]无起租日期，不能判断是否当年合同");
        }
        //查询已有服务费计划
        LambdaQueryWrapper<ServiceFeePlanNewEntity> getServiceFeePlanWrapper = new LambdaQueryWrapper<>();
        getServiceFeePlanWrapper.in(ServiceFeePlanNewEntity::getContractCode, mainContractCodeList);
        getServiceFeePlanWrapper.eq(ServiceFeePlanNewEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<ServiceFeePlanNewEntity> serviceFeePlanNewEntities = serviceFeePlanNewMapper.selectList(getServiceFeePlanWrapper);
        Map<String, List<ServiceFeePlanNewEntity>> serviceFeePlanMap = serviceFeePlanNewEntities.stream().collect(Collectors.groupingBy(ServiceFeePlanNewEntity::getContractCode));
        //查询服务费合同
        LambdaQueryWrapper<ContractMonthEntity> getServiceFeeCodeWrapper = new LambdaQueryWrapper<>();
        getServiceFeeCodeWrapper.in(ContractMonthEntity::getContractCodeM, mainContractCodeList);
        getServiceFeeCodeWrapper.eq(ContractMonthEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<ContractMonthEntity> serviceFeeList = contractMonthService.list(getServiceFeeCodeWrapper);
        Map<String, List<ContractMonthEntity>> serviceFeeMap = serviceFeeList.stream().collect(Collectors.groupingBy(ContractMonthEntity::getContractCodeM));

        //落库
        List<ServiceFeePlanNewEntity> updates = new ArrayList<>();
        List<Long> removes = new ArrayList<>();
        List<ServiceFeePlanNewEntity> inserts = new ArrayList<>();
        //计算服务费计划
        for (ContractMonthEntity contractCodeM : list) {
            List<ServiceFeePlanNewEntity> serviceFeePlanNews = serviceFeePlanMap.get(contractCodeM.getContractCode());
            if (serviceFeePlanNews != null) {
                //分摊比例是否变更,变更重算分摊计划
                if (!CommonDateUtils.isPreviousYears(contractCodeM.getLeaseDateStart()) && serviceFeePlanNews.get(0).getAccrualRate().compareTo(queryDTO.getAllocationRatio()) != 0) {
                    // 查询偿还计划
                    List<RepaymentPlanEntity> repayments = repaymentPlanService.selectListPrioritySnapshot(Lists.newArrayList(contractCodeM.getContractCode()));
                    Map<Date, BigDecimal> repaymentMap = getRepaymentMap(contractCodeM, repayments);
                    if (repaymentMap == null) continue;
                    if (extracted(queryDTO, contractCodeM, serviceFeeMap, repaymentMap, inserts)) continue;
                    removes.addAll(serviceFeePlanNews.stream().map(ServiceFeePlanNewEntity::getId).collect(Collectors.toList()));
                }
            } else {
                //查询偿还计划
                List<RepaymentPlanEntity> repayments = repaymentPlanService.selectListPrioritySnapshot(Lists.newArrayList(contractCodeM.getContractCode()));
                Map<Date, BigDecimal> repaymentMap = getRepaymentMap(contractCodeM, repayments);
                if (repaymentMap == null) continue;
                if (extracted(queryDTO, contractCodeM, serviceFeeMap, repaymentMap, inserts)) continue;
                //保存偿还计划快照
                repaymentPlanSnapshotService.remove(new QueryWrapper<>(RepaymentPlanSnapshotEntity.builder().contractCode(contractCodeM.getContractCode()).build()));
                repaymentPlanSnapshotService.saveBatch(BeanUtil.copyToList(repayments, RepaymentPlanSnapshotEntity.class));
            }

        }
        this.removeByIds(removes);
        this.updateBatchById(updates);
        this.saveBatch(inserts);
    }

    private static boolean extracted(ServiceFeeQueryDTO queryDTO, ContractMonthEntity contractCodeM, Map<String, List<ContractMonthEntity>> serviceFeeMap, Map<Date, BigDecimal> repaymentMap, List<ServiceFeePlanNewEntity> inserts) {
        //创建服务费分摊计划
        List<ContractMonthEntity> serviceFeeListByContractCodeM = serviceFeeMap.get(contractCodeM.getContractCode());
        if (CollectionUtils.isEmpty(serviceFeeListByContractCodeM)) return true;
        BigDecimal contractDeviceAmount = contractCodeM.getPayableDeviceAmount();
        //服务费总额
        BigDecimal actualServiceAmount = serviceFeeListByContractCodeM.stream().filter(a -> ObjectUtil.isNotEmpty(a.getActualServiceAmount()))
                .map(ContractMonthEntity::getActualServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //分摊起始金额
        BigDecimal allocateStartingAmount = NumberUtil.mul(contractDeviceAmount, queryDTO.getAllocationRatio());
        //合同确认金额
//                BigDecimal contractConfirmedAmount = NumberUtil.min(receivableServiceFeeTotal, allocateStartingAmount);
        //服务费协议金额
//                BigDecimal serviceFeeAgreedAmount = NumberUtil.div(receivableServiceFeeTotal, allocateStartingAmount);
        //实收服务费总额
//                BigDecimal actualSericeAmountTotal = serviceFeeListByContractCodeM.stream().filter(a -> ObjectUtil.isNotEmpty(a.getActualServiceAmount()))
//                        .map(ContractMonthEntity::getActualServiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //待分摊总服务费
        BigDecimal allocateAmountTotal = actualServiceAmount.subtract(allocateStartingAmount);
        if (allocateAmountTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }

        BigDecimal actualReceiveServiceFeeTotal = BigDecimal.ZERO;
//                BigDecimal agreedConfirmedAmountTotal = BigDecimal.ZERO;
        for (ContractMonthEntity serviceContract : serviceFeeListByContractCodeM) {
            if (allocateAmountTotal.compareTo(BigDecimal.ZERO) == 0 || serviceContract.getActualServiceAmount().compareTo(BigDecimal.ZERO) == 0) break;
            actualReceiveServiceFeeTotal = NumberUtil.add(actualReceiveServiceFeeTotal, serviceContract.getActualServiceAmount());
            // 协议一次性确认金额=（实收服务费金额之和与合同一次性确认金额的较小值）-协议一次性确认金额之和
            BigDecimal allocateAmount = NumberUtil.min(allocateAmountTotal, serviceContract.getActualServiceAmount());
            allocateAmountTotal = allocateAmountTotal.subtract(allocateAmount);
//                    BigDecimal agreedConfirmedAmount = NumberUtil.sub(NumberUtil.min(actualReceiveServiceFeeTotal, serviceContract.getActualServiceAmount()), agreedConfirmedAmountTotal);
//                    agreedConfirmedAmountTotal = NumberUtil.add(agreedConfirmedAmountTotal, agreedConfirmedAmount);
//                    BigDecimal planApportionAmount = NumberUtil.sub(serviceContract.getActualServiceAmount(), agreedConfirmedAmount);
            int periods = 1;
//                    BigDecimal shouldApportionmentAmountTaxInclude = NumberUtil.sub(NumberUtil.min(allocateAmountTotal, contractConfirmedAmount), agreedConfirmedAmountTotal);
            List<Date> collect = repaymentMap.keySet().stream().sorted(Comparator.comparing(Date::getTime)).collect(Collectors.toList());
            BigDecimal planAmountTaxIncludeTotal = BigDecimal.ZERO;
            BigDecimal PlanAmountNoTaxTotal = BigDecimal.ZERO;
            BigDecimal apportionmentRateTotal = BigDecimal.ZERO;
            for (int i = 0; i < collect.size(); i++) {
                Date planDate = collect.get(i);
                BigDecimal rate = repaymentMap.get(planDate);
                ServiceFeePlanNewEntity serviceFeePlanNewEntity = new ServiceFeePlanNewEntity();
                serviceFeePlanNewEntity.setContractCode(contractCodeM.getContractCode());
                serviceFeePlanNewEntity.setOrgId(contractCodeM.getOrgId());
                serviceFeePlanNewEntity.setServiceFeeNo(serviceContract.getContractCode());
                serviceFeePlanNewEntity.setAccrualRate(queryDTO.getAllocationRatio());
                serviceFeePlanNewEntity.setServiceOrgId(serviceContract.getOrgId());
                serviceFeePlanNewEntity.setApportionmentRate(rate);
                serviceFeePlanNewEntity.setReceivableServiceFeeTaxInclude(serviceContract.getReceivableServiceAmount());
                serviceFeePlanNewEntity.setReceivableServiceFeeNoTax(FinhubAmountUtils.amountNoTax(serviceContract.getReceivableServiceAmount()));
                serviceFeePlanNewEntity.setReceivedServiceFeeTaxInclude(serviceContract.getActualServiceAmount());
                serviceFeePlanNewEntity.setReceivedServiceFeeNoTax(FinhubAmountUtils.amountNoTax(serviceContract.getActualServiceAmount()));
                serviceFeePlanNewEntity.setShouldApportionmentAmountTaxInclude(allocateAmount);
                serviceFeePlanNewEntity.setShouldApportionmentAmountNoTax(FinhubAmountUtils.amountNoTax(serviceFeePlanNewEntity.getShouldApportionmentAmountTaxInclude()));
                serviceFeePlanNewEntity.setPlanDate(planDate);
                serviceFeePlanNewEntity.setPeriods(periods++);
                apportionmentRateTotal = apportionmentRateTotal.add(rate);
                serviceFeePlanNewEntity.setPlanAmountTaxInclude(FinhubAmountUtils.multiply(serviceFeePlanNewEntity.getShouldApportionmentAmountTaxInclude(), apportionmentRateTotal).subtract(planAmountTaxIncludeTotal));
                serviceFeePlanNewEntity.setPlanAmountNoTax(FinhubAmountUtils.multiply(serviceFeePlanNewEntity.getShouldApportionmentAmountNoTax(), apportionmentRateTotal).subtract(PlanAmountNoTaxTotal));
                planAmountTaxIncludeTotal = planAmountTaxIncludeTotal.add(serviceFeePlanNewEntity.getPlanAmountTaxInclude());
                PlanAmountNoTaxTotal = PlanAmountNoTaxTotal.add(serviceFeePlanNewEntity.getPlanAmountNoTax());
                serviceFeePlanNewEntity.setPlanAmountTotalTaxInclude(planAmountTaxIncludeTotal);
                serviceFeePlanNewEntity.setPlanAmountTotalNoTax(PlanAmountNoTaxTotal);
                inserts.add(serviceFeePlanNewEntity);
            }
        }
        return false;
    }

    private static Map<Date, BigDecimal> getRepaymentMap(ContractMonthEntity contractCodeM, List<RepaymentPlanEntity> repayments) {
        if (CollUtil.isEmpty(repayments)) {
            // 没有偿还计划，跳过
            log.info("合同没有偿还计划：{}", contractCodeM.getContractCode());
            return null;
        }
        // 将偿还计划
        Map<Date, BigDecimal> repaymentMap = repayments.stream().collect(Collectors.toMap(
                a -> DateUtil.beginOfDay(DateUtil.endOfMonth(a.getPlanDate())),
                RepaymentPlanEntity::getServiceFeeAmortizationRate, BigDecimal::add, LinkedHashMap::new));
        return repaymentMap;
    }


    @Override
    public List<ServiceFeePlanNewVO> selectDetailPlanList(ServiceFeeDetailsQueryDTO queryDTO) {
        String contractCode = queryDTO.getContractCode();
        if (StringUtils.isBlank(contractCode)) {
            return new ArrayList<>();
        }
        List<ServiceFeePlanNewVO> repaymentPlanVOS = serviceFeePlanNewMapper.selectRepaymentPlanByContractCode(contractCode, queryDTO.getServiceFeeNo());
//        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(repaymentPlanVOS)) {
//            for (ServiceFeePlanNewVO repaymentPlanVO : repaymentPlanVOS) {
//                repaymentPlanVO.setBusinessName(BusinessEnum.getDescByCode(repaymentPlanVO.getBusinessCode()));
//            }
//        }
        return repaymentPlanVOS;
    }

    @Override
    public List<ServiceFeeAllocationExcelVo> getAllAllocationsPlanList(ServiceFeeQueryDTO queryDTO) {

        LambdaQueryWrapper<ServiceFeeDetailsNewEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceFeeDetailsNewEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.apply("TO_CHAR(business_date, 'YYYY-MM') = {0}", DateUtil.format(queryDTO.getBusinessDate(), "yyyy-MM"));
        List<ServiceFeeDetailsNewEntity> list = serviceFeeDetailsNewMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<String> contractCodes = list.stream().map(ServiceFeeDetailsNewEntity::getContractCode).collect(Collectors.toList());
        LambdaQueryWrapper<ServiceFeePlanEntity> planQueryWrapper = new LambdaQueryWrapper<>();
        planQueryWrapper.in(ServiceFeePlanEntity::getContractCode, contractCodes);
        planQueryWrapper.eq(ServiceFeePlanEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<ServiceFeeAllocationExcelVo> serviceFeePlanEntities = serviceFeePlanNewMapper.selectServiceFeeAllocationExcelList(contractCodes);
        Map<String, List<ServiceFeeAllocationExcelVo>> planGroupByServiceNo = serviceFeePlanEntities.stream().collect(Collectors.groupingBy(v -> v.getServiceFeeNo().concat("@").concat(v.getServiceOrgId())));
        List<ServiceFeeAllocationExcelVo> results = new ArrayList<>();
        for (ServiceFeeDetailsNewEntity ServiceFeeDetailsNewEntity : list) {
            ServiceFeeAllocationExcelVo excelVo = new ServiceFeeAllocationExcelVo();
            BeanUtil.copyProperties(ServiceFeeDetailsNewEntity, excelVo);
            excelVo.setLesseeName(ServiceFeeDetailsNewEntity.getClientName());
            excelVo.setPlanApportionAmount(ServiceFeeDetailsNewEntity.getShouldApportionmentAmountTaxInclude());
            excelVo.setPlanApportionNoTax(ServiceFeeDetailsNewEntity.getShouldApportionmentAmountNoTax());
            excelVo.setActualReceive(ServiceFeeDetailsNewEntity.getReceivedServiceFeeTaxInclude());
            excelVo.setActualReceiveNoTax(ServiceFeeDetailsNewEntity.getReceivedServiceFeeNoTax());
            excelVo.setActualAccruedAmount(ServiceFeeDetailsNewEntity.getAccruedAmount());
            List<ServiceFeeAllocationExcelVo> serviceFeePlanEntities1 = planGroupByServiceNo.get(ServiceFeeDetailsNewEntity.getServiceFeeNo().concat("@").concat(ServiceFeeDetailsNewEntity.getServiceOrgId()));
            excelVo.setAllocationRatio(serviceFeePlanEntities1.get(0).getAllocationRatio());
            if (CollectionUtils.isNotEmpty(serviceFeePlanEntities1)) {
                excelVo.setPayableDeviceAmount(serviceFeePlanEntities1.get(0).getPayableDeviceAmount());
                excelVo.setServiceFeeRatio(serviceFeePlanEntities1.get(0).getServiceFeeRatio());
                if (excelVo.getPayableDeviceAmount() != null && excelVo.getPayableDeviceAmount().compareTo(BigDecimal.ZERO) != 0)
                    excelVo.setActualReceiveRatio(excelVo.getActualReceive().divide(excelVo.getPayableDeviceAmount(), 4, BigDecimal.ROUND_HALF_UP));
            }
            if (CollectionUtils.isNotEmpty(serviceFeePlanEntities1)) {
                // 按照 planDate 字段升序排序
                serviceFeePlanEntities1 = serviceFeePlanEntities1.stream()
                        .sorted(Comparator.comparing(ServiceFeeAllocationExcelVo::getPlanDate))
                        .collect(Collectors.toList());
                List<ServiceFeeMonthlyData> monthlyDatas = new ArrayList<>();
                for (ServiceFeeAllocationExcelVo serviceFeePlanEntity : serviceFeePlanEntities1) {
                    ServiceFeeMonthlyData monthlyData = new ServiceFeeMonthlyData();
                    monthlyData.setMonth("计划数" + DateUtil.format(serviceFeePlanEntity.getPlanDate(), "yyyy-MM"));
                    monthlyData.setAmount(serviceFeePlanEntity.getPlanAmountTaxInclude());
                    monthlyDatas.add(monthlyData);
                }
                excelVo.setMonthlyDatas(monthlyDatas);
            }
            results.add(excelVo);
        }
        return results;
    }


}

