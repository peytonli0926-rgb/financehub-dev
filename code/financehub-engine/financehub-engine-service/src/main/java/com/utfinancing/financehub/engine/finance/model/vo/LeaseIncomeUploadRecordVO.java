package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : robjiang
 * @Date : Create in 2025-11-20
 * @Description : 收益计提上传记录VO对象
 * @Modified :
 */
@Data
public class LeaseIncomeUploadRecordVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "计提月份")
    private Date businessDate;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "是否计提")
    private String accrued;

    @ApiModelProperty(value = "是否逾期")
    private String laborOverdueMark;

    @ApiModelProperty(value = "计提方式(XIRR分摊收益/实收/IRR分摊收益)")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "上期实收期间")
    private Date previousPaidPeriod;

    @ApiModelProperty(value = "是否删除 0：未删除1：已删除")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
