package com.utfinancing.financehub.engine.verification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.model.dto.CheckPageQueryDTO;
import com.utfinancing.financehub.engine.verification.entity.VerificationDetailsEntity;
import com.utfinancing.financehub.engine.verification.mapper.VerificationDetailsMapper;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationCheckDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsExcelDTO;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationDetailsVO;
import com.utfinancing.financehub.engine.verification.service.IVerificationDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-12
 * @Description :  VerificationDetails服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VerificationDetailsServiceImpl extends ServiceImpl<VerificationDetailsMapper, VerificationDetailsEntity> implements IVerificationDetailsService {

    private final VerificationDetailsMapper verificationDetailsMapper;

    @Override
    public Long saveVerificationDetails(VerificationDetailsDTO dto) {
        VerificationDetailsEntity entity = BeanUtil.copyProperties(dto, VerificationDetailsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVerificationDetails(Long id, VerificationDetailsDTO dto) {
        VerificationDetailsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VerificationDetailsDTO getVerificationDetailsDTOById(Long id) {
        VerificationDetailsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VerificationDetailsDTO.class);
    }

    @Override
    public IPage<VerificationDetailsVO> selectPage(VerificationDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<VerificationDetailsEntity> queryWrapper = getQueryWrapper(queryDTO);
        IPage<VerificationDetailsEntity> entityIPage = verificationDetailsMapper.selectPage(new Page<VerificationDetailsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VerificationDetailsVO.class);
    }

    @Override
    public Boolean updateBatchByVerificationIdList(List<Long> idList) {
        QueryWrapper<VerificationDetailsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(VerificationDetailsEntity::getVerificationId, idList);
        queryWrapper.lambda().eq(VerificationDetailsEntity::getDelFlag,"0");
        List<VerificationDetailsEntity> verificationDetailsEntityList = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(verificationDetailsEntityList)) {
            return Boolean.TRUE;
        }
        return removeBatchByIds(verificationDetailsEntityList.stream().map(VerificationDetailsEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public Boolean deleteById(Long id) {
        return removeById(id);
    }

    @Override
    public List<VerificationDetailsVO> listByCondition(VerificationDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<VerificationDetailsEntity> queryWrapper = getQueryWrapper(queryDTO);
        List<VerificationDetailsEntity> verificationDetailsEntityList = list(queryWrapper);
        return ListBeanUtil.copyList(verificationDetailsEntityList, VerificationDetailsVO.class);
    }

    @Override
    public Long importDetailInfos(MultipartFile file, Long verificationId) {
        try {
            //只更新不新增
            ExcelUtil<VerificationDetailsExcelDTO> util = new ExcelUtil<VerificationDetailsExcelDTO>(VerificationDetailsExcelDTO.class);
            List<VerificationDetailsExcelDTO> verificationExcelDTOS = util.importExcel(file.getInputStream());
            if (CollectionUtil.isEmpty(verificationExcelDTOS)) {
                throw new ServiceException("没有数据需要更新");
            }
            Map<String, VerificationDetailsExcelDTO> excelDataMap = verificationExcelDTOS.stream().collect(Collectors.toMap(VerificationDetailsExcelDTO::getContractCode, Function.identity()));
            //获取核销详情数据
            VerificationDetailsQueryDTO queryDTO = new VerificationDetailsQueryDTO();
            queryDTO.setVerificationId(verificationId);
            List<VerificationDetailsVO> verificationDetailsVOList = this.listByCondition(queryDTO);
            List<VerificationDetailsEntity> verificationDetailsEntityList = Lists.newArrayList();
            //按照合同编码更新数据
            verificationDetailsVOList.stream().forEach(v -> {
                if (excelDataMap.containsKey(v.getContractCode())) {
                    VerificationDetailsEntity entity = new VerificationDetailsEntity();
                    BeanUtil.copyProperties(v, entity);
                    VerificationDetailsExcelDTO verificationDetailsExcelDTO = excelDataMap.get(v.getContractCode());
                    entity.setPayableAgencyEstimate(verificationDetailsExcelDTO.getPayableAgencyEstimate());
                    entity.setPayableBandCostEstimate(verificationDetailsExcelDTO.getPayableBandCostEstimate());
                    entity.setPayableDeviceEstimate(verificationDetailsExcelDTO.getPayableDeviceEstimate());
                    entity.setPayableOtherCostEstimate(verificationDetailsExcelDTO.getPayableOtherCostEstimate());
                    entity.setPayablePledgeEstimate(verificationDetailsExcelDTO.getPayablePledgeEstimate());
                    entity.setPayableUnpledgeEstimate(verificationDetailsExcelDTO.getPayableUnpledgeEstimate());
                    entity.setPayableVehicleEstimate(verificationDetailsExcelDTO.getPayableVehicleEstimate());
                    entity.setReceivableCommission(verificationDetailsExcelDTO.getReceivableCommission());
                    entity.setReceivableDownpayment(verificationDetailsExcelDTO.getReceivableDownpayment());
                    entity.setReceivableInsurance(verificationDetailsExcelDTO.getReceivableInsurance());
                    entity.setReceivableOtherincome(verificationDetailsExcelDTO.getReceivableOtherincome());
                    entity.setReceivableOuttax(verificationDetailsExcelDTO.getReceivableOuttax());
                    entity.setReceivableRent(verificationDetailsExcelDTO.getReceivableRent());
                    entity.setReceivableResidualValue(verificationDetailsExcelDTO.getReceivableResidualValue());
                    entity.setUnrealizedRevenue(verificationDetailsExcelDTO.getUnrealizedRevenue());
                    entity.setDepreciationReserves(verificationDetailsExcelDTO.getDepreciationReserves());
                    entity.setFinancialContractStatus(verificationDetailsExcelDTO.getFinancialContractStatus());
                    calculateFinancialExpenseAmount(entity);
                    verificationDetailsEntityList.add(entity);
                }
            });
            if (CollectionUtil.isNotEmpty(verificationDetailsEntityList)) {
                this.updateBatchById(verificationDetailsEntityList);
            }
        } catch (Exception exception) {
            throw new ServiceException("导入核销详情数据失败");
        }
        return verificationId;
    }

    public LambdaQueryWrapper<VerificationDetailsEntity> getQueryWrapper(VerificationDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<VerificationDetailsEntity> queryWrapper = new LambdaQueryWrapper<>();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(VerificationDetailsEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientName())) {
            queryWrapper.like(VerificationDetailsEntity::getClientName, queryDTO.getClientName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(VerificationDetailsEntity::getOrgId,queryDTO.getOrgId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getFinancialContractStatus())) {
            queryWrapper.eq(VerificationDetailsEntity::getFinancialContractStatus, queryDTO.getFinancialContractStatus());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(VerificationDetailsEntity::getId, queryDTO.getIdList());
        }
        if (CollectionUtil.isNotEmpty(queryDTO.getVerificationIdList())) {
            queryWrapper.in(VerificationDetailsEntity::getVerificationId, queryDTO.getVerificationIdList());
        }
        if (ObjectUtil.isNotNull(queryDTO.getVerificationId())) {
            queryWrapper.eq(VerificationDetailsEntity::getVerificationId, queryDTO.getVerificationId());
        }
        return queryWrapper;
    }

    public void calculateFinancialExpenseAmount (VerificationDetailsEntity verificationDetailsEntity) {
        //财务核销敞口 = （应收租金+应收期末残值+应收首付款+应收手续费+应收保险费+应收其他收入+应收肖项税）- 未实现收益- （应付设备款-暂估+应付收车费-暂估+应付手环成本_暂估+应付抵押费_暂估+应付解抵押费_暂估+应付其他租赁成本-暂估）
        //补提拨备 = 财务核销敞口-应收租赁款组合拨备
        BigDecimal financialExpenseAmount = BigDecimal.ZERO;
        BigDecimal compensationProvisionAmount = BigDecimal.ZERO;
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableRent())) {
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableRent());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableResidualValue())) {
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableResidualValue());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableDownpayment())){
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableDownpayment());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableCommission())) {
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableCommission());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableInsurance())) {
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableInsurance());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableOtherincome())) {
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableOtherincome());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getReceivableOuttax())) {
            financialExpenseAmount = financialExpenseAmount.add(verificationDetailsEntity.getReceivableOuttax());
        }
        //未实现收益
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getUnrealizedRevenue())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getUnrealizedRevenue());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayableDeviceEstimate())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayableDeviceEstimate());
        }

