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
 * @Description :   SceneVoucher查询from对象
 * @Modified :
 */
@ApiModel("SceneVoucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SceneVoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

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

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;
}
