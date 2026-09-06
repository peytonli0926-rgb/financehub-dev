package com.utfinancing.financehub.engine.api;

import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.fallback.FieldMappingFacadeFallBack;
import com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO;
import com.utfinancing.financehub.engine.model.vo.FieldMappingApiVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.api.FieldMappingFacade</li>
 * <li>CreateTime : 2023/11/29 17:24</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@FeignClient(
        value = "financehub-engine-service",
        contextId = "fieldMappingFacade",
        fallbackFactory = FieldMappingFacadeFallBack.class)
public interface FieldMappingFacade {

    @PostMapping("/scene/field-mapping/selectFieldMappingByCondition")
    R<List<FieldMappingApiVO>> selectFieldMappingByCondition(FieldMappingApiDTO queryDTO);

}
