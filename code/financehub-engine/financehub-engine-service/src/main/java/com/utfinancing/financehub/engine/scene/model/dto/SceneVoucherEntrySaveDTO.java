package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 场景凭证分录配置;DTO对象
 * @Modified :
 */
@Data
public class SceneVoucherEntrySaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "场景凭证配置ID")
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

    @ApiModelProperty(value = "凭证维度列表-[客户,合同]")
    private List<String> assistFlags;

    //是否现金流属性相关(0:否 1:是)
    private String cashAttributeFlag;

    @ApiModelProperty(value = "凭证行规则")
    private List<SceneVoucherConditionSaveDTO> conditionList;

}
