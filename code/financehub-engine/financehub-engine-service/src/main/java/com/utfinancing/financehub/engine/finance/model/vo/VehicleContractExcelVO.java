package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/** 乘用车单合同查询导出模型。 */
@Data
public class VehicleContractExcelVO {
    @Excel(name = "签约主体", width = 20)
    private String orgId;
    @Excel(name = "合同编码", width = 24)
    private String contractCode;
    @Excel(name = "客户名称", width = 24)
    private String customerName;
    @Excel(name = "系统来源", width = 20)
    private String sourceSystem;
    @Excel(name = "业务板块", width = 16)
    private String businessLine;
    @Excel(name = "租赁类型", width = 16)
    private String leaseType;
    @Excel(name = "租赁方式", width = 16)
    private String leaseMethod;
    @Excel(name = "融资金额（元）", width = 18, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal financeAmount;
    @Excel(name = "合同金额（元）", width = 18, cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal contractAmount;
    @Excel(name = "车架号/VIN", width = 24)
    private String vin;
    @Excel(name = "品牌", width = 16)
    private String brand;
    @Excel(name = "车型", width = 20)
    private String model;
    @Excel(name = "渠道名称", width = 24)
    private String dealerName;
    @Excel(name = "合同状态", width = 14)
    private String contractStatus;
    @Excel(name = "合同签订日", width = 16, dateFormat = "yyyy-MM-dd")
    private LocalDate contractSignDate;
    @Excel(name = "起租日", width = 16, dateFormat = "yyyy-MM-dd")
    private LocalDate leaseStartDate;
    @Excel(name = "到期日", width = 16, dateFormat = "yyyy-MM-dd")
    private LocalDate maturityDate;
}
