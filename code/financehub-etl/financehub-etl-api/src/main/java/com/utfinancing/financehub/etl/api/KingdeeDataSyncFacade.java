package com.utfinancing.financehub.etl.api;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.model.dto.TGLVoucherInitDTO;
import com.utfinancing.financehub.etl.model.dto.VoucherAmountSyncDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(
        value = "financehub-etl-service",
        contextId = "kingdeeDataSyncFacade")
public interface KingdeeDataSyncFacade {

    @PostMapping("/financial/kingdee/voucherAmountSync")
    R<List<TGLVoucherInitDTO>> voucherAmountSync(TGLVoucherInitDTO params);

}
