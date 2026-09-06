package com.utfinancing.financehub.engine.verification.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.dto.VerificationDetailsExcelDTO</li>
 * <li>CreateTime : 2023/10/12 16:48</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "核销详情导出DTO")
@Data
public class VerificationDetailsExcelDTO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体",width = 20)
    private String orgName;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "记账日期")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记账日期",width = 20,cellType = Excel.ColumnType.DATE)
    private Date accountDate;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    @Excel(name = "应收期末残值",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收首付款")
    @Excel(name = "应收首付款",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableDownpayment;

    @ApiModelProperty(value = "应收手续费")
    @Excel(name = "应收手续费",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableCommission;

    @ApiModelProperty(value = "应收保险费")
    @Excel(name = "应收保险费",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableInsurance;

    @ApiModelProperty(value = "应收其他收入")
    @Excel(name = "应收其他收入",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOtherincome;

    @ApiModelProperty(value = "应收销项税")
    @Excel(name = "应收销项税",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益")
    @Excel(name = "未实现收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "应付设备款-暂估")
    @Excel(name = "应付设备款-暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableDeviceEstimate;

    @ApiModelProperty(value = "应付经销商服务费-暂估")
    @Excel(name = "应付经销商服务费-暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付收车费-暂估")
    @Excel(name = "应付收车费-暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估")
    @Excel(name = "应付手环成本_暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估")
    @Excel(name = "应付抵押费_暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应付解抵押费_暂估")
    @Excel(name = "应付解抵押费_暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估")
    @Excel(name = "应付其他租赁成本-暂估",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableOtherCostEstimate;

    @ApiModelProperty(value = "财务核销敞口")
    @Excel(name = "财务核销敞口",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "应收租赁款组合拨备")
    @Excel(name = "应收租赁款组合拨备",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depreciationReserves;

    @ApiModelProperty(value = "补提拨备")
    @Excel(name = "补提拨备",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal compensationProvisionAmount;
}
