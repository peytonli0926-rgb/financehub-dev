package com.utfinancing.financehub.engine.fallback;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.api.VerificationFacade;
import com.utfinancing.financehub.engine.model.dto.CourtCostVerificationDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.fallback.VerificationFacadeFallBack</li>
 * <li>CreateTime : 2023/11/21 15:54</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Component
public class VerificationFacadeFallBack implements FallbackFactory<VerificationFacade> {
    @Override
    public VerificationFacade create(Throwable cause) {
        return new VerificationFacade() {
            @Override
            public R<Boolean> updateContractAmount(List<CourtCostVerificationDTO> dtoList) {
                return R.fail("调用会计引擎服务更新合同转回拨备接口失败");
            }
        };
    }
}
