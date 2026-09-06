package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-12-28
 * @Description :   RepaymentPlanCost查询from对象
 * @Modified :
 */
@ApiModel("RepaymentPlanCost查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RepaymentPlanCostQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同号")
    private String contractCode;
    private List<String> contractCodeList;

    @ApiModelProperty(value = "账期")
    private String accountDate;

    @ApiModelProperty(value = "计划日期")
    private Date planDate;

    @ApiModelProperty(value = "成本类本月汇总金额")
    private BigDecimal cashFlow;
}
