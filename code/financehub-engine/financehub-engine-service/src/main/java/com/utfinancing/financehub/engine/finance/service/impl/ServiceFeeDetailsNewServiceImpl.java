package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsNewQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeDetailsNewVO;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeDetailsNewMapper;
import com.utfinancing.financehub.engine.finance.service.IContractStatusRecordService;
import com.utfinancing.financehub.engine.finance.service.IServiceFeeDetailsNewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IServiceFeePlanNewService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.utils.CommonDateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :  ServiceFeeDetailsNew服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ServiceFeeDetailsNewServiceImpl extends ServiceImpl<ServiceFeeDetailsNewMapper, ServiceFeeDetailsNewEntity> implements IServiceFeeDetailsNewService {

    private final ServiceFeeDetailsNewMapper serviceFeeDetailsNewMapper;
    private final IServiceFeePlanNewService serviceFeePlanNewService;
    private final IContractStatusRecordService contractStatusRecordService;
    private final IVoucherService voucherService;

    @Override
    public IPage<ServiceFeeDetailsNewVO> selectPage(ServiceFeeDetailsNewQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeeDetailsNewEntity> queryWrapper = Wrappers.<ServiceFeeDetailsNewEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ServiceFeeDetailsNewEntity> entityIPage = serviceFeeDetailsNewMapper.selectPage(new Page<ServiceFeeDetailsNewEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ServiceFeeDetailsNewVO.class);
    }

    @Override
    public List<ServiceFeeDetailsNewEntity> selectLastPeriodData(ServiceFeeDetailsNewVO serviceFeeDetailsVO) {
        LambdaQueryWrapper<ServiceFeeDetailsNewEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ServiceFeeDetailsNewEntity::getShouldApportionmentAmountNoTax, ServiceFeeDetailsNewEntity::getShouldApportionmentAmountTaxInclude);
        queryWrapper.eq(ServiceFeeDetailsNewEntity::getContractCode, serviceFeeDetailsVO.getContractCode());
        queryWrapper.eq(ServiceFeeDetailsNewEntity::getOrgId, serviceFeeDetailsVO.getOrgId());
        queryWrapper.eq(ServiceFeeDetailsNewEntity::getServiceFeeNo, serviceFeeDetailsVO.getServiceFeeNo());
        queryWrapper.eq(ServiceFeeDetailsNewEntity::getServiceOrgId, serviceFeeDetailsVO.getServiceOrgId());
        String lastMonth = DateUtil.format(DateUtil.offsetMonth(serviceFeeDetailsVO.getBusinessDate(), -1), "yyyy-MM");
        queryWrapper.apply("TO_CHAR(business_date, 'YYYY-MM') <= {0}", lastMonth);
        queryWrapper.orderByDesc(ServiceFeeDetailsNewEntity::getBusinessDate);
        queryWrapper.last("limit 1");
        return serviceFeeDetailsNewMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<ServiceFeeDetailsNewVO> selectDetailPage(ServiceFeeDetailsQueryDTO queryDTO) {
        Date queryDate = queryDTO.getBusinessDate();
        if (null != queryDate) {
            queryDate = DateUtil.beginOfDay(DateUtil.endOfMonth(queryDate));
            queryDTO.setBusinessDate(queryDate);
        }
        Date businessStartDate = queryDTO.getBusinessStartDate();
        if (null != businessStartDate) {
            businessStartDate = DateUtil.beginOfMonth(businessStartDate);
            queryDTO.setBusinessStartDate(businessStartDate);
        }
        Date businessEndDate = queryDTO.getBusinessEndDate();
        if (null != businessEndDate) {
            businessEndDate = DateUtil.endOfMonth(businessEndDate);
            queryDTO.setBusinessEndDate(businessEndDate);
        }
        Page<ServiceFeeDetailsNewVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<ServiceFeeDetailsNewVO> serviceFeeDetailsVOS = serviceFeeDetailsNewMapper.selectServiceFeeInfo(page, queryDTO);
        List<String> contractCodes = serviceFeeDetailsVOS.stream().map(ServiceFeeDetailsNewVO::getContractCode).collect(Collectors.toList());
        setPeriodAmount(queryDTO.getBusinessStartDate(), queryDTO.getBusinessEndDate(), contractCodes, serviceFeeDetailsVOS);
        checkTransferStatus(serviceFeeDetailsVOS, contractCodes, queryDTO.getBusinessDate());
        page.setRecords(serviceFeeDetailsVOS);
        return page;
    }

    @Override
    public void updateVoucherStatus(List<Long> ids, String voucherStatus, Integer periodCode) {
        List<ServiceFeeDetailsNewEntity> serviceFeeDetailsEntities = this.list(new LambdaQueryWrapper<ServiceFeeDetailsNewEntity>().in(ServiceFeeDetailsNewEntity::getServiceFeeId, ids));
        List<String> voucherIds = serviceFeeDetailsEntities.stream()
                .map(ServiceFeeDetailsNewEntity::getVoucherId)
                .filter(Objects::nonNull).collect(Collectors.toList());

        voucherService.updateStatusBatchByIds(voucherIds, voucherStatus, periodCode);
    }

    @Override
    public List<ServiceFeeDetailsNewEntity> getUnapportionedAndNoPlanData(ServiceFeeQueryDTO queryDTO, List<String> allocateContractCodes) {
        DateTime preMonth = DateUtil.offsetMonth(queryDTO.getBusinessDate(), -1);
        Integer preYearValue = CommonDateUtils.getYearValue(preMonth);
        Integer preMonthValue = CommonDateUtils.getMonthValue(preMonth);
        Integer year = CommonDateUtils.getYearValue(queryDTO.getBusinessDate());
        Integer month = CommonDateUtils.getMonthValue(queryDTO.getBusinessDate());
        return serviceFeeDetailsNewMapper.getUnapportionedAndNoPlanData(preYearValue, preMonthValue, allocateContractCodes);

    }

    @Override
    public List<ServiceFeeDetailsNewEntity> getUnapportionedAndNoPlanData(String date, List<String> allocateContractCodes) {
        return serviceFeeDetailsNewMapper.getUnapportionedAndNoPlanData1(date,allocateContractCodes);
    }

    private void checkTransferStatus(List<ServiceFeeDetailsNewVO> serviceFeeDetailsVOS, List<String> contractCodes, Date businessDate) {
        if (CollectionUtils.isEmpty(contractCodes)) {
            return;
        }
//        List<ContractStatusRecordEntity> finContractStatusByContractCodes = contractStatusRecordService.listFinContractStatusByContractCodes(contractCodes, Lists.newArrayList(FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc()));
        List<ContractStatusRecordEntity> finContractStatusByContractCodes = contractStatusRecordService.listFinContractStatusByContractCodes(contractCodes, Lists.newArrayList());
        if (CollectionUtils.isNotEmpty(finContractStatusByContractCodes)) {
            List<ContractStatusRecordEntity> finContractStatusByContractCodesGroupByContractCode = finContractStatusByContractCodes.stream().collect(Collectors.groupingBy(ContractStatusRecordEntity::getFinancialContractStatus)).get(FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc());
            Map<String, List<ContractStatusRecordEntity>> collect = finContractStatusByContractCodesGroupByContractCode.stream().collect(Collectors.groupingBy(ContractStatusRecordEntity::getContractCode));
            serviceFeeDetailsVOS.forEach(v -> {
                if (CollectionUtils.isNotEmpty(collect.get(v.getContractCode()))) {
                    Optional<ContractStatusRecordEntity> max = collect.get(v.getContractCode()).stream()
                            .filter(a -> a.getFinancialContractStatusUpdateTime().before(DateUtil.offset(businessDate, DateField.MONTH, 1)))
                            .max(Comparator.comparing(ContractStatusRecordEntity::getFinancialContractStatusUpdateTime));
                    if (max.isPresent() && FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc().equals(max.get().getFinancialContractStatus())) {
                        v.setExceptionType(v.getExceptionType() + "合同内部转让");
                    }

                }
            });
        }
    }

    private void setPeriodAmount(Date businessStartDate, Date businessEndDate, List<String> contractCodes, List<ServiceFeeDetailsNewVO> serviceFeeDetailsVOS) {
        if (CollectionUtils.isNotEmpty(contractCodes)) {
            LambdaQueryWrapper<ServiceFeePlanNewEntity> serviceFeePlanQueryWrapper = new LambdaQueryWrapper<>();
            serviceFeePlanQueryWrapper.in(ServiceFeePlanNewEntity::getContractCode, contractCodes);
            serviceFeePlanQueryWrapper.eq(ServiceFeePlanNewEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            List<ServiceFeePlanNewEntity> serviceFeePlanEntities = serviceFeePlanNewService.list(serviceFeePlanQueryWrapper);

            Map<String, List<ServiceFeePlanNewEntity>> serviceFeePlanGroupByContractCode = serviceFeePlanEntities.stream().collect(Collectors.groupingBy(ServiceFeePlanNewEntity::getContractCode));
            for (ServiceFeeDetailsNewVO serviceFeeDetailsVO : serviceFeeDetailsVOS) {
                List<ServiceFeePlanNewEntity> serviceFeePlanEntities1 = serviceFeePlanGroupByContractCode.get(serviceFeeDetailsVO.getContractCode());
                if (CollectionUtils.isNotEmpty(serviceFeePlanEntities1) && businessStartDate != null && businessEndDate != null) {
                    List<ServiceFeePlanNewEntity> collect = serviceFeePlanEntities1.stream().filter(v -> v.getContractCode().equals(serviceFeeDetailsVO.getContractCode()) &&
                            v.getServiceFeeNo().equals(serviceFeeDetailsVO.getServiceFeeNo()) && v.getOrgId().equals(serviceFeeDetailsVO.getOrgId()) &&
                            v.getServiceOrgId().equals(serviceFeeDetailsVO.getServiceOrgId())).collect(Collectors.toList());
                    BigDecimal before = collect.stream().filter(v -> v.getPlanDate().compareTo(businessStartDate) < 0).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal after = collect.stream().filter(v -> v.getPlanDate().compareTo(businessEndDate) > 0).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal current = collect.stream().filter(v -> v.getPlanDate().compareTo(businessStartDate) >= 0 && v.getPlanDate().compareTo(businessEndDate) <= 0).map(ServiceFeePlanNewEntity::getPlanAmountTaxInclude).reduce(BigDecimal.ZERO, BigDecimal::add);
                    serviceFeeDetailsVO.setBeforeCurrentPeriodPlanAmountTaxInclude(before);
                    serviceFeeDetailsVO.setCurrentPeriodPlanAmountTaxInclude(current);
                    serviceFeeDetailsVO.setAfterCurrentPeriodPlanAmountTaxInclude(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude().subtract(before).subtract(current));
                }
                serviceFeeDetailsVO.setEndSharingServiceFeeFlagStr(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getEndSharingServiceFeeFlag().equals(YesOrNoEnum.YES.getCode())));
                serviceFeeDetailsVO.setSharedFlagStr(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getSharingServiceFeeFlag().equals(YesOrNoEnum.YES.getCode())));
                serviceFeeDetailsVO.setSpecialStatusAdjustmentFlagStr(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getSpecialStatusAdjustmentFlag().equals(YesOrNoEnum.YES.getCode())));
                serviceFeeDetailsVO.setVoucherStatus(YesOrNoEnum.YES.getCode().equals(serviceFeeDetailsVO.getIsGenerateVoucher()) ? "已生成" : "未生成");
                serviceFeeDetailsVO.setAllocationCompletionMark(YesOrNoEnum.getDescByBool(serviceFeeDetailsVO.getEndSharingServiceFeeFlag().equals(YesOrNoEnum.YES.getCode())));
                //设置上月分摊金额
                setLastMonthAllocationAmount(serviceFeeDetailsVO);
                if (serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().compareTo(BigDecimal.ZERO) != 0) {
                    serviceFeeDetailsVO.setThisMonthReclassificationAdjustmentAmountNoTax(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountNoTax().subtract(serviceFeeDetailsVO.getShouldApportionmentAmountNoTax()));
                    serviceFeeDetailsVO.setThisMonthReclassificationAdjustmentAmountTaxInclude(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().subtract(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude()));
                }
                serviceFeeDetailsVO.setBusinessName(BusinessEnum.getDescByCode(serviceFeeDetailsVO.getBusinessCode()));
            }
        }
    }

    private void setLastMonthAllocationAmount(ServiceFeeDetailsNewVO serviceFeeDetailsVO) {
        List<ServiceFeeDetailsNewEntity> lastServiceFeeDetail = this.selectLastPeriodData(serviceFeeDetailsVO);
        serviceFeeDetailsVO.setAccrualType("");
        if (CollectionUtils.isNotEmpty(lastServiceFeeDetail)) {
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountNoTax(lastServiceFeeDetail.get(0).getShouldApportionmentAmountNoTax());
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountTaxInclude(lastServiceFeeDetail.get(0).getShouldApportionmentAmountTaxInclude());
            if (serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().compareTo(serviceFeeDetailsVO.getShouldApportionmentAmountTaxInclude()) != 0) {
                serviceFeeDetailsVO.setAccrualType(AccrualTypeEnum.ADJUST.getCode());
            }
        } else {
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountNoTax(BigDecimal.ZERO);
            serviceFeeDetailsVO.setLastMonthShouldApportionmentAmountTaxInclude(BigDecimal.ZERO);
            if (ObjectUtil.isNull(serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude()) || serviceFeeDetailsVO.getLastMonthShouldApportionmentAmountTaxInclude().compareTo(BigDecimal.ZERO) == 0) {
                serviceFeeDetailsVO.setAccrualType(AccrualTypeEnum.ADDITIONAL.getCode());
            }
        }
    }

}

