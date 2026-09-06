package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanExceldataEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-01-23
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RepaymentPlanExceldataDTO extends RepaymentPlanExceldataEntity {

    private static final long serialVersionUID = 1L;

    private String recordIsUsed;
}
