package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 尾差调整详情实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_tail_difference_adjustment_detail")
public class TailDifferenceAdjustmentDetailEntity extends Model<TailDifferenceAdjustmentDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //签约主体
    private String orgId;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //业务日期
    private LocalDateTime businessDate;

    //记账日期
    private LocalDateTime accountDate;

    //科目余额
    private BigDecimal accountBalance;

    //尾差调整Id
    private Long tailDifferenceAdjustmentId;

    //是否删除（0：未删除1：删除）默认0
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //凭证id多个逗号分隔
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

    @ApiModelProperty(value = "约定到期日")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "应收总额")
    private BigDecimal receiveSum;
}
