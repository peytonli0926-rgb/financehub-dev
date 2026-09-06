package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : VO对象
 * @Modified :
 */
@Data
public class ReceiveTaxExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "签约主体")
    private String sellerName;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "是否开票")
    private String isInvoiced;

    @Excel(name = "开票/计提名称")
    private String productName;

    @Excel(name = "发票号码")
    private String invoiceNumber;

    @Excel(name = "税额")
    private BigDecimal taxValue;

    @Excel(name = "税率")
    private BigDecimal taxRate;

    @Excel(name = "开票明细税额")
    private BigDecimal invoiceDetailTax;

    @Excel(name = "差异情况")
    private String differenceSituation;

    @Excel(name = "创建时间")
    private LocalDateTime createTime;

}
