package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :   ConvertTransferPlan查询from对象
 * @Modified :
 */
@ApiModel("ConvertTransferPlan查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConvertTransferPlanQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "折价转让id", required = true)
    @NotNull(message = "请选择一个汇总")
    private Long convertTransferId;

    @ApiModelProperty(value = "原合同编号")
    private String oldContractCode;


}
