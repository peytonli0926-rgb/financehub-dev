package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@ApiModel("已勾稽明细查询表单")
public class SystemBankMappingReconciliationDetailQueryDTO extends BaseQueryDTO  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 改成之选一个，默认当前会计期间
     * 按当前时间计算 如 2023-12-22 -> 202312
     */
    @ApiModelProperty(value = "批扣号")
    @NotBlank(message = "批扣号 不能为空")
    private String matchNumber;

    @ApiModelProperty(value = "会计期间",example = "202312", required = true)
    @NotNull(message = "会计期间 不能为空")
    private Integer periodCode;


}
