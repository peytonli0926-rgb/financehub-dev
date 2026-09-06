package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-16
 * @Description : 资金系统、业务系统网银编号金额DTO对象
 * @Modified :
 */
@Data
public class FundBusinessSystemEbankWyAmountDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "ebank_amount_id")
    private Long ebankAmountId;

    @ApiModelProperty(value = "勾稽编号")
    private String matchNumber;

    @ApiModelProperty(value = "资金系统网银编号")
    private String ebankNumber;

    @ApiModelProperty(value = "网银金额")
    private BigDecimal wyAmount;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0:否，1：是）")
    private String delFlag;

    @ApiModelProperty("收款账号（虚拟户）")
    private String collectionAccountsBankNo;

    @ApiModelProperty("收款开户行")
    private String collectionAccountsBank;

    @ApiModelProperty("客户编码")
    private String clientCode;

    @ApiModelProperty("客户名称")
    private String clientName;

    private Date businessDate;

    @ApiModelProperty("票据号")
    private String billNumber;

    @ApiModelProperty("银行交易摘要")
    private String bankSummary;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("对方客户开户行")
    private String clientAccountsBank;

    @ApiModelProperty("对方客户银行账号")
    private String clientAccountsBankNo;

    @ApiModelProperty("币种")
    private String currencyType;

}
