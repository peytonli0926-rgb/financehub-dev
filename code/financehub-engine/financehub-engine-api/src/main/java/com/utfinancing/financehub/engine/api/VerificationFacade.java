package com.utfinancing.financehub.engine.api;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.fallback.VerificationFacadeFallBack;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.api.VerificationFacade</li>
 * <li>CreateTime : 2023/11/21 15:50</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@FeignClient(
        value = "financehub-engine-service",
        contextId = "verificationFacade",
        fallbackFactory = VerificationFacadeFallBack.class)
public interface VerificationFacade {

    @PostMapping("/verification/updateContractAmount")
    @ApiOperation(value = "开票认领更新合同转回拨备金额")
    R<Boolean> updateContractAmount(@RequestBody List<CourtCostVerificationDTO> dtoList);
}
