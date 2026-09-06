package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-11
 * @Description : 长期应收款登记DTO对象
 * @Modified :
 */
@Data
public class LongReceivableRegisterDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "长期应收款编号")
    private String longReceivableNumber;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "应收总额")
    private BigDecimal receivableTotal;

    @ApiModelProperty(value = "应收本金")
    private BigDecimal receivablePrincipal;

    @ApiModelProperty(value = "应收利息")
    private BigDecimal receivableInterest;

    @ApiModelProperty(value = "其他应收款")
    private BigDecimal otherReceivable;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "记账日期")
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

    @ApiModelProperty(value = "版本号")
    private Integer versionNum;

}
