package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.EasExchangeRateQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.EasExchangeRateDTO;
import com.utfinancing.financehub.engine.finance.model.vo.EasExchangeRateVO;
import com.utfinancing.financehub.engine.finance.entity.EasExchangeRateEntity;
import com.utfinancing.financehub.engine.finance.mapper.EasExchangeRateMapper;
import com.utfinancing.financehub.engine.finance.service.IEasExchangeRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-16
 * @Description :  EasExchangeRate服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class EasExchangeRateServiceImpl extends ServiceImpl<EasExchangeRateMapper, EasExchangeRateEntity> implements IEasExchangeRateService {

    private final EasExchangeRateMapper easExchangeRateMapper;

    @Override
    public Long saveEasExchangeRate(EasExchangeRateDTO dto) {
        EasExchangeRateEntity entity = BeanUtil.copyProperties(dto, EasExchangeRateEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateEasExchangeRate(Long id, EasExchangeRateDTO dto) {
        EasExchangeRateEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public EasExchangeRateDTO getEasExchangeRateDTOById(Long id) {
        EasExchangeRateEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, EasExchangeRateDTO.class);
    }

    @Override
    public IPage<EasExchangeRateVO> selectPage(EasExchangeRateQueryDTO queryDTO) {
        LambdaQueryWrapper<EasExchangeRateEntity> queryWrapper = Wrappers.<EasExchangeRateEntity>lambdaQuery();
        // 这里注入查询条件
        IPage<EasExchangeRateEntity> entityIPage = easExchangeRateMapper.selectPage(new Page<EasExchangeRateEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, EasExchangeRateVO.class);
    }

    /**
     * 根据来源币种名称 和目标币种名称 查询汇率
     *
     * @param sourceCurrency
     * @param targetCurrency
     * @return
     */
    @Override
    public BigDecimal getRateBySourceNameAndTargetName(String sourceCurrency, String targetCurrency) {
        EasExchangeRateEntity easExchangeRateEntity = easExchangeRateMapper.selectOne(new LambdaQueryWrapper<EasExchangeRateEntity>()
                .eq(EasExchangeRateEntity::getSourceEasName, sourceCurrency)
                .eq(EasExchangeRateEntity::getTargetEasName, targetCurrency)
                .orderByDesc(EasExchangeRateEntity::getExecuteDate)
                .last("limit 1")
        );
        BigDecimal rate = BigDecimal.ONE;
        if (ObjectUtil.isNotEmpty(easExchangeRateEntity)) {
            rate = easExchangeRateEntity.getExchangeRate();
        }
        return rate;
    }

}

