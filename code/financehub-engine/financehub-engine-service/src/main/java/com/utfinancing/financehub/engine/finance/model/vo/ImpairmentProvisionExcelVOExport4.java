package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提导出清单 附件4：恒信应收蓬莱租赁清单
 * @Modified :
 */
@Data
public class ImpairmentProvisionExcelVOExport4 implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "物料编码")
    private String contractCode;

    @Excel(name = "物料名称")
    private String contractName;

    @Excel(name = "公司名称")
    private String clientName;

    @Excel(name = "应收租金", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentBalance;

    @Excel(name = "应收期末残值", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableResidualValueBalance;

    @Excel(name = "应收销项税", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttaxBalance;

    @Excel(name = "未实现融资租赁收益", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal unrealizedRevenueBalance;

    @Excel(name = "账面价值", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal boolValue;




}
