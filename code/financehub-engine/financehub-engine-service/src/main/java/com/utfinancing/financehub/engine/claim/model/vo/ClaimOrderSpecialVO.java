package com.utfinancing.financehub.engine.claim.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description : 报销系统-报销单专项费明细VO对象
 * @Modified :
 */
@Data
public class ClaimOrderSpecialVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "报销单主表ID")
    private Long claimOrderId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "序号")
    private String no;

    @ApiModelProperty(value = "组织")
    private String org;

    @ApiModelProperty(value = "系统")
    private String sourceSystem;

    @ApiModelProperty(value = "合同号")
    private String contractNum;

    @ApiModelProperty(value = "承租人")
    private String tenant;

    @ApiModelProperty(value = "账期")
    private String accountDate;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "设备款不含税额")
    private BigDecimal equipNotaxAmount;

    @ApiModelProperty(value = "设备款进项税")
    private BigDecimal equipInputTax;

    @ApiModelProperty(value = "安装费")
    private BigDecimal installAmount;

    @ApiModelProperty(value = "安装费不含税额")
    private BigDecimal installNotaxAmount;

    @ApiModelProperty(value = "安装费进项税")
    private BigDecimal installInputTax;

    @ApiModelProperty(value = "服务费")
    private BigDecimal serviceAmount;

    @ApiModelProperty(value = "服务费不含税额")
    private BigDecimal serviceNotaxAmount;

    @ApiModelProperty(value = "服务费进项税")
    private BigDecimal serviceInputTax;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "是否转天津")
    private String tianjiFlag;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal paymentNotaxAmount;

    @ApiModelProperty(value = "进项税")
    private BigDecimal paymentInputTax;

    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    @ApiModelProperty(value = "凭证标识")
    private String voucherFlag;

    @ApiModelProperty(value = "item01")
    private String item01;

    @ApiModelProperty(value = "item01")
    private String item02;

    @ApiModelProperty(value = "item01")
    private String item03;

    @ApiModelProperty(value = "item01")
    private String item04;

    @ApiModelProperty(value = "item01")
    private String item05;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;


    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "费用时间")
    private LocalDateTime expenseDate;

    @ApiModelProperty(value = "签约主体Id")
    private String orgId;

    private String currencyType;

    @ApiModelProperty(value = "内容摘要")
    private String contentAbstract;
}
