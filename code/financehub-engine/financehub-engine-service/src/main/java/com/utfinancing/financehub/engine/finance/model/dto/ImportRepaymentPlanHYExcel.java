package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 导入对象
 * @Modified :
 */
@Data
public class ImportRepaymentPlanHYExcel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "签约主体编号", type = Excel.Type.IMPORT)
    private String orgId;

    @Excel(name = "合同编号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "客户编号", type = Excel.Type.IMPORT)
    private String clientCode;

    @Excel(name = "客户名称", type = Excel.Type.IMPORT)
    private String clientName;

    @Excel(name = "计划还款日", type = Excel.Type.IMPORT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planDate;

    @Excel(name = "归还租金_含税额", type = Excel.Type.IMPORT)
    private BigDecimal rentAmount;
    @Excel(name = "归还本金_含税额", type = Excel.Type.IMPORT)
    private BigDecimal principalAmount;
    @Excel(name = "归还利息_含税额", type = Excel.Type.IMPORT)
    private BigDecimal interestAmount;
    @Excel(name = "回笼状态", type = Excel.Type.IMPORT)
    private String recaptureStatus;

    @Excel(name = "IRR", type = Excel.Type.IMPORT)
    private String comment;

    // 期数
    private Integer periods;
    //XIRR
    private BigDecimal xirrRate;
    private String messageId;
    private String systemCode;


}
