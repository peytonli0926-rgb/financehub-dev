package com.utfinancing.financehub.engine.bak.model.vo;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-18
 * @Description : VO对象
 * @Modified :
 */
@Data
public class RawTransactionDataBakVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

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

    @ApiModelProperty(value = "")
    private String createBy;

    @ApiModelProperty(value = "")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    private String updateBy;

    @ApiModelProperty(value = "")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    private String delFlag;

}
