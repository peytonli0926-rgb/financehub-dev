package com.utfinancing.financehub.etl.invoicing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.vo.InvoiceClaimVO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TaxicOiInvoiceMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TaxicOiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TaxicOiInvoiceMiddleEntity;
import com.utfinancing.financehub.etl.invoicing.mapper.TaxicOiInvoiceMiddleMapper;
import com.utfinancing.financehub.etl.invoicing.service.ITaxicOiInvoiceMiddleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :  TaxicOiInvoiceMiddle服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaxicOiInvoiceMiddleServiceImpl extends ServiceImpl<TaxicOiInvoiceMiddleMapper, TaxicOiInvoiceMiddleEntity> implements ITaxicOiInvoiceMiddleService {

    private final TaxicOiInvoiceMiddleMapper taxicOiInvoiceMiddleMapper;

    @Override
    public Long saveTaxicOiInvoiceMiddle(TaxicOiInvoiceMiddleDTO dto) {
        TaxicOiInvoiceMiddleEntity entity = BeanUtil.copyProperties(dto, TaxicOiInvoiceMiddleEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaxicOiInvoiceMiddle(Long id, TaxicOiInvoiceMiddleDTO dto) {
        TaxicOiInvoiceMiddleEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaxicOiInvoiceMiddleDTO getTaxicOiInvoiceMiddleDTOById(Long id) {
        TaxicOiInvoiceMiddleEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaxicOiInvoiceMiddleDTO.class);
    }

    @Override
    public IPage<TaxicOiInvoiceMiddleVO> selectPage(TaxicOiInvoiceMiddleQueryDTO queryDTO) {
        LambdaQueryWrapper<TaxicOiInvoiceMiddleEntity> queryWrapper = Wrappers.<TaxicOiInvoiceMiddleEntity>lambdaQuery();
        //这里注入查询条件
        IPage<TaxicOiInvoiceMiddleEntity> entityIPage = taxicOiInvoiceMiddleMapper.selectPage(new Page<TaxicOiInvoiceMiddleEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TaxicOiInvoiceMiddleVO.class);
    }

    @Override
    public List<InvoiceClaimVO> selectByCondition(TaxicOiInvoiceMiddleQueryDTO queryDTO) {
        return taxicOiInvoiceMiddleMapper.selectByCondition(queryDTO);
    }


}

