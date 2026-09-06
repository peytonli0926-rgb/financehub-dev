package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-29
 * @Description :   ServiceFeePlan查询from对象
 * @Modified :
 */
@ApiModel("ServiceFeePlan查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceFeePlanQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "服务费协议编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "服务费签约主体")
    private String serviceOrgId;

    @ApiModelProperty(value = "服务费摊销利率")
    private BigDecimal serviceFeeAmortizationRate;

    @ApiModelProperty(value = "实际计提金额")
    private BigDecimal actualAccruedAmount;

    @ApiModelProperty(value = "调整金额")
    private BigDecimal adjustAmount;
}
