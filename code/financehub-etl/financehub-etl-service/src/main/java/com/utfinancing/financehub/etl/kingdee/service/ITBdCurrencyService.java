package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TBdCurrencyQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdCurrencyDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdCurrencyVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdCurrencyEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdCurrency服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdCurrencyService extends IService<TBdCurrencyEntity> {


}
