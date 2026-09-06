package com.utfinancing.financehub.engine.claim.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description : 报销系统-报销单付款信息表VO对象
 * @Modified :
 */
@Data
public class ClaimOrderPaymentVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "报销单主表ID")
    private Long claimOrderId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "收款账户")
    private String receiptAccount;

    @ApiModelProperty(value = "收款支行")
    private String receiptBranchBank;

    @ApiModelProperty(value = "收款行联行号")
    private String receiptBankUnionNum;

    @ApiModelProperty(value = "支付方式")
    private String paymentWay;

    @ApiModelProperty(value = "支付金额")
    private String paymentAmount;

    @ApiModelProperty(value = "swiftCode")
    private String swiftCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行编码（名称）")
    private String bankNum;

    @ApiModelProperty(value = "交易附言")
    private String dealAddition;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "收款行国家/地区")
    private String receiptCountry;

    @ApiModelProperty(value = "收款人常驻国家/地区")
    private String payeeCountry;

    @ApiModelProperty(value = "境外收款行地址")
    private String receiptBankOverseasAddress;

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

}
