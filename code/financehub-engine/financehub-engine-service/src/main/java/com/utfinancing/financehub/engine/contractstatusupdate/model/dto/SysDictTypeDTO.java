package com.utfinancing.financehub.engine.contractstatusupdate.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.*;
import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * SysDictTypeDTO数据传输对象
 */
@Data
@ApiModel(description = "SysDictTypeDTO数据传输对象")
public class SysDictTypeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "字典主键")
	private Long dict_id;

	@ApiModelProperty(value = "字典名称")
	private String dict_name;

	@ApiModelProperty(value = "字典类型")
	private String dict_type;

	@ApiModelProperty(value = "状态（0正常 1停用）")
	private String status;

	@ApiModelProperty(value = "创建者")
	private String create_by;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime create_time;

	@ApiModelProperty(value = "更新者")
	private String update_by;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime update_time;

	@ApiModelProperty(value = "备注")
	private String remark;

}