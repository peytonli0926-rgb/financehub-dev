package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.CurrencyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CurrencyDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CurrencyVO;
import com.utfinancing.financehub.engine.finance.entity.CurrencyEntity;
import com.utfinancing.financehub.engine.finance.mapper.CurrencyMapper;
import com.utfinancing.financehub.engine.finance.service.ICurrencyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-01-11
 * @Description :  Currency服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, CurrencyEntity> implements ICurrencyService {

    private final CurrencyMapper currencyMapper;

    @Override
    public Long saveCurrency(CurrencyDTO dto) {
        CurrencyEntity entity = BeanUtil.copyProperties(dto, CurrencyEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCurrency(Long id, CurrencyDTO dto) {
        CurrencyEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CurrencyDTO getCurrencyDTOById(Long id) {
        CurrencyEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CurrencyDTO.class);
    }

    @Override
    public IPage<CurrencyVO> selectPage(CurrencyQueryDTO queryDTO) {
        LambdaQueryWrapper<CurrencyEntity> queryWrapper = Wrappers.<CurrencyEntity>lambdaQuery();
        //这里注入查询条件
        IPage<CurrencyEntity> entityIPage = currencyMapper.selectPage(new Page<CurrencyEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, CurrencyVO.class);
    }

}

