package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description : 科目辅助帐余额表VO对象
 * @Modified :
 */
@Data
public class AccountBalanceSheetExcelExportDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "会计期间")
//    @Excel(name = "会计期间",width = 20)
    private Integer periodCode;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体",width = 20)
    private String orgName;

    @ApiModelProperty(value = "科目编码")
    @Excel(name = "科目编码",width = 20)
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    @Excel(name = "科目名称",width = 20)
    private String accountName;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "年初借方余额")
    @Excel(name = "年初借方余额", width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal yearBeginDebitBalance;

    @ApiModelProperty(value = "年初贷方余额")
    @Excel(name = "年初贷方余额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal yearBeginCreditBalance;

    @ApiModelProperty(value = "期初借方余额")
    @Excel(name = "期初借方余额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal monthBeginDebitBalance;

    @ApiModelProperty(value = "期初贷方余额")
    @Excel(name = "期初贷方余额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal monthBeginCreditBalance;

    @ApiModelProperty(value = "本期借方发生额")
    @Excel(name = "本期借方发生额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal monthDebitAmount;

    @ApiModelProperty(value = "本期贷方发生额")
    @Excel(name = "本期贷方发生额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal monthCreditAmount;

    @ApiModelProperty(value = "年累计借方发生额")
    @Excel(name = "年累计借方发生额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal yearDebitAmount;

    @ApiModelProperty(value = "年累计贷方发生额")
    @Excel(name = "年累计贷方发生额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal yearCreditAmount;

    @ApiModelProperty(value = "期末借方余额")
    @Excel(name = "期末借方余额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal monthEndDebitBalance;

    @ApiModelProperty(value = "期末贷方余额")
    @Excel(name = "期末贷方余额",width = 20, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal monthEndCreditBalance;

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

//    @ApiModelProperty(value = "合同编号")
//    private String contractCode;
    @ApiModelProperty(value = "签约主体")
    private String orgId;


}
