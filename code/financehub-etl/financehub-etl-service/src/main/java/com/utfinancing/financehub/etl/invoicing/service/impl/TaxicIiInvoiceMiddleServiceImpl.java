package com.utfinancing.financehub.etl.invoicing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicIiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicIiInvoiceMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicIiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicIiInvoiceMiddleEntity;
import com.utfinancing.financehub.etl.invoicing.mapper.TaxicIiInvoiceMiddleMapper;
import com.utfinancing.financehub.etl.invoicing.service.ITaxicIiInvoiceMiddleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :  TaxicIiInvoiceMiddle服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaxicIiInvoiceMiddleServiceImpl extends ServiceImpl<TaxicIiInvoiceMiddleMapper, TaxicIiInvoiceMiddleEntity> implements ITaxicIiInvoiceMiddleService {

    private final TaxicIiInvoiceMiddleMapper taxicIiInvoiceMiddleMapper;

    @Override
    public Long saveTaxicIiInvoiceMiddle(TaxicIiInvoiceMiddleDTO dto) {
        TaxicIiInvoiceMiddleEntity entity = BeanUtil.copyProperties(dto, TaxicIiInvoiceMiddleEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaxicIiInvoiceMiddle(Long id, TaxicIiInvoiceMiddleDTO dto) {
        TaxicIiInvoiceMiddleEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaxicIiInvoiceMiddleDTO getTaxicIiInvoiceMiddleDTOById(Long id) {
        TaxicIiInvoiceMiddleEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaxicIiInvoiceMiddleDTO.class);
    }

    @Override
    public IPage<TaxicIiInvoiceMiddleVO> selectPage(TaxicIiInvoiceMiddleQueryDTO queryDTO) {
        LambdaQueryWrapper<TaxicIiInvoiceMiddleEntity> queryWrapper = Wrappers.<TaxicIiInvoiceMiddleEntity>lambdaQuery();
        //这里注入查询条件
        IPage<TaxicIiInvoiceMiddleEntity> entityIPage = taxicIiInvoiceMiddleMapper.selectPage(new Page<TaxicIiInvoiceMiddleEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TaxicIiInvoiceMiddleVO.class);
    }

}

