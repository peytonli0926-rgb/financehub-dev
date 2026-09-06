package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-25
 * @Description : 场景凭证头
 * @Modified :
 */
@Data
public class SceneRuleSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "*场景ID")
    @NotNull(message = "场景ID不能为空")
    private Long sceneId;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "凭证名称")
    private String sceneVoucherName;

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

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "凭证行")
    private List<SceneVoucherEntrySaveDTO> entryList;

    //@ApiModelProperty(value = "删除的凭证行")
    //private List<SceneVoucherEntryDTO> removeEntryList;

}
