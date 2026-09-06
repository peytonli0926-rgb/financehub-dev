package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAsstaccountQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAsstaccountDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAsstaccountVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAsstaccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdAsstaccount服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdAsstaccountService extends IService<TBdAsstaccountEntity> {


}
