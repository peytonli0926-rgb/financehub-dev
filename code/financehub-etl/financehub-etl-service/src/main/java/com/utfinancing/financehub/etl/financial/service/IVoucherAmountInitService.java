package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.entity.VoucherAmountInitEntity;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGLVoucherInitDTO;

import java.util.List;


/**
 * @Author : robjiang
 * @Date : Create in 2024-01-10
 * @Description : VoucherAmountInit服务类接口
 * @Modified :
 */
public interface IVoucherAmountInitService extends IService<VoucherAmountInitEntity> {

    public R<List<TGLVoucherInitDTO>> voucherAmountInit(TGLVoucherInitDTO dto);

}
