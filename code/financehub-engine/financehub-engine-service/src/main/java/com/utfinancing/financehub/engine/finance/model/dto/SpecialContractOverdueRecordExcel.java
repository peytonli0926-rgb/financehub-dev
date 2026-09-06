package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : robjiang
 * @Date : Create in 2025-11-21
 * @Description : 特殊合同逾期上传记录VO对象
 * @Modified :
 */
@Data
public class SpecialContractOverdueRecordExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "计提月份", cellType = Excel.ColumnType.DATE, comment = "yyyy-MM-dd")
    private Date businessDate;

    @Excel(name = "合同编号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "上传账期", comment = "yyyy-MM")
    private String uploadPeriods;

    @Excel(name = "是否逾期", readConverterExp = "0=否,1=是", comment = "否/是")
    private String laborOverdueMark;

    @Excel(name = "上期实收期间", cellType = Excel.ColumnType.DATE)
    private Date previousPaidPeriod;

}
