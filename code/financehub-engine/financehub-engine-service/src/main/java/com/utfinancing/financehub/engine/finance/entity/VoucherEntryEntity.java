package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.utfinancing.financehub.common.mybatis.handler.ListToVarcharTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 凭证分录表;实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-11
 */
@Data
@TableName(value = "eg_voucher_entry", autoResultMap = true)
public class VoucherEntryEntity extends Model<VoucherEntryEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //凭证ID
    private Long voucherId;

    //金额类型;refDict
    private String fundType;

    //是否银行账号相关(0:否 1是)
    private String relateBankFlag;

    //银行账号
    private String bankAccount;

    //现金流属性
    private String cashAttribute;

    //凭证摘要
    private String voucherSummary;

    //客户标识(0:否 1是)
    private String clientFlag;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //合同标识(0:否 1是)
    private String contractFlag;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //借贷方向
    private String debitCreditType;

    //财务账期(yyyyMM)
    private Integer accountPeriod;

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
//    @TableLogic
    private String delFlag;

    //借方发生额
    private BigDecimal debitAmount;

    //贷方发生额
    private BigDecimal creditAmount;

    private Integer periodCode;

    //借款合同编号
    private String billContractCode;

    //凭证行维度
    @TableField(typeHandler = ListToVarcharTypeHandler.class)
    private List<String> assistFlags;

    @ApiModelProperty("凭证行是否可以编辑，0：否，1：是")
    private String editFlag;

    @ApiModelProperty(value = "职员编码")
    private String employeeCode;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "借据号")
    private String receiptNumber;

    @ApiModelProperty(value = "衍生合约编号")
    private String derivativeContractNumber;

    @ApiModelProperty(value = "批次号")
    private String batchNumber;

    @ApiModelProperty(value = "辅助帐科目余额表ID")
    private Long accountAssistBalanceId;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    private String isRelatedOtherCustomer;

    @ApiModelProperty(value = "折本币借方发生额")
    private BigDecimal convertDebitAmount;

    @ApiModelProperty(value = "折本币贷方发生额")
    private BigDecimal convertCreditAmount;

    @ApiModelProperty(value = "原币金额")
    private BigDecimal voucherAmount;

    @ApiModelProperty(value = "是否发送金蝶（0：未发送，1：已发送）该字段只针对不汇总分录数据'")
    private String isSendKingdee;

    //金蝶凭证号
    private String easVoucherNumber;
}
