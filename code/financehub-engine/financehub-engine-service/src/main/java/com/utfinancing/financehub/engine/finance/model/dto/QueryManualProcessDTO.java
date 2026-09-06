package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionSecondDetailVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class QueryManualProcessDTO implements Serializable {

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String businessEbankNo;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "未确认收款明细表(第二层明细)ID")
    private String detailId;

    @ApiModelProperty(value = "未确认收款汇总表ID")
    private String sumId;
//
//    @ApiModelProperty(value = "手工处理明细")
//    private List<ManualProcessListDTO> secondDetailVOList = new ArrayList<>();
}
