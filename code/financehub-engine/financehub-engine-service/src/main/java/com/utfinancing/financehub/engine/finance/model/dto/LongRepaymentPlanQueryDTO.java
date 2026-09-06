package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description :   LongRepaymentPlan查询from对象
 * @Modified :
 */
@ApiModel("LongRepaymentPlan查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class LongRepaymentPlanQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "长期应收款idList")
    private List<Long> longRegisterIdList;

}
