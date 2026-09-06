package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChangeRepaymentDTO implements Serializable {
    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "偿还计划")
    private List<RepaymentPlanSaveDTO> repaymentPlanList = new ArrayList<>();

    @ApiModelProperty(value = "是否起租")
    private String isLease;
}
