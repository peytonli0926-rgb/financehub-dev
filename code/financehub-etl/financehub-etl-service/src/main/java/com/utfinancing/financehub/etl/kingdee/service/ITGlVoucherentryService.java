package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TGlVoucherentryQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGlVoucherentryDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TGlVoucherentryVO;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherentryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TGlVoucherentry服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITGlVoucherentryService extends IService<TGlVoucherentryEntity> {


}
