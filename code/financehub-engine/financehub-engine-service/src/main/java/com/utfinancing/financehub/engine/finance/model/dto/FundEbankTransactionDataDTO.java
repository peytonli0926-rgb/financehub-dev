package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description : 网银收付款数据表DTO对象
 * @Modified :
 */
@Data
public class FundEbankTransactionDataDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "交易类型(必填,收款：collection,付款：payment)")
    private String transactionType;

    @ApiModelProperty(value = "交易流水号(必填，需保证唯一)")
    private String orderId;

    @ApiModelProperty(value = "业务日期(yyyy-MM-dd HH:mm:ss)")
    private String businessDate;

    @ApiModelProperty(value = "操作日期(yyyy-MM-dd HH:mm:ss)")
    private String operationDate;

    @ApiModelProperty(value = "业务事件")
    private String businessOperation;

    @ApiModelProperty(value = "网银编号")
    private String ebankNumber;

    @ApiModelProperty(value = "收款账号（虚拟户）")
    private String collectionAccountsBankNo;

    @ApiModelProperty(value = "收款开户行")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "对方合同号")
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "网银金额")
    private BigDecimal bankAmount;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "对方客户开户行")
    private String clientAccountsBank;

    @ApiModelProperty(value = "对方客户银行账号")
    private String clientAccountsBankNo;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
    @ApiModelProperty(value = "应收票据金额")
    private BigDecimal receivableBill;
    @ApiModelProperty(value = "应付票据金额")
    private BigDecimal payableBill;
    @ApiModelProperty(value = "付款账号")
    private String paymentAccountsBankNo;

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

    private String bankSummary;


}
