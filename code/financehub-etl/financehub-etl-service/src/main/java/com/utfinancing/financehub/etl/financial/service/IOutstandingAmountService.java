package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.entity.OutstandingAmountInitEntity;


public interface IOutstandingAmountService extends IService<OutstandingAmountInitEntity> {

    public R<Boolean> outstandingAmountInit(String period);

}
