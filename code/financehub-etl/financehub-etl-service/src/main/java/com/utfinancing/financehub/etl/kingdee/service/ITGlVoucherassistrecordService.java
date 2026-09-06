package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TGlVoucherassistrecordQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGlVoucherassistrecordDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TGlVoucherassistrecordVO;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherassistrecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TGlVoucherassistrecord服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITGlVoucherassistrecordService extends IService<TGlVoucherassistrecordEntity> {


}
