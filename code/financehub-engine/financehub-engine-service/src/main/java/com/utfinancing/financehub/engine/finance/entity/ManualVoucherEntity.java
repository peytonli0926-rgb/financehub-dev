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
 * 手工凭证表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_manual_voucher")
public class ManualVoucherEntity extends Model<ManualVoucherEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编码
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //签约主体
    private String orgId;

    //会计期间（yyyyMM）
    private Integer periodCode;

    //业务日期
    private LocalDateTime businessDate;

    //记账日期(财务日期)
    private LocalDateTime voucherDate;

    //凭证类型
    private String voucherType;

    //摘要内容
    private String voucherSummary;

    //业务场景编码
    private String sceneCode;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //币种编码
    private String currencyCode;

    //汇率
    private String rate;

    //借方发生额
    private BigDecimal debitAmount;

    //贷方发生额
    private BigDecimal creditAmount;

    //是否有现金流量（0：否，1：是）
    private String isCashFlow;

    //现金流量标记
    private String cashFlowMarker;

    //辅助帐摘要
    private String subsidiaryAccount;

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

    //是否删除（0：否，1：是）
    @TableLogic
    private String delFlag = "0";

    //手工Id
    private Long manualId;

    //借款合同编号
    private String loansContractCode;

    //成本中心
    private String costCentre;

    //员工姓名
    private String employeeName;

    //费用类型
    private String expenseType;

    //金融机构
    private String financialInstitution;

    //批次号
    private String batchNum;

    //银行账号
    private String bankNo;

    //制单人姓名
    private String preparerName;

    //物料（合同号）
    private String materialContractCode;

    //借据号
    private String receiptNum;

    //衍生合约编号
    private String derivativeContractCode;

    //项目类型（开发项目）
    private String projectType;

    //借款合同名称
    private String loansContractCodeName;

    //成本中心名称
    private String costCentreName;

    //员工编码
    private String employeeCode;

    //费用类型名称
    private String expenseTypeName;

    //金融机构名称
    private String financialInstitutionName;

    //银行账号名称
    private String bankNoName;

    //物料（合同名称）
    private String materialContractName;

    //项目类型名称
    private String projectTypeName;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    private String isRelatedOtherCustomer;


}
