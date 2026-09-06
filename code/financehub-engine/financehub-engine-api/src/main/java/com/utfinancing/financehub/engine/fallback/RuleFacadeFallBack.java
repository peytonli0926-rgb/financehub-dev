package com.utfinancing.financehub.engine.fallback;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.api.RuleFacade;
import com.utfinancing.financehub.engine.model.vo.VoucherVO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.fallback.IRuleFacadeFallBack</li>
 * <li>CreateTime : 2023/11/13 15:05</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Component
public class RuleFacadeFallBack implements FallbackFactory<RuleFacade> {
    private static final Logger log = LoggerFactory.getLogger(RuleFacadeFallBack.class);

    @Override
    public RuleFacade create(Throwable cause) {
        log.info("调用engine接口失败");
        return new RuleFacade() {
            @Override
            public R<List<VoucherVO>> executeRule(Map<String, Object> dataMap) {
                return null;
            }
        };
    }
}
