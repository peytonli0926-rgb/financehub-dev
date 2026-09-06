package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description : 网银收付款数据表VO对象
 * @Modified :
 */
@Data
public class FundPaymentDataVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "交易流水号(必填，需保证唯一)")
    private String orderId;

    @ApiModelProperty(value = "业务事件")
    private String businessOperation;

    @ApiModelProperty(value = "网银编号")
    private String ebankNumber;

    @ApiModelProperty(value = "付款账号")
    private String paymentBankNo;

    @ApiModelProperty(value = "收款开户行")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "合同号")
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "网银金额")
    private BigDecimal bankAmount;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "付款单")
    private String paymentOrder;

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

    @ApiModelProperty("票据号")
    private String billNumber;

    @ApiModelProperty("细分票据类型")
    private String segmentedBillType;

    @ApiModelProperty("业务日期操作付款日期")
    private String businessDate;

}
