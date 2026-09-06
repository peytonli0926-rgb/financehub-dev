package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description : 科目辅助帐余额表VO对象
 * @Modified :
 */
@Data
public class AccountCurrentBalanceSheetVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "科目编码")
    @Excel(name = "科目编码",width = 20)
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "科目名称",width = 20)
    private String accountName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "业务类型")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    @Excel(name = "业务类型",width = 20)
    private String businessCodeDesc;

    @ApiModelProperty(value = "币种")
    @Excel(name = "币种",width = 20)
    private String currencyCode;

    @ApiModelProperty(value = "记账日期")
    @Excel(name = "记账日期",width = 20)
    private String voucherDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体",width = 20)
    private String orgName;

    @ApiModelProperty(value = "本期借方发生额")
    @Excel(name = "本期借方发生额",width = 20)
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "本期贷方发生额")
    @Excel(name = "本期贷方发生额",width = 20)
    private BigDecimal creditAmount;

    @ApiModelProperty(value = "凭证id，逗号隔开")
    private String voucherIds;

}
