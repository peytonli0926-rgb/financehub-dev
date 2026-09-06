package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-10
 * @Description : 租赁收益DTO对象
 * @Modified :
 */
@Data
public class LeaseIncomeDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "业务日期")
    private Date businessDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "租赁收益")
    private BigDecimal leaseIncomeAmount;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "提交人")
    private String submitBy;

}
