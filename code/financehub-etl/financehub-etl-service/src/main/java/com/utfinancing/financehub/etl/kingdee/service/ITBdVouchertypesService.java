package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.VoucherTypeDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdVouchertypesQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdVouchertypesDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdVouchertypesVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdVouchertypesEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TBdVouchertypes服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITBdVouchertypesService extends IService<TBdVouchertypesEntity> {

    List<VoucherTypeDTO> selectAllVoucherTypes();

}
