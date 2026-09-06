package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description : 出表ABS合同详情VO对象
 * @Modified :
 */
@Data
public class OutTableContractDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "出表absId")
    private Long outTableAbsId;

    @ApiModelProperty(value = "借款合同编码")
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgIdName;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "封包日应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "封包日应收残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "封包日应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "封包日为实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "封包日承租人保证金")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "拨备余额")
    private BigDecimal depreciationReservesBalance;

    @ApiModelProperty(value = "封包日后计提收益")
    private BigDecimal leaseRevenueBalance;

    @ApiModelProperty(value = "封包日后收款")
    private BigDecimal receivableUnconfirmReceiptAmount;

    @ApiModelProperty(value = "封包日后开票")
    private BigDecimal receivableOuttaxDebtRestructureAmount;

    @ApiModelProperty(value = "应收融资租赁款")
    private BigDecimal financeLeaseReceivablesAmount;

    @ApiModelProperty(value = "出表期数")
    private String periods;

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "转让损益")
    private BigDecimal transferLossPrice;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty("记账时间")
    private LocalDateTime accountDate;
}
