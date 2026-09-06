package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.enums.EnableFlagEnum;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.TaxRateVO;
import com.utfinancing.financehub.engine.scene.entity.TaxRateEntity;
import com.utfinancing.financehub.engine.scene.mapper.TaxRateMapper;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-18
 * @Description :  TaxRate服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaxRateServiceImpl extends ServiceImpl<TaxRateMapper, TaxRateEntity> implements ITaxRateService {

    private final TaxRateMapper taxRateMapper;
    private final IBusinessService businessService;
    private final RemoteDictService remoteDictService;
    private final RedisService redisService;

    @Override
    public Long saveTaxRate(TaxRateSaveDTO dto) {
        LambdaQueryWrapper<TaxRateEntity> queryWrapper = Wrappers.<TaxRateEntity>lambdaQuery();
        queryWrapper.eq(TaxRateEntity::getBusinessCode, dto.getBusinessCode());
        queryWrapper.eq(TaxRateEntity::getFundType, dto.getFundType());
        queryWrapper.eq(TaxRateEntity::getLeaseType, dto.getLeaseType());
        if (StrUtil.isBlank(dto.getLeaseSubType())){
            queryWrapper.isNull(TaxRateEntity::getLeaseSubType);
        } else {
            queryWrapper.eq(TaxRateEntity::getLeaseSubType, dto.getLeaseSubType());
        }
        if (ObjectUtil.isNull(dto.getEnableDate())){
            queryWrapper.isNull(TaxRateEntity::getEnableDate);
        } else {
            queryWrapper.eq(TaxRateEntity::getEnableDate, dto.getEnableDate());
        }
        long count = this.count(queryWrapper);
        if (count > 0){
            throw new ServiceException("税率配置重复");
        }
        TaxRateEntity entity = BeanUtil.copyProperties(dto, TaxRateEntity.class);
        entity.setEnableFlag(EnableFlagEnum.ENABLE.getCode());
        this.save(entity);
        redisService.deleteObject(RedisConstant.V_ALL_TAX_RATE);
        return entity.getId();
    }

    @Override
    public Long updateTaxRate(Long id, TaxRateSaveDTO dto) {
        TaxRateEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        redisService.deleteObject(RedisConstant.V_ALL_TAX_RATE);
        return id;
    }

    @Override
    public TaxRateDTO getTaxRateDTOById(Long id) {
        TaxRateEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaxRateDTO.class);
    }

    @Override
    public IPage<TaxRateVO> selectPage(TaxRateQueryDTO queryDTO) {
        LambdaQueryWrapper<TaxRateEntity> queryWrapper = Wrappers.<TaxRateEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getBusinessCode()), TaxRateEntity::getBusinessCode, queryDTO.getBusinessCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getFundType()), TaxRateEntity::getFundType, queryDTO.getFundType());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getLeaseType()), TaxRateEntity::getLeaseType, queryDTO.getLeaseType());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getLeaseSubType()), TaxRateEntity::getLeaseSubType, queryDTO.getLeaseSubType());
        queryWrapper.orderByDesc(TaxRateEntity::getCreateTime);
        IPage<TaxRateEntity> entityIPage = taxRateMapper.selectPage(new Page<TaxRateEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<TaxRateVO> voPage = ListBeanUtil.copyPage(entityIPage, TaxRateVO.class);
        //填充业务名称
        if (CollectionUtil.isNotEmpty(voPage.getRecords())){
            List<TaxRateVO> records = voPage.getRecords();
            Map<String, BusinessDTO> businessDTOMap = businessService.getMapByCode(records.stream().map(TaxRateVO::getBusinessCode).collect(Collectors.toList()));
            records.forEach(e->{
                BusinessDTO businessDTO = businessDTOMap.get(e.getBusinessCode());
                if (businessDTO != null){
                    e.setBusinessName(businessDTO.getBusinessName());
                }
            });
        }
        return voPage;
    }

    @Override
    public BigDecimal getValidTaxRateByCode(String businessCode, String fundType) {
        TaxRateEntity entity = this.getOne(Wrappers.<TaxRateEntity>lambdaQuery()
                .eq(TaxRateEntity::getBusinessCode, businessCode)
                .eq(TaxRateEntity::getFundType, fundType)
                .eq(TaxRateEntity::getEnableFlag, EnableFlagEnum.ENABLE.getCode())
                .le(TaxRateEntity::getEnableDate, LocalDate.now())
                .orderByDesc(TaxRateEntity::getEnableDate), false);
        if (entity != null){
            return NumberUtil.div(entity.getTaxRate(), 100);
        }
        return null;
    }

    @Override
    public BigDecimal getGeneralValidTaxRateByLeaseType(String businessCode, String leaseType) {
        TaxRateEntity entity = this.getOne(Wrappers.<TaxRateEntity>lambdaQuery()
                .eq(TaxRateEntity::getBusinessCode, businessCode)
                .eq(TaxRateEntity::getFundType, "tax_general")
                .eq(TaxRateEntity::getLeaseType, leaseType)
                .eq(TaxRateEntity::getEnableFlag, EnableFlagEnum.ENABLE.getCode())
                .le(TaxRateEntity::getEnableDate, LocalDate.now())
                .orderByDesc(TaxRateEntity::getEnableDate), false);
        if (entity != null){
            return NumberUtil.div(entity.getTaxRate(), 100);
        }
        return null;
    }

    @Override
    public List<TaxRateDTO> queryAllForEditor() {
        List<TaxRateEntity> entityList = this.list(Wrappers.<TaxRateEntity>lambdaQuery()
                .eq(TaxRateEntity::getEnableFlag, EnableFlagEnum.ENABLE.getCode())
                .le(TaxRateEntity::getEnableDate, LocalDate.now())
                .orderByDesc(TaxRateEntity::getBusinessCode, TaxRateEntity::getFundType, TaxRateEntity::getLeaseType, TaxRateEntity::getLeaseSubType));
        //业务编码
        List<BusinessDTO> businessDTOList = businessService.queryAll();
        Map<String, String> businessMap = businessDTOList.stream().collect(Collectors.toMap(e->e.getBusinessCode(), e->e.getBusinessName()));
        //金额类型
        R<List<SysDictData>> fundTypeR = remoteDictService.listDictData(DictTypeEnum.CASH_TYPE.getCode());
        Map<String, String> fundTypeMap = fundTypeR.getData().stream().collect(Collectors.toMap(e->e.getDictValue(), e->e.getDictLabel()));
        //租赁类型
        R<List<SysDictData>> leaseTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_TYPE.getCode());
        Map<String, String> leaseTypeMap = leaseTypeR.getData().stream().collect(Collectors.toMap(e->e.getDictValue(), e->e.getDictLabel()));
        //租赁细类
        // 华夏金租税率按“租赁类型 + 租赁方式”配置，leaseSubType 字段承载租赁方式。
        R<List<SysDictData>> leaseSubTypeR = remoteDictService.listDictData(DictTypeEnum.LEASE_METHOD.getCode());
        Map<String, String> leaseSubTypeMap = leaseSubTypeR.getData().stream().collect(Collectors.toMap(e->e.getDictValue(), e->e.getDictLabel()));
        List<TaxRateDTO> taxRateDTOList = ListBeanUtil.copyList(entityList, TaxRateDTO.class);
        taxRateDTOList.forEach(e->{
            e.setBusinessCode(businessMap.get(e.getBusinessCode()));
            e.setFundType(fundTypeMap.get(e.getFundType()));
            e.setLeaseType(leaseTypeMap.get(e.getLeaseType()));
            e.setLeaseSubType(leaseSubTypeMap.get(e.getLeaseSubType()));
            e.setTaxRate(NumberUtil.div(e.getTaxRate(), 100));
        });
        return taxRateDTOList;
    }

}

