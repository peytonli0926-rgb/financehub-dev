package com.utfinancing.financehub.engine.claim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资产费用对接表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_assets_expense")
public class AssetsExpenseEntity extends Model<AssetsExpenseEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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

    //删除标识(0:未删除,1:已删除)
    private String delFlag;

    //费用类型
    private String expenseType;

    //供应商
    private String supplier;

    //合同编号
    private String contractCode;

    //合同主体
    private String contractOrgId;

    //费用所属期
    private String expensePeriod;

    //资产转让标识
    private String assetsTransferFlag;

    //诉讼费/执行费金额
    private BigDecimal briefAndExecutionFee;

    //基础律师费
    private BigDecimal basicCounselFee;

    //风险律师服务费
    private BigDecimal riskCounselServiceFee;

    //回款金额
    private BigDecimal receivedAmount;

    //委案标的金额
    private BigDecimal entrustSubjectMatterAmount;

    //债转批次
    private String debtToBatchNo;

    //服务费费率
    private String serviceFeeRate;

    //[NULL]
    private BigDecimal serviceFeeAmount;

    //逾期天数
    private Integer overdueDays;

    //帐龄
    private String accountAge;

    //费率版本
    private String feeRateVersion;

    //运输距离
    private BigDecimal haulDistance;

    //运输费用
    private BigDecimal haulAmount;

    //整备费用
    private BigDecimal maintenanceAmount;

    //费用合计
    private BigDecimal expenseTotalAmount;

    //保管天数
    private Integer storageDays;

    //仓储保管服务费
    private BigDecimal storageSerivceFee;

    //评估费金额
    private BigDecimal assessAmount;

    // claim order detail表id
    private Long claimOrderDetailId;

    // 错误信息
    private String errsInfo;
}
