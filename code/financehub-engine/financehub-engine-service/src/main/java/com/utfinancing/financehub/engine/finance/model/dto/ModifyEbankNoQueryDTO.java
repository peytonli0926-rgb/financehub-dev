package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ModifyEbankNoQueryDTO implements Serializable {

    @ApiModelProperty(value = "未确认收款ID", required = true)
    private String sumId;


    @ApiModelProperty(value = "汇总认领记录")
    private List<BusinessClaimRepaymentRecordDTO> claimRecordNewList = new ArrayList<>();

    @ApiModelProperty(value = "原认领记录")
    private List<BusinessClaimRepaymentRecordDTO> claimRecordOldList = new ArrayList<>();
}
