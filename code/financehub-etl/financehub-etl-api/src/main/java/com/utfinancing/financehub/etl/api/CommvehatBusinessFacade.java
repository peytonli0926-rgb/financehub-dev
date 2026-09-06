package com.utfinancing.financehub.etl.api;

import com.utfinancing.financehub.common.core.constant.ServiceNameConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.model.dto.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.model.dto.SelectReceiveRepaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        value = ServiceNameConstants.ETL_SERVICE,
        contextId = "commvehatBusinessFacade")
public interface CommvehatBusinessFacade {

    @PostMapping("/commvehat/queryRepaymentPlan")
    R<List<RepaymentPlanSaveDTO>> queryRepaymentPlan(QueryRepaymentPlanDTO params);

    @PostMapping("/commvehat/queryReceivedRepaymentPlan")
    R<List<SelectReceiveRepaymentDTO>> queryReceivedRepaymentPlan(QueryRepaymentPlanDTO params);
}
