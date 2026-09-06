package com.utfinancing.financehub.engine.scene.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 科目DTO对象
 * @Modified :
 */
@Data
public class AccountSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "金额类型")
    private String fundType;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目性质")
    private String accountCategory;

    @ApiModelProperty(value = "余额方向（DR/CR）")
    private String debitCreditType;

    @ApiModelProperty(value = "凭证维度-客户(0:否 1是)")
    private String clientFlag;

    @ApiModelProperty(value = "凭证维度-合同(0:否 1是)")
    private String contractFlag;

    @ApiModelProperty(value = "凭证维度列表")
    private List<String> assistFlags;

    @ApiModelProperty("核算类型")
    private String settlementType;
}
