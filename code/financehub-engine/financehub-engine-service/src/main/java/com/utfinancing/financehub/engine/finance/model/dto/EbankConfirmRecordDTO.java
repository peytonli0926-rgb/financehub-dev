package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EbankConfirmRecordDTO implements Serializable {
    @ApiModelProperty(value = "网银确认日期")
    private String ebankConfirmDate;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "网银确认内容")
    private String ebankConfirmComments;

    @ApiModelProperty(value = "确认金额")
    private BigDecimal confirmAmount;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTime;

    private int batchNo;
}
