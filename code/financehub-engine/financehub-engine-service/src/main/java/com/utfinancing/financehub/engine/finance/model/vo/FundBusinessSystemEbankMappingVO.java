package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-21
 * @Description : 资金系统、业务系统网银编号映射表VO对象
 * @Modified :
 */
@Data
public class FundBusinessSystemEbankMappingVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "勾稽编号")
    private String matchNumber;

    @ApiModelProperty(value = "资金系统网银编号")
    private String ebankNumber;

    @ApiModelProperty(value = "业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "勾稽金额")
    private String matchAmount;

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

    @ApiModelProperty("消息状态：NOT_EXECUTE:未执行,RUNNING:进行中,SUCCESS:成功,FAILED:失败")
    private String messageStatus;

    @ApiModelProperty("错误信息")
    private String errorInfo;

}
