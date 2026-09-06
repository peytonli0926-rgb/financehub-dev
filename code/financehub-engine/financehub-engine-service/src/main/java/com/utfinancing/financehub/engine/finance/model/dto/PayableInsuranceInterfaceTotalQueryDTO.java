package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-30
 * @Description :   PayableInsuranceInterfaceTotal查询from对象
 * @Modified :
 */
@ApiModel("PayableInsuranceInterfaceTotal查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayableInsuranceInterfaceTotalQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "实际计划支付保险费")
    private String actualPayableInsuaranceAmount;

    @ApiModelProperty(value = "应付保险费")
    private String payableInsuranceAmount;

    @ApiModelProperty(value = "应付保险费余额")
    private String payableInsuranceBalance;

    @ApiModelProperty(value = "是否可用1:可用 0:不可用")
    private String enableFlag;
}
