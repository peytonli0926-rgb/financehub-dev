package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;

public interface IServiceFeeMeasurementService {

    public R measurement(ServiceFeeQueryDTO queryDTO, Long taskId);

}
