package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.EasBdCustomerEntity;
import com.utfinancing.financehub.etl.financial.mapper.EasBdCustomerMapper;
import com.utfinancing.financehub.etl.financial.service.IEasBdCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2023-12-19
 * @Description :  EasBdCustomer服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class EasBdCustomerServiceImpl extends ServiceImpl<EasBdCustomerMapper, EasBdCustomerEntity> implements IEasBdCustomerService {

    private final EasBdCustomerMapper easBdCustomerMapper;

}

