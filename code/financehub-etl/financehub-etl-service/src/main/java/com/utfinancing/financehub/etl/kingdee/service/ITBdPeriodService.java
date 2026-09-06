package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TBdPeriodQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdPeriodDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdPeriodVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdPeriodEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdPeriod服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdPeriodService extends IService<TBdPeriodEntity> {


}
