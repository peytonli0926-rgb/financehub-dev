package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description : 出售登记DTO对象
 * @Modified :
 */
@Data
public class SellRegisterDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "资产编号")
    private String assetNumber;

    @ApiModelProperty(value = "转出时间")
    private LocalDateTime transferOutDate;

    @ApiModelProperty(value = "买售人")
    private String buyOrSellPerson;

    @ApiModelProperty(value = "售价")
    private BigDecimal sellPrice;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
