package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class ConvertTransferThirdPartQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    //记账日期	转让批次	转让方
    @ApiModelProperty(value = "记账开始日期", example = "2024-12-20")
    private LocalDate accountStartDate;

    @ApiModelProperty(value = "记账结束日期", example = "2024-12-20")
    private LocalDate accountEndDate;

    @ApiModelProperty("转账批次")
    private String batch;

    @ApiModelProperty("转让方")
    private List<String> transferParty;
}