//        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayableAgencyEstimate())) {
//            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayableAgencyEstimate());
//        }

        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayableVehicleEstimate())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayableVehicleEstimate());
        }

        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayableBandCostEstimate())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayableBandCostEstimate());
        }

        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayablePledgeEstimate())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayablePledgeEstimate());
        }

        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayableUnpledgeEstimate())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayableUnpledgeEstimate());
        }
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getPayableOtherCostEstimate())) {
            financialExpenseAmount = financialExpenseAmount.subtract(verificationDetailsEntity.getPayableOtherCostEstimate());
        }
        compensationProvisionAmount = financialExpenseAmount;
        //应收租赁款组合拨备
        if (ObjectUtil.isNotEmpty(verificationDetailsEntity.getDepreciationReserves())) {
            compensationProvisionAmount = compensationProvisionAmount.subtract(verificationDetailsEntity.getDepreciationReserves());
        }
        verificationDetailsEntity.setFinancialExpenseAmount(financialExpenseAmount);
        verificationDetailsEntity.setCompensationProvisionAmount(compensationProvisionAmount);
    }

    @Override
    public IPage<VerificationCheckDTO> checkDataPage(CheckPageQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(),queryDTO.getPageSize());
        return verificationDetailsMapper.checkDataPage(page,queryDTO);
    }


}

