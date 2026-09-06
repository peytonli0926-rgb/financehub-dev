package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAsstaccountEntity;
import com.utfinancing.financehub.etl.kingdee.entity.TGLAssistBalanceEntity;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGLVoucherInitDTO;

import java.util.List;

@DS("slave_kingdee")
public interface ITGLAssistBalanceService extends IService<TGLAssistBalanceEntity> {
    /**
     * 未偿还金额列表查询
     */
    public List<TGLAssistBalanceEntity> outstandingAmountQuery(String period);

    public List<TGLVoucherInitDTO> voucherInitQuery(TGLVoucherInitDTO dto);
}
