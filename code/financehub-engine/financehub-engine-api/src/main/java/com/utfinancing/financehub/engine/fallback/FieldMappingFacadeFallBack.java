package com.utfinancing.financehub.engine.fallback;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.api.FieldMappingFacade;
import com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO;
import com.utfinancing.financehub.engine.model.vo.FieldMappingApiVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.fallback.FieldMappingFacadeFallBack</li>
 * <li>CreateTime : 2023/11/29 17:25</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Component
public class FieldMappingFacadeFallBack implements FallbackFactory<FieldMappingFacade> {
    @Override
    public FieldMappingFacade create(Throwable cause) {
        return new FieldMappingFacade() {
            @Override
            public R<List<FieldMappingApiVO>> selectFieldMappingByCondition(FieldMappingApiDTO queryDTO) {
                return null;
            }
        };
    }
}
