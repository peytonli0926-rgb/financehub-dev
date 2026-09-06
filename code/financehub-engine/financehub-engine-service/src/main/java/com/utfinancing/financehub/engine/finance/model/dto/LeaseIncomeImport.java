package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description : 上传分摊信息及此次计提相关合同的分摊方式
 * @Modified :
 */
@Data
public class LeaseIncomeImport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "计提月份", type = Excel.Type.IMPORT)
    private Date businessDate;

    @Excel(name = "合同编号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "是否计提", readConverterExp = "0=否,1=是", comment = "否/是")
    private String accrued;

    @Excel(name = "是否逾期", readConverterExp = "0=否,1=是", comment = "否/是")
    private String laborOverdueMark;
    @Excel(name = "是否观察期", readConverterExp = "0=否,1=是", comment = "否/是")
    private String observed;
//    @Excel(name = "观察期到期日", cellType = Excel.ColumnType.DATE)
//    private Date observedExpirationDate;

    @Excel(name = "计提方式", comment = "XIRR分摊收益/实收/IRR分摊收益")
    private String incomeProvisionMethod;
//    @Excel(name = "处理方式")
//    private String processMethod;

    @Excel(name = "备注")
    private String comment;
    @Excel(name = "上期实收期间", cellType = Excel.ColumnType.DATE)
    private Date previousPaidPeriod;

}
