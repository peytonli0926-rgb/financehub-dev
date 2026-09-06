package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : 线下合同交易数据DTO对象
 * @Modified :
 */
@Data
public class OfflineContractStructureExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "主合同编号")
    private String contractCodeM;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "设备款", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal payableDevice;

    @Excel(name = "首付款", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableDownpayment;

    @Excel(name = "出租人保险费", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lessorInsurance;

    @Excel(name = "承租人履约保证金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lesseeMargin;

    @Excel(name = "渠道费用", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal channelFee;

    @Excel(name = "手续费收入(含增值税)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableCommission;

    @Excel(name = "出租人其它成本", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lessorOtherincome;

    @Excel(name = "厂商返利", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRebate;

    @Excel(name = "承租人保险费", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableInsurance;

    @Excel(name = "期末残值", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValue;

    @Excel(name = "其他收入 (含增值税)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOtherincome;

    @Excel(name = "咨询服务收入(含增值税)", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableService;

    @Excel(name = "供应商履约保证金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal supplierMargin;

}
