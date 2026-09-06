package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-27
 * @Description : 字段映射表DTO对象
 * @Modified :
 */
@Data
public class FieldMappingSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "字段编码")
    private String fieldCode;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "业务系统源数据")
    private String sourceValue;

    @ApiModelProperty(value = "财务中台数据")
    private String targetValue;

    @ApiModelProperty(value = "默认数据")
    private String defaultValue;

    @ApiModelProperty("目标字段")
    private String targetFieldCode;


}
