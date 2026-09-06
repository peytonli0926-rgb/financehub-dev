package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 场景凭证配置DTO对象
 * @Modified :
 */
@Data
public class SceneVoucherSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

    @ApiModelProperty(value = "规则执行条件")
    private String scriptCondition;

    @ApiModelProperty(value = "来源;refInterface")
    private String source;

    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherType;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "业务日期")
    private String businessDate;

    @ApiModelProperty(value = "凭证日期")
    private String voucherDate;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证名称")
    private String sceneVoucherName;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

}
