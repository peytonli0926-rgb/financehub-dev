package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.model.dto.ServiceFeeQueryDTO;
import org.springframework.scheduling.annotation.Async;

public interface IServiceFeeAsyncService {

    public R measurementAsync(ServiceFeeQueryDTO queryDTO);

}
