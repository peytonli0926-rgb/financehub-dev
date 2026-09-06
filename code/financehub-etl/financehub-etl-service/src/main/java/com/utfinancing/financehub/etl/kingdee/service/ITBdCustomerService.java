package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.entity.TBdCustomerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-19
 * @Description : TBdCustomer服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdCustomerService extends IService<TBdCustomerEntity> {


}
