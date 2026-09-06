package com.utfinancing.financehub.etl.invoicing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceBackMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceBackMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicOiInvoiceBackMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicOiInvoiceBackMiddleEntity;
import com.utfinancing.financehub.etl.invoicing.mapper.TaxicOiInvoiceBackMiddleMapper;
import com.utfinancing.financehub.etl.invoicing.service.ITaxicOiInvoiceBackMiddleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :  TaxicOiInvoiceBackMiddle服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaxicOiInvoiceBackMiddleServiceImpl extends ServiceImpl<TaxicOiInvoiceBackMiddleMapper, TaxicOiInvoiceBackMiddleEntity> implements ITaxicOiInvoiceBackMiddleService {

    private final TaxicOiInvoiceBackMiddleMapper taxicOiInvoiceBackMiddleMapper;

    @Override
    public Long saveTaxicOiInvoiceBackMiddle(TaxicOiInvoiceBackMiddleDTO dto) {
        TaxicOiInvoiceBackMiddleEntity entity = BeanUtil.copyProperties(dto, TaxicOiInvoiceBackMiddleEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaxicOiInvoiceBackMiddle(Long id, TaxicOiInvoiceBackMiddleDTO dto) {
        TaxicOiInvoiceBackMiddleEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaxicOiInvoiceBackMiddleDTO getTaxicOiInvoiceBackMiddleDTOById(Long id) {
        TaxicOiInvoiceBackMiddleEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaxicOiInvoiceBackMiddleDTO.class);
    }

    @Override
    public IPage<TaxicOiInvoiceBackMiddleVO> selectPage(TaxicOiInvoiceBackMiddleQueryDTO queryDTO) {
        LambdaQueryWrapper<TaxicOiInvoiceBackMiddleEntity> queryWrapper = Wrappers.<TaxicOiInvoiceBackMiddleEntity>lambdaQuery();
        //这里注入查询条件
        IPage<TaxicOiInvoiceBackMiddleEntity> entityIPage = taxicOiInvoiceBackMiddleMapper.selectPage(new Page<TaxicOiInvoiceBackMiddleEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TaxicOiInvoiceBackMiddleVO.class);
    }

}

