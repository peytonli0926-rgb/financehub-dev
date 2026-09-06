package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 导入对象
 * @Modified :
 */
@Data
public class ImportContractBalanceExcel1 implements Serializable{
    private static final long serialVersionUID = 1L;


    @Excel(name = "公司", type = Excel.Type.IMPORT)
    private String orgId;

    @Excel(name = "合同", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "入库时应收租金", type = Excel.Type.IMPORT)
    private BigDecimal receivableLeaseBalance;
    @Excel(name = "入库时应收残值", type = Excel.Type.IMPORT)
    private BigDecimal residualBalance;
    @Excel(name = "入库时应收销项税", type = Excel.Type.IMPORT)
    private BigDecimal receivableOuttaxBalance;
    @Excel(name = "入库时未实现收益", type = Excel.Type.IMPORT)
    private BigDecimal unrealizedRevenueBalance;
    @Excel(name = "入库时承租人保证金", type = Excel.Type.IMPORT)
    private BigDecimal receivableMarginBalance;
    @Excel(name = "入库时回收设备成本", type = Excel.Type.IMPORT)
    private BigDecimal receiveCost;
    @Excel(name = "入库时计提减值", type = Excel.Type.IMPORT)
    private BigDecimal provisionBalance;


}
