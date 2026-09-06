package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   SceneVoucherCondition查询from对象
 * @Modified :
 */
@ApiModel("SceneVoucherCondition查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SceneVoucherConditionQueryDTO extends BaseQueryDTO{

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
}
