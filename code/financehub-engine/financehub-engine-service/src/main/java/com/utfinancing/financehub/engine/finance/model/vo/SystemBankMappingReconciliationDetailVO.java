package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemBankMappingReconciliationDetailVO {

    @ApiModelProperty("交易日期")
    @Excel(name = "交易日期")
    private String businessDate;

    @ApiModelProperty("大网银编号")
    @Excel(name = "大网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("小网银编号")
    @Excel(name = "小网银编号")
    private String deductBatchNo;

    @ApiModelProperty("网银金额")
    @Excel(name = "网银金额")
    private String businessMatchAmount;

    @ApiModelProperty("批扣总金额")
    @Excel(name = "批扣总金额")
    private String collectAmount;

    @ApiModelProperty("差额")
    @Excel(name = "差额")
    private String diffAmount;

}
