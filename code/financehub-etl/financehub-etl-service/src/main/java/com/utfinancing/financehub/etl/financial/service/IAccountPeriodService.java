package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.AccountPeriodQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.AccountPeriodDTO;
import com.utfinancing.financehub.etl.financial.model.vo.AccountPeriodVO;
import com.utfinancing.financehub.etl.financial.entity.AccountPeriodEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : AccountPeriod服务类接口
 * @Modified :
 */
@DS("master")
public interface IAccountPeriodService extends IService<AccountPeriodEntity> {


}
