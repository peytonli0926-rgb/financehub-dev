package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-17
 * @Description :   FundPaymentData查询from对象
 * @Modified :
 */
@ApiModel("FundPaymentData查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class FundPaymentDataQueryDTO extends BaseQueryDTO{

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

    @ApiModelProperty(value = "签约主体")
    private String orgId;
    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
    @ApiModelProperty(value = "票据类型")
    private String billType;
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "业务时间 yyyy-MM-dd")
    private String businessDate;

    @ApiModelProperty(value = "单据开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDocumentDate;

    @ApiModelProperty(value = "单据结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDocumentDate;
}
