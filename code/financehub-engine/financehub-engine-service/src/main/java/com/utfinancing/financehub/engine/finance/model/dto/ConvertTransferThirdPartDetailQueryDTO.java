package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class ConvertTransferThirdPartDetailQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    //记账日期	转让批次	转让方
    @ApiModelProperty(value = "转让方", required = true)
    @NotNull(message = "请选择一条汇总记录进行查看")
    private Long transferId;
}
