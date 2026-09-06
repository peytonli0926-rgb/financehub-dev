package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemDetailExcelVO</li>
 * <li>CreateTime : 2024/03/19 15:46</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("详情表导出VO")
@Data
public class AssetAbsRedeemDetailExcelVO {

    //借款合同编号
    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;
    //出表期数
    @ApiModelProperty(value = "出表期数")
    @Excel(name = "出表期数",width = 20)
    private String periods;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    //签约主体
    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "税率")
    @Excel(name = "税率",width = 20)
    private String rate;

    @ApiModelProperty(value = "赎回开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "赎回起算日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "实际赎回日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际赎回日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date actualDate;

    @ApiModelProperty(value = "赎回价格")
    @Excel(name = "赎回价格",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal redeemPrice;

    @ApiModelProperty(value = "赎回起算日租金余额")
    @Excel(name = "赎回起算日租金余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "赎回起算日本金余额")
    @Excel(name = "赎回起算日本金余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal principalBalance;

    @ApiModelProperty(value = "赎回起算日利息余额")
    @Excel(name = "赎回起算日利息余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal interestBalance;

    @ApiModelProperty(value = "赎回起算日留购价余额")
    @Excel(name = "赎回起算日留购价余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "赎回起算日保证金余额")
    @Excel(name = "赎回起算日保证金余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lesseeMarginBalance;
}
