package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-12
 * @Description : 资金系统、业务系统网银编号金额映射表DTO对象
 * @Modified :
 */
@Data
public class FundBusinessSystemEbankAmountMappingDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "勾稽编号")
    private String matchNumber;

    @ApiModelProperty(value = "勾稽金额")
    private BigDecimal matchAmount;

    @ApiModelProperty(value = "mq信息")
    private String mqMessage;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0:否，1：是）")
    private String delFlag;

    @ApiModelProperty(value = "批次金额数组")
    List<FundBusinessSystemEbankPcAmountDTO> pcList;

    @ApiModelProperty(value = "网银金额数组")
    List<FundBusinessSystemEbankWyAmountDTO> wyList;


    @ApiModelProperty("消息状态：NOT_EXECUTE:未执行,RUNNING:进行中,SUCCESS:成功,FAILED:失败")
    private String messageStatus;

    @ApiModelProperty("错误信息")
    private String errorInfo;


}
