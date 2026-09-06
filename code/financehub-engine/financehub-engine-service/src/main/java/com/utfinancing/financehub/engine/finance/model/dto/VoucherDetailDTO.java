package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Author : lixin
 * @Date : Create in 24/10/2023
 * @Description 凭证明细DTO，包含凭证头和凭证行
 */
@Data
public class VoucherDetailDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "批次ID")
    private Long batchId;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherType;

    @ApiModelProperty(value = "签约主体编码")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "业务日期")
    private LocalDate businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDate voucherDate;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    private String sceneName;

    @ApiModelProperty(value = "科目代码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "借方发生额")
    private String debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    private String creditAmount;

    @ApiModelProperty(value = "凭证头摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证行摘要")
    private String voucherEntrySummary;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "制单人工号")
    private String createUserNo;

    @ApiModelProperty(value = "制单人姓名")
    private String createUserName;

    @ApiModelProperty(value = "复核人工号")
    private String recheckUserNo;

    @ApiModelProperty(value = "复核人姓名")
    private String recheckUserName;

    @ApiModelProperty(value = "处理状态")
    private String voucherStatus;

    @ApiModelProperty(value = "是否可以编辑，0:不可以，1：可以")
    private String editFlag;


    //借款合同编号
    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "汇率")
    private String taxRate;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    private String isRelatedOtherCustomer;

    @ApiModelProperty(value = "金蝶生成凭证后的凭证编码")
    private String easVoucherNumber;

    @ApiModelProperty(value = "金蝶凭证ID")
    private String easVoucherId;

    @ApiModelProperty(value = "eas跳转Url")
    private String easLoginUrl;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

    @ApiModelProperty(value = "数据来源0:凭证表，1：手工表")
    private String sourceFromType;

    @ApiModelProperty(value = "是否冲销：0")
    private String isWriteOff;
    //应付保险费-暂估期初余额
    private BigDecimal payableInsuranceEstimateBalanceOpening;

    @ApiModelProperty(value = "凭证方向")
    private String debitCreditType;


}
