package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description :   MarginContractBalance查询from对象
 * @Modified :
 */
@ApiModel("MarginContractBalance审核表单")
@Data
public class MarginContractBalanceCheckDTO {


    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    @ApiModelProperty(value = "查询日期")
    private Date balanceDate;

    @ApiModelProperty(value = "公司")
    private List<String> orgIdList;

    @ApiModelProperty(value = "科目编码List")
    private List<String> accountCodeList;

    @ApiModelProperty(value = "批次id")
    private List<Long> batchIdList;

}
