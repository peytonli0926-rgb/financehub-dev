package com.utfinancing.financehub.engine.verification.entity;

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
 * 实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_verification_details")
public class VerificationDetailsEntity extends Model<VerificationDetailsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //核销表id
    private Long verificationId;

    //合同编号
    private String contractCode;

    //财务合同状态
    private String financialContractStatus;

    //记账日期
    private LocalDateTime accountDate;

    //客户名称
    private String clientName;

    //签约主体
    private String orgId;

    //应收租金
    private BigDecimal receivableRent;

    //应收期末残值
    private BigDecimal receivableResidualValue;

    //应收首付款
    private BigDecimal receivableDownpayment;

    //应收手续费
    private BigDecimal receivableCommission;

    //应收保险费
    private BigDecimal receivableInsurance;

    //应收其他收入
    private BigDecimal receivableOtherincome;

    //应收销项税
    private BigDecimal receivableOuttax;

    //未实现收益
    private BigDecimal unrealizedRevenue;

    //应付设备款-暂估
    private BigDecimal payableDeviceEstimate;

    //应付经销商服务费-暂估
    private BigDecimal payableAgencyEstimate;

    //应付收车费-暂估
    private BigDecimal payableVehicleEstimate;

    //应付手环成本_暂估
    private BigDecimal payableBandCostEstimate;

    //应付抵押费_暂估
    private BigDecimal payablePledgeEstimate;

    //应付解抵押费_暂估
    private BigDecimal payableUnpledgeEstimate;

    //应付其他租赁成本-暂估
    private BigDecimal payableOtherCostEstimate;

    //财务核销敞口
    private BigDecimal financialExpenseAmount;

    //补偿提备
    private BigDecimal compensationProvisionAmount;

    //应收租赁款组合拨备（减值准备）
    private BigDecimal depreciationReserves;

    //是否删除 0：未删除1：已删除
    @TableLogic
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

    //客户编码
    private String clientCode;

    //凭证id 多个以逗号分隔
    private String voucherId;

    @ApiModelProperty("凭证报错信息")
    private String errorInfo;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty("创建人姓名")
    private String createName;

}
