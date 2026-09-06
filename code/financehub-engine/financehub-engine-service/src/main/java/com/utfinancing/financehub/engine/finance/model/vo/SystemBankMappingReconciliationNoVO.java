package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemBankMappingReconciliationNoVO {

    @ApiModelProperty("推送日期")
    @Excel(name = "推送日期")
    private String createTime;

    @ApiModelProperty("大网银编号")
    @Excel(name = "大网银编号")
    private String onlineBankNo;

    @ApiModelProperty("小网银编号")
    @Excel(name = "小网银编号")
    private String deductBatchNo;

    @ApiModelProperty("批扣总金额")
    @Excel(name = "批扣总金额")
    private String collectAmount;
}
