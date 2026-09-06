package com.utfinancing.financehub.engine.bak.model.dto;
import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-18
 * @Description :   RawTransactionDataBak查询from对象
 * @Modified :
 */
@ApiModel("RawTransactionDataBak查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RawTransactionDataBakQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String systemCode;

    @ApiModelProperty(value = "")
    private String orderId;

    @ApiModelProperty(value = "")
    private String messageId;

    @ApiModelProperty(value = "")
    private String businessCode;

    @ApiModelProperty(value = "")
    private String orgId;

    @ApiModelProperty(value = "")
    private String sceneCode;

    @ApiModelProperty(value = "")
    private String contractCode;

    @ApiModelProperty(value = "")
    private String contractStatus;

    @ApiModelProperty(value = "")
    private JSONObject messageContent;

    @ApiModelProperty(value = "")
    private String messageStatus;

    @ApiModelProperty(value = "")
    private String errorInfo;
}
