package com.utfinancing.financehub.etl.api;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.model.dto.ContractQueryInfoDTO;
import com.utfinancing.financehub.etl.model.vo.ContractInvoiceClaimVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(
        value = "financehub-etl-service",
        contextId = "InvoiceClaimFacade")
public interface InvoiceClaimFacade {

    @PostMapping("/financial/invoice-claim/selectContractInvoiceList")
    R<List<ContractInvoiceClaimVO>> selectContractInvoiceList(ContractQueryInfoDTO queryDTO);

}
