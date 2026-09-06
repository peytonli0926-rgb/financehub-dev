package com.utfinancing.financehub.engine.scene.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 场景凭证分录条件配置;DTO对象
 * @Modified :
 */
@Data
public class SceneVoucherConditionDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "场景凭证分录ID")
    private Long sceneVoucherEntryId;

    @ApiModelProperty(value = "序号")
    private Integer serial;

    @ApiModelProperty(value = "条件")
    private String scriptCondition;

    @ApiModelProperty(value = "条件描述")
    private String donditionDescription;

    @ApiModelProperty(value = "金额")
    private String scriptAmount;

    @ApiModelProperty(value = "金额描述")
    private String amountDescription;

    @ApiModelProperty(value = "借贷方向")
    private String debitCreditType;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
