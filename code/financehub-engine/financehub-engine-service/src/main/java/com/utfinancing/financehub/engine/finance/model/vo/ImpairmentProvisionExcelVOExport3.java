package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提导出清单 附件3：应收投资性房地产
 * @Modified :
 */
@Data
public class ImpairmentProvisionExcelVOExport3 implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "系统报表")
    private String systemReport;

    @Excel(name = "合同号")
    private String contractCode;

    @Excel(name = "核算项目名称")
    private String contractName;

    @Excel(name = "会计年度")
    private String periodYear;

    @Excel(name = "期间")
    private String periodMonth;

    @Excel(name = "科目代码")
    private String accountCode;

    @Excel(name = "科目名称")
    private String accountName;

    @Excel(name = "金  额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal amount;

    @Excel(name = "确认日期",dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    @Excel(name = "账龄")
    private Integer aging;

    @Excel(name = "账龄段")
    private String agingGroup;

    @Excel(name = "第一笔应收日期",dateFormat = "yyyy-MM-dd")
    private Date planDate;


}
