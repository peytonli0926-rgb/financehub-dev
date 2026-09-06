package com.utfinancing.financehub.engine.claim.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 资产费用对接表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-09-11
 */
@Data
public class AssetsExpenseExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    //费用类型
    @Excel(name = "费用类型")
    private String expenseType;

    //供应商
    @Excel(name = "供应商")
    private String supplier;

    //合同编号
    @Excel(name = "合同号")
    private String contractCode;

    //合同主体
    @Excel(name = "合同主体")
    private String contractOrgId;

    //费用所属期
    @Excel(name = "费用所属期")
    private String expensePeriod;

    //资产转让标识
    @Excel(name = "资产转让标识")
    private String assetsTransferFlag;

    //诉讼费/执行费金额
    @Excel(name = "诉讼费/执行费金额")
    private BigDecimal briefAndExecutionFee;

    //基础律师费
    @Excel(name = "基础律师费")
    private BigDecimal basicCounselFee;

    //风险律师服务费
    @Excel(name = "风险律师服务费")
    private BigDecimal riskCounselServiceFee;

    //回款金额
    @Excel(name = "回款金额")
    private BigDecimal receivedAmount;

    //委案标的金额
    @Excel(name = "委案标的金额")
    private BigDecimal entrustSubjectMatterAmount;

    //债转批次
    @Excel(name = "债转批次")
    private String debtToBatchNo;

    //服务费费率
    @Excel(name = "服务费费率")
    private String serviceFeeRate;

    //[NULL]
    @Excel(name = "服务费金额")
    private BigDecimal serviceFeeAmount;

    //逾期天数
    @Excel(name = "逾期天数")
    private Integer overdueDays;

    //帐龄
    @Excel(name = "账龄")
    private String accountAge;

    //费率版本
    @Excel(name = "费率版本")
    private String feeRateVersion;

    //运输距离
    @Excel(name = "运输距离")
    private BigDecimal haulDistance;

    //运输费用
    @Excel(name = "运输费用")
    private BigDecimal haulAmount;

    //整备费用
    @Excel(name = "整备费用")
    private BigDecimal maintenanceAmount;

    //费用合计
    @Excel(name = "费用合计")
    private BigDecimal expenseTotalAmount;

    //保管天数
    @Excel(name = "保管天数")
    private Integer storageDays;

    //仓储保管服务费
    @Excel(name = "仓储保管服务费")
    private BigDecimal storageSerivceFee;

    //评估费金额
    @Excel(name = "评估费金额")
    private BigDecimal assessAmount;
}
