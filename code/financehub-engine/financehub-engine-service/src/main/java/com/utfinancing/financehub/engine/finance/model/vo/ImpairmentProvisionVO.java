package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提VO对象
 * @Modified :
 */
@Data
public class ImpairmentProvisionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "拨备合计")
    private BigDecimal provisionTotal;

    @ApiModelProperty(value = "上月余额")
    private BigDecimal lastMonthBalance;

    @ApiModelProperty(value = "本月计提")
    private BigDecimal thisMonthProvision;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime accountDate;

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

    @ApiModelProperty(value = "冲销来源id")
    private Long writeOffOriginalId;

    @ApiModelProperty(value = "是否被冲销（0：否，1：是）")
    private String isWriteOff;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty("会计期间")
    private Integer periodCode;


}
