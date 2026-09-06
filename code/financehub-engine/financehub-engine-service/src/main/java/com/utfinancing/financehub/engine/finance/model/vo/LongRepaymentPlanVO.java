package com.utfinancing.financehub.engine.finance.model.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-12
 * @Description : 长期应收款-偿还计划VO对象
 * @Modified :
 */
@Data
public class LongRepaymentPlanVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "长期应收款编号")
    private String longReceivableNumber;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "应收日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime receivableDate;

    @ApiModelProperty(value = "应收总额")
    private BigDecimal receivableTotal;

    @ApiModelProperty(value = "应收本金")
    private BigDecimal receivablePrincipal;

    @ApiModelProperty(value = "应收利息")
    private BigDecimal receivableInterest;

    @ApiModelProperty(value = "实收日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date actualRepaymentDate;

    @ApiModelProperty(value = "实收金额")
    @Excel(name = "实收金额")
    private BigDecimal repaymentAmount;

    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date invoiceDate;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

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

    @ApiModelProperty(value = "长期应收款id")
    private Long longRegisterId;

}
