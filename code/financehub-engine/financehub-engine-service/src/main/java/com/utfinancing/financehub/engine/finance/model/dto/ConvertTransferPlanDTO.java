package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-租金计划DTO对象
 * @Modified :
 */
@Data
public class ConvertTransferPlanDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "折价转让id")
    private Long convertTransferId;

    @ApiModelProperty(value = "原合同编码")
    private String oldContractCode;

    @ApiModelProperty(value = "新合同编码")
    private String newContractCode;

    @ApiModelProperty(value = "计划日期")
    private LocalDateTime planDate;

    @ApiModelProperty(value = "期数")
    private Integer periods;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收本金")
    private BigDecimal receivablePrincipal;

    @ApiModelProperty(value = "应收利息")
    private BigDecimal receivableInterest;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableEndingSalvage;

    @ApiModelProperty(value = "凭证id(多个逗号分隔)")
    private String voucherId;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

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
