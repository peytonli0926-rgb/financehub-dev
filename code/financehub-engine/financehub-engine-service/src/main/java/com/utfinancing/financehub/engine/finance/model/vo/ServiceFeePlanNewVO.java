package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : le
 * @Date : Create in 2025-11-10
 * @Description : VO对象
 * @Modified :
 */
@Data
public class ServiceFeePlanNewVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

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

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

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
    //计提比例
    private BigDecimal accrualRate;

    @ApiModelProperty(value = "凭证状态")
    private String voucherStatus;
    @ApiModelProperty(value = "是否分摊标记")
    private Boolean sharingServiceFeeFlag;
    private String sharingServiceFeeFlagStr;

    @ApiModelProperty(value = "分摊完结标记")
    private Boolean endSharingServiceFeeFlag;
    private String endSharingServiceFeeFlagStr;

    @ApiModelProperty(value = "是否特殊状态调整")
    private Boolean specialStatusAdjustmentFlag;
    private String specialStatusAdjustmentFlagStr;
    @ApiModelProperty(value = "本月调整（税后）")
    private BigDecimal thisMonthAdjustmentAmountNoTax;
    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;
    @ApiModelProperty(value = "到期日")
    private Date leaseDateEnd;
    private String businessCode;
    private String businessName;


}
