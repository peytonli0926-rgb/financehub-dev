package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提导出清单 附件2：其他应收款项
 * @Modified :
 */
@Data
public class ImpairmentProvisionExcelVOExport2 implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "主体")
    private String orgName;

    @Excel(name = "合同号")
    private String contractCode;

    @Excel(name = "应收账款客户")
    private String clientName;

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
