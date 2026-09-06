package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeeDetailsEntity;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeeDetailsMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AccountBalanceSheetExcelExportDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeePlanDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeAllocationExcelVo;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeeMonthlyData;
import com.utfinancing.financehub.engine.finance.model.vo.ServiceFeePlanVO;
import com.utfinancing.financehub.engine.finance.entity.ServiceFeePlanEntity;
import com.utfinancing.financehub.engine.finance.mapper.ServiceFeePlanMapper;
import com.utfinancing.financehub.engine.finance.service.IServiceFeeDetailsService;
import com.utfinancing.financehub.engine.finance.service.IServiceFeePlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-29
 * @Description :  ServiceFeePlan服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ServiceFeePlanServiceImpl extends ServiceImpl<ServiceFeePlanMapper, ServiceFeePlanEntity> implements IServiceFeePlanService {

    private final ServiceFeePlanMapper serviceFeePlanMapper;
    @Autowired
    private final IServiceFeeDetailsService serviceFeeDetailsService;

    @Override
    public Long saveServiceFeePlan(ServiceFeePlanDTO dto) {
        ServiceFeePlanEntity entity = BeanUtil.copyProperties(dto, ServiceFeePlanEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateServiceFeePlan(Long id, ServiceFeePlanDTO dto) {
        ServiceFeePlanEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ServiceFeePlanDTO getServiceFeePlanDTOById(Long id) {
        ServiceFeePlanEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ServiceFeePlanDTO.class);
    }

    @Override
    public IPage<ServiceFeePlanVO> selectPage(ServiceFeePlanQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeePlanEntity> queryWrapper = Wrappers.<ServiceFeePlanEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ServiceFeePlanEntity> entityIPage = serviceFeePlanMapper.selectPage(new Page<ServiceFeePlanEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ServiceFeePlanVO.class);
    }

    @Override
    public List<ServiceFeePlanEntity> selectByContractCode(String contractCode) {
        return list(new LambdaQueryWrapper<ServiceFeePlanEntity>().eq(ServiceFeePlanEntity::getContractCode, contractCode));
    }

    @Override
    public void physicalDeleteByContractCode(String contractCode) {
        baseMapper.physicalDeleteByContractCode(contractCode);
    }

    @Override
    public List<ServiceFeePlanVO> export(ServiceFeePlanQueryDTO queryDTO) {
        return serviceFeePlanMapper.getExportData(queryDTO.getContractCode());
    }

    @Override
    public List<ServiceFeeAllocationExcelVo> getAllAllocationsPlanList(ServiceFeeQueryDTO queryDTO) {
        LambdaQueryWrapper<ServiceFeeDetailsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceFeeDetailsEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.apply("TO_CHAR(business_date, 'YYYY-MM') = {0}", DateUtil.format(queryDTO.getBusinessDate(), "yyyy-MM"));
        List<ServiceFeeDetailsEntity> list = serviceFeeDetailsService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<String> contractCodes = list.stream().map(ServiceFeeDetailsEntity::getContractCode).collect(Collectors.toList());
        LambdaQueryWrapper<ServiceFeePlanEntity> planQueryWrapper = new LambdaQueryWrapper<>();
        planQueryWrapper.in(ServiceFeePlanEntity::getContractCode, contractCodes);
        planQueryWrapper.eq(ServiceFeePlanEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<ServiceFeeAllocationExcelVo> serviceFeePlanEntities = serviceFeePlanMapper.selectServiceFeeAllocationExcelList(contractCodes);
        Map<String, List<ServiceFeeAllocationExcelVo>> planGroupByServiceNo = serviceFeePlanEntities.stream().collect(Collectors.groupingBy(ServiceFeeAllocationExcelVo::getServiceFeeNo));
        List<ServiceFeeAllocationExcelVo> results = new ArrayList<>();
        for (ServiceFeeDetailsEntity serviceFeeDetailsEntity : list) {
            ServiceFeeAllocationExcelVo excelVo = new ServiceFeeAllocationExcelVo();
            BeanUtil.copyProperties(serviceFeeDetailsEntity, excelVo);
            excelVo.setLesseeName(serviceFeeDetailsEntity.getClientName());
            excelVo.setPlanApportionAmount(serviceFeeDetailsEntity.getServiceFeeAllocationTaxIncluded());
            excelVo.setPlanApportionNoTax(serviceFeeDetailsEntity.getServiceFeeAllocationNoTax());
            excelVo.setActualReceive(serviceFeeDetailsEntity.getServiceFeeReceivedTaxIncluded());
            excelVo.setActualReceiveNoTax(serviceFeeDetailsEntity.getServiceFeeReceived());
            excelVo.setActualAccruedAmount(serviceFeeDetailsEntity.getAccumulatedAccruedAmount());
            List<ServiceFeeAllocationExcelVo> serviceFeePlanEntities1 = planGroupByServiceNo.get(serviceFeeDetailsEntity.getServiceFeeNo());
            if (CollectionUtils.isNotEmpty(serviceFeePlanEntities1)) {
                excelVo.setPayableDeviceAmount(serviceFeePlanEntities1.get(0).getPayableDeviceAmount());
                excelVo.setServiceFeeRatio(serviceFeePlanEntities1.get(0).getServiceFeeRatio());
                if (excelVo.getPayableDeviceAmount()!=null && excelVo.getPayableDeviceAmount().compareTo(BigDecimal.ZERO) != 0)
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
//                for (ServiceFeeAllocationExcelVo serviceFeePlanEntity : serviceFeePlanEntities1) {
//                    ServiceFeeMonthlyData monthlyData = new ServiceFeeMonthlyData();
//                    monthlyData.setMonth("计提数" + DateUtil.format(serviceFeePlanEntity.getPlanDate(), "yyyy-MM"));
//                    monthlyData.setAmount(serviceFeePlanEntity.getActualAccruedAmount());
//                    monthlyDatas.add(monthlyData);
//                }
                excelVo.setMonthlyDatas(monthlyDatas);
            }
            results.add(excelVo);
        }
        return results;
    }

}

