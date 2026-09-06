package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.EasExchangeRateEntity;
import com.utfinancing.financehub.etl.financial.mapper.EasExchangeRateMapper;
import com.utfinancing.financehub.etl.financial.service.IEasExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : jnc
 * @Date : Create in 2024-04-15
 * @Description :  EasExchangeRate服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class EasExchangeRateServiceImpl extends ServiceImpl<EasExchangeRateMapper, EasExchangeRateEntity> implements IEasExchangeRateService {

    private final EasExchangeRateMapper easExchangeRateMapper;

}

