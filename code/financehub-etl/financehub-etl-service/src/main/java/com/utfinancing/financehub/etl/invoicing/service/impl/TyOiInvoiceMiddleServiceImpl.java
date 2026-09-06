package com.utfinancing.financehub.etl.invoicing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceMiddleQueryDTO;
import com.utfinancing.financehub.etl.invoicing.model.dto.TyOiInvoiceMiddleDTO;
import com.utfinancing.financehub.etl.invoicing.model.vo.TyOiInvoiceMiddleVO;
import com.utfinancing.financehub.etl.invoicing.entity.TyOiInvoiceMiddleEntity;
import com.utfinancing.financehub.etl.invoicing.mapper.TyOiInvoiceMiddleMapper;
import com.utfinancing.financehub.etl.invoicing.service.ITyOiInvoiceMiddleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :  TyOiInvoiceMiddle服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TyOiInvoiceMiddleServiceImpl extends ServiceImpl<TyOiInvoiceMiddleMapper, TyOiInvoiceMiddleEntity> implements ITyOiInvoiceMiddleService {

    private final TyOiInvoiceMiddleMapper tyOiInvoiceMiddleMapper;

    @Override
    public Long saveTyOiInvoiceMiddle(TyOiInvoiceMiddleDTO dto) {
        TyOiInvoiceMiddleEntity entity = BeanUtil.copyProperties(dto, TyOiInvoiceMiddleEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTyOiInvoiceMiddle(Long id, TyOiInvoiceMiddleDTO dto) {
        TyOiInvoiceMiddleEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TyOiInvoiceMiddleDTO getTyOiInvoiceMiddleDTOById(Long id) {
        TyOiInvoiceMiddleEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TyOiInvoiceMiddleDTO.class);
    }

    @Override
    public IPage<TyOiInvoiceMiddleVO> selectPage(TyOiInvoiceMiddleQueryDTO queryDTO) {
        LambdaQueryWrapper<TyOiInvoiceMiddleEntity> queryWrapper = Wrappers.<TyOiInvoiceMiddleEntity>lambdaQuery();
        //这里注入查询条件
        IPage<TyOiInvoiceMiddleEntity> entityIPage = tyOiInvoiceMiddleMapper.selectPage(new Page<TyOiInvoiceMiddleEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TyOiInvoiceMiddleVO.class);
    }

    @Override
    public List<TyOiInvoiceMiddleVO> selectByCondition(TyOiInvoiceMiddleQueryDTO queryDTO) {
        return tyOiInvoiceMiddleMapper.selectByCondition(queryDTO);
    }


}

