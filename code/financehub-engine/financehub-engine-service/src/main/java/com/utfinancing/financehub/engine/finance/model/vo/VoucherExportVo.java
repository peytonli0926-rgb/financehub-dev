package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author : lixin
 * @Date : Create in 24/10/2023
 * @Description 凭证明细DTO，包含凭证头和凭证行
 */
@Data
public class VoucherExportVo {

    private String orgId;
    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体名称", width = 20, sort = 1)
    private String orgName;

    @ApiModelProperty(value = "会计期间")
    @Excel(name = "会计期间", width = 20, sort = 2)
    private Integer periodCode;

    @ApiModelProperty(value = "业务日期")
    @Excel(name = "业务日期", width = 20, sort = 3)
    private LocalDate businessDate;

    @ApiModelProperty(value = "记账日期")
    @Excel(name = "记账日期", width = 20, sort = 4)
    private LocalDate voucherDate;

    @ApiModelProperty(value = "凭证类型;refDict")
    @Excel(name = "凭证类型", width = 20, sort = 5)
    private String voucherType;

    @ApiModelProperty(value = "凭证号")
    @Excel(name = "凭证号", width = 20, sort = 6)
    private Long voucherNum;

    @ApiModelProperty(value = "业务场景名称")
    @Excel(name = "业务场景名称", width = 20, sort = 7)
    private String sceneName;

    @ApiModelProperty(value = "细分场景")
    @Excel(name = "细分场景", width = 20, sort = 8)
    private String subSceneType;

    @ApiModelProperty(value = "凭证头摘要")
    @Excel(name = "凭证头摘要", width = 20, sort = 9)
    private String voucherSummary;

    @ApiModelProperty(value = "科目代码")
    @Excel(name = "科目代码", width = 20, sort = 10)
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "科目名称", width = 20, sort = 11)
    private String accountName;

    @ApiModelProperty(value = "币种")
    @Excel(name = "币种", width = 20, sort = 12)
    private String currency;

    @ApiModelProperty(value = "汇率")
    @Excel(name = "汇率", width = 20, sort = 13)
    private String taxRate;

    @ApiModelProperty(value = "借方发生额")
    @Excel(name = "借方发生额", width = 20, sort = 14)
    private String debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    @Excel(name = "贷方发生额", width = 20, sort = 15)
    private String creditAmount;

    @ApiModelProperty(value = "制单人姓名")
    @Excel(name = "制单人姓名", width = 20, sort = 16)
    private String createUserName;

    @ApiModelProperty(value = "复核人姓名")
    @Excel(name = "复核人姓名", width = 20, sort = 17)
    private String recheckUserName;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态", width = 20, sort = 18)
    private String voucherStatus;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    @Excel(name = "是否涉及其他客户及辅助帐", width = 20, sort = 19)
    private String isRelatedOtherCustomer;


}
