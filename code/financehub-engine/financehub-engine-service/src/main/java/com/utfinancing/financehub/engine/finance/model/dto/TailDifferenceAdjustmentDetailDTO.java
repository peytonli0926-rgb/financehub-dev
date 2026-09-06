package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description : 尾差调整详情DTO对象
 * @Modified :
 */
@Data
public class TailDifferenceAdjustmentDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "尾差调整Id")
    private Long tailDifferenceAdjustmentId;

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

    @ApiModelProperty("凭证id多个逗号分隔")
    private String voucherIds;

    @ApiModelProperty(value = "应收租金余额")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值余额")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应付设备款余额")
    private BigDecimal payableDevice;

    @ApiModelProperty(value = "应付其他款项余额")
    private BigDecimal payableOther;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "未实现融资收益-待摊收益")
    private BigDecimal rentalIncomeAfterTotal;

    @ApiModelProperty(value = "未实现融资租赁收益-待摊收益")
    private BigDecimal rentalIncomeAfterLeaseTotal;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;
}
