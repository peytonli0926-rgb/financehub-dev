package com.utfinancing.financehub.etl.invoicing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceBackMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceBackMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TyOiInvoiceBackMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TyOiInvoiceBackMiddleEntity;
import com.utfinancing.financehub.etl.invoicing.mapper.TyOiInvoiceBackMiddleMapper;
import com.utfinancing.financehub.etl.invoicing.service.ITyOiInvoiceBackMiddleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :  TyOiInvoiceBackMiddle服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TyOiInvoiceBackMiddleServiceImpl extends ServiceImpl<TyOiInvoiceBackMiddleMapper, TyOiInvoiceBackMiddleEntity> implements ITyOiInvoiceBackMiddleService {

    private final TyOiInvoiceBackMiddleMapper tyOiInvoiceBackMiddleMapper;

    @Override
    public Long saveTyOiInvoiceBackMiddle(TyOiInvoiceBackMiddleDTO dto) {
        TyOiInvoiceBackMiddleEntity entity = BeanUtil.copyProperties(dto, TyOiInvoiceBackMiddleEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTyOiInvoiceBackMiddle(Long id, TyOiInvoiceBackMiddleDTO dto) {
        TyOiInvoiceBackMiddleEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TyOiInvoiceBackMiddleDTO getTyOiInvoiceBackMiddleDTOById(Long id) {
        TyOiInvoiceBackMiddleEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TyOiInvoiceBackMiddleDTO.class);
    }

    @Override
    public IPage<TyOiInvoiceBackMiddleVO> selectPage(TyOiInvoiceBackMiddleQueryDTO queryDTO) {
        LambdaQueryWrapper<TyOiInvoiceBackMiddleEntity> queryWrapper = Wrappers.<TyOiInvoiceBackMiddleEntity>lambdaQuery();
        //这里注入查询条件
        IPage<TyOiInvoiceBackMiddleEntity> entityIPage = tyOiInvoiceBackMiddleMapper.selectPage(new Page<TyOiInvoiceBackMiddleEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TyOiInvoiceBackMiddleVO.class);
    }

}

