package com.utfinancing.financehub.etl.financial.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 16/11/2023
 */
@Data
public class KingdeeVoucherEntryInnerDTO {

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherTypeCode;

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

    @ApiModelProperty(value = "金额类型;refDict")
    private String fundType;

    @ApiModelProperty(value = "是否银行账号相关(0:否 1是)")
    private String relateBankFlag;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "现金流属性")
    private String cashAttribute;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证金额")
    private String voucherAmount;

    @ApiModelProperty(value = "客户标识(0:否 1是)")
    private String clientFlag;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    //银行编号
    private String bankNumber;

    //借款合同编号
    private String billContractCode;

    @ApiModelProperty(value = "合同标识(0:否 1是)")
    private String contractFlag;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "借贷方向")
    private String debitCreditType;

    //币种
    private String currencyCode;

    @ApiModelProperty(value = "财务账期(yyyyMM)")
    private Integer accountPeriod;

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

    @ApiModelProperty(value = "借方发生额")
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    private BigDecimal creditAmount;


}
