package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.kingdee.entity.TBdCustomerEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdCustomerMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdCustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-19
 * @Description :  TBdCustomer服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TBdCustomerServiceImpl extends ServiceImpl<TBdCustomerMapper, TBdCustomerEntity> implements ITBdCustomerService {

    private final TBdCustomerMapper tBdCustomerMapper;


}

