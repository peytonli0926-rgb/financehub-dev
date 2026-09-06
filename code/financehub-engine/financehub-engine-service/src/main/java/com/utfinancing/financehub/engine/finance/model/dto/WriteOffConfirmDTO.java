package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class WriteOffConfirmDTO implements Serializable {

    @ApiModelProperty(value = "未确认收款ID", required = true)
    private Long id;

    @ApiModelProperty(value = "记账日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐(1:是, 0:否)", required = true)
    private String isRelateClientAuxiliaryAccount;

//    @ApiModelProperty(value = "是否修改入账日期")
//    private String isModifyIncomeDate;
//
//    @ApiModelProperty(value = "原入账年月（字段：是否修改入账日期 为是时，该字段必填）")
//    private String incomeYmOld;

    @ApiModelProperty(value = "认领数据明细")
    private List<WriteOffDetailDTO> writeOffDetailList = new ArrayList<>();

    @ApiModelProperty(value = "认领凭证明细")
    private List<ClaimConfirmVoucherDTO> writeOffConfirmVoucherList = new ArrayList<>();
}
