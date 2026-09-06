package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ModifyEbankNoConfirmDTO implements Serializable {

    @ApiModelProperty(value = "未确认收款ID", required = true)
    private Long sumId;

    @ApiModelProperty(value = "记账日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountDate;

//    @ApiModelProperty(value = "是否修改入账日期")
//    private String isModifyIncomeDate;
//
//    @ApiModelProperty(value = "原入账年月（字段：是否修改入账日期 为是时，该字段必填）")
//    private String incomeYmOld;

    @ApiModelProperty(value = "汇总调整记录")
    private List<ModifyEbankNoConfirmRecordDTO> claimRecordNewList = new ArrayList<>();
}
