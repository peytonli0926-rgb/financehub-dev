package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.CurrencyQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.CurrencyDTO;
import com.utfinancing.financehub.etl.financial.model.vo.CurrencyVO;
import com.utfinancing.financehub.etl.financial.entity.CurrencyEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : Currency服务类接口
 * @Modified :
 */
@DS("master")
public interface ICurrencyService extends IService<CurrencyEntity> {


}
