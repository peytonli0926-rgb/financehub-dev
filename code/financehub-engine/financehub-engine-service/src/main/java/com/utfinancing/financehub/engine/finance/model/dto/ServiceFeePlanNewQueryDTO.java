package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description :   ServiceFeePlanNew查询from对象
 * @Modified :
 */
@ApiModel("ServiceFeePlanNew查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceFeePlanNewQueryDTO extends BaseQueryDTO {

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "组织机构ID")
    private String orgId;

    @ApiModelProperty(value = "服务费编号")
    private String serviceFeeNo;

    @ApiModelProperty(value = "服务方组织机构ID")
    private String serviceOrgId;

    @ApiModelProperty(value = "分摊比例")
    private String apportionmentRate;

    @ApiModelProperty(value = "计提金额")
    private String accruedAmount;

    @ApiModelProperty(value = "调整金额")
    private String adjustAmount;

    @ApiModelProperty(value = "计划日期")
    private Date planDate;

    @ApiModelProperty(value = "应收服务费金额(税前)")
    private BigDecimal receivableServiceFeeTaxInclude;

    @ApiModelProperty(value = "应收服务费金额(税后)")
    private BigDecimal receivableServiceFeeNoTax;

    @ApiModelProperty(value = "实收服务费(税前)")
    private BigDecimal receivedServiceFeeTaxInclude;

    @ApiModelProperty(value = "实收服务费(税后)")
    private BigDecimal receivedServiceFeeNoTax;

    @ApiModelProperty(value = "期数")
    private Integer periods;

    @ApiModelProperty(value = "计划金额(税前)")
    private BigDecimal planAmountTaxInclude;

    @ApiModelProperty(value = "不含税计划金额(税后)")
    private BigDecimal planAmountNoTax;
    @ApiModelProperty(value = "计提比例")
    private BigDecimal accrualRate;
}
