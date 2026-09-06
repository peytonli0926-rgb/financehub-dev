package com.utfinancing.financehub.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("分页查询基础类")
@Data
public class BaseQueryDTO {

	@ApiModelProperty(value = "当前页", example = "1")
	private int pageNum = 1;

	@ApiModelProperty(value = "页大小", example = "15")
	private int pageSize = 15;

}
