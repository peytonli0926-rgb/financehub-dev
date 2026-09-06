package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.entity.AccountPeriodEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.scene.model.dto.AccountPeriodDTO;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-28
 * @Description : AccountPeriod服务类接口
 * @Modified :
 */
public interface IAccountPeriodService extends IService<AccountPeriodEntity> {

    List<AccountPeriodDTO> queryAllAccountPeriod();
}
