package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description : 业务配置DTO对象
 * @Modified :
 */
@Data
public class BusinessSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "科目余额表编码")
    private String accountBalanceCode;

}
