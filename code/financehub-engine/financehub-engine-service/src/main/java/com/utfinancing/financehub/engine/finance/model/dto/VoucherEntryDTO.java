package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-11
 * @Description : 凭证分录表;DTO对象
 * @Modified :
 */
@Data
public class VoucherEntryDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

    @ApiModelProperty(value = "金额类型;refDict")
    private String fundType;

    @ApiModelProperty(value = "是否银行账号相关(0:否 1是)")
    private String relateBankFlag;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "现金流属性")
    private String cashAttribute;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "客户标识(0:否 1是)")
    private String clientFlag;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同标识(0:否 1是)")
    private String contractFlag;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "借贷方向")
    private String debitCreditType;

    @ApiModelProperty(value = "财务账期(yyyyMM)")
    private Integer accountPeriod;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    @ApiModelProperty(value = "借方发生额")
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    private BigDecimal creditAmount;

    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    @ApiModelProperty(value = "凭证行维度")
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

    @ApiModelProperty(value = "是否发送金蝶（0：未发送，1：已发送）该字段只针对不汇总分录数据'")
    private String isSendKingdee;

}
