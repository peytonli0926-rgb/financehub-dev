package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 场景凭证行
 * @Modified :
 */
@Data
public class SceneVoucherEntryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "场景凭证配置ID")
    @NotNull(message = "场景凭证配置ID")
    private Long sceneVoucherId;

    @ApiModelProperty(value = "金额类型;refDict")
    private String fundType;

    @ApiModelProperty(value = "是否银行账号相关(0:否 1是)")
    private String relateBankFlag;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "现金流属性")
    private String cashAttribute;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证维度")
    private List<String> assistFlags;

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

    //是否现金流属性相关(0:否 1:是)
    private String cashAttributeFlag;


    @ApiModelProperty(value = "凭证行规则")
    private List<SceneVoucherConditionDTO> conditionList;

    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    //@ApiModelProperty(value = "删除的凭证行规则")
    //private List<SceneVoucherConditionDTO> removeConditionList;

}
