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
        contextId = "passvehBusinessFacade")
public interface PassvehBusinessFacade {

    @PostMapping("/passveh/queryRepaymentPlan")
    R<List<RepaymentPlanSaveDTO>> queryRepaymentPlan(QueryRepaymentPlanDTO params);

    @PostMapping("/passveh/queryReceivedRepaymentPlan")
    R<List<SelectReceiveRepaymentDTO>> queryReceivedRepaymentPlan(QueryRepaymentPlanDTO params);
}
