package com.utfinancing.financehub.engine.api;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.fallback.RuleFacadeFallBack;
import com.utfinancing.financehub.engine.model.vo.VoucherVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.api.RuleFacade</li>
 * <li>CreateTime : 2023/11/13 14:52</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@FeignClient(
        value = "financehub-engine-service",
        contextId = "ruleFacade",
        fallbackFactory = RuleFacadeFallBack.class)
public interface RuleFacade {

    @PostMapping("/rule/executeRule")
    @ApiOperation(value = "生成凭证")
    R<List<VoucherVO>> executeRule(@RequestBody Map<String, Object> dataMap);
}
