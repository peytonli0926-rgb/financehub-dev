package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用模块名称: 金蝶凭证推送参数类
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/5/8 13:44
 */
@Data
public class HthxPushVoucherParamsDTO implements Serializable {

    @ApiModelProperty("单据id")
    private Long documentId;

    @ApiModelProperty("单据类型")
    private String documentType;

    @ApiModelProperty("提交人工号")
    private String submitUserId;

    @ApiModelProperty("提交人姓名")
    private String submitUserName;


}
