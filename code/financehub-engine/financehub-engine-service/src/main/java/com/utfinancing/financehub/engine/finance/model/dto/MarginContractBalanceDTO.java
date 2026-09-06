package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description : 保证金合同余额表DTO对象
 * @Modified :
 */
@Data
public class MarginContractBalanceDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date balanceDate;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "财务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date financeDate;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "财务账期(yyyyMM)")
    private Integer accountPeriod;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机构编码")
    private String orgId;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "合同期末余额")
    private BigDecimal contractBalance;

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

    @ApiModelProperty(value = "是否一年内到期(0:否,1:是)")
    private String withinOneYear;

    @ApiModelProperty(value = "应付一年内到期保证金")
    private BigDecimal withinOneYearDeposit;

    @ApiModelProperty(value = "保证金利息支出")
    private BigDecimal depositInterestExpense;

    @ApiModelProperty(value = "保证金利息收入")
    private BigDecimal depositInterestIncome;

    @ApiModelProperty(value = "贷款利率")
    private BigDecimal lpr;

    @ApiModelProperty(value = "PV")
    private BigDecimal pv;

    @ApiModelProperty(value = "本金")
    private BigDecimal principalAmount;

    @ApiModelProperty(value = "本期进入PL")
    private BigDecimal currentEnterPl;

    @ApiModelProperty(value = "状态(0: 未录入,1: 已录入,2: 已提交,3: 复核通过,4: 复核失败,5: 已传至金蝶)")
    private String marginStatus;

}
