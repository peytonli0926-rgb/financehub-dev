package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提上传Excel类型
 * @Modified :
 */
@Data
public class ImpairmentProvisionExcelTypeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "excel类型")
    private String excelType;

    @ApiModelProperty(name = "名称")
    private String name;


}
