package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QueryDetailDataDTO implements Serializable {
    @ApiModelProperty(value = "网银确认开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ebankConfirmDateStart;

    @ApiModelProperty(value = "网银确认结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ebankConfirmDateEnd;

    @ApiModelProperty(value = "网银到账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ebankIncomeDateStart;

    @ApiModelProperty(value = "网银到账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ebankIncomeDateEnd;

    @ApiModelProperty(value = "汇总页数据ID", required = true)
    private String sumId;
//
//    @ApiModelProperty(value = "列表数据")
//    private List<QueryDetailListDataDTO> listData = new ArrayList<>();
}
