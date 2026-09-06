package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.FundSystemVoucherDTO</li>
 * <li>CreateTime : 2023/12/05 09:53</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "资金系统生成凭证DTO")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FundSystemVoucherDTO extends ExecuteCommonDTO {

    @ApiModelProperty("交易类型")
    private String transactionType;

    @ApiModelProperty("操作日期")
    private String operationDate;

    @ApiModelProperty("网银编号")
    private String ebankNumber;

    @ApiModelProperty("收款账户（虚拟账户）")
    private String collectionAccountsBankNo;

    @ApiModelProperty("收款开户行")
    private String collectionAccountsBank;

    @ApiModelProperty("网银金额")
    private BigDecimal bankAmount;

    @ApiModelProperty("对方开户行")
    private String clientAccountsBank;

    @ApiModelProperty("对方银行账号")
    private String clientAccountsBankNo;

    @ApiModelProperty("付款账号")
    private String paymentAccountsBankNo;

    @ApiModelProperty("支付方式")
    private String paymentMethod;

    @ApiModelProperty("票据类型")
    private String billType;

    @ApiModelProperty("付款单")
    private String paymentOrder;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("细分票据类型")
    private String segmentedBillType;

    //是否生成凭证
    private String isGenerateVoucher;

    @ApiModelProperty("业务日期操作付款日期")
    private Date businessDate;

    @ApiModelProperty("票据号")
    private String billNumber;

    @ApiModelProperty("业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)")
    private String ebankSerialNumber;

    @ApiModelProperty("恒运金额")
    private BigDecimal claimAmount;

    @ApiModelProperty("恒运批次流水号")
    private String ebankBatchNo;

    @ApiModelProperty("收款类型")
    private String collectionType;

    @ApiModelProperty("付款项目")
    private String paymentItem;

    @ApiModelProperty("付款类型")
    private String paymentType;

    @ApiModelProperty("付款批次号")
    private String batchNumber;

    //币种
    @ApiModelProperty("币种")
    private String currencyType;

    @ApiModelProperty("实际客户名")
    private String actualClientName;
    @ApiModelProperty("实际客户名")
    private String actualClientCode;
}
