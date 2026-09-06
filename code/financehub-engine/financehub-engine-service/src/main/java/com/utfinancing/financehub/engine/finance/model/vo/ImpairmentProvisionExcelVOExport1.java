package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提导出清单 附件1：其他应收款_诉讼费保全费
 * @Modified :
 */
@Data
public class ImpairmentProvisionExcelVOExport1 implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "所属账套")
    private String orgName;

    @Excel(name = "科目代码")
    private String accountCode;

    @Excel(name = "科目名称")
    private String accountName;

    @Excel(name = "辅助账编码")
    private String contractCode;

    @Excel(name = "辅助项类别")
    private String assistFlags;

    @Excel(name = "客  户")
    private String clientName;

    @Excel(name = "币种")
    private String currencyName;

    @Excel(name = "金  额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal amount;

    @Excel(name = "付款/记账时间",dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    @Excel(name = "账龄")
    private Integer aging;

}
