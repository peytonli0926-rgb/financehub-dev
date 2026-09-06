package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提VO对象
 * @Modified :
 */
@Data
public class ImpairmentProvisionExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "财务日期",dateFormat = "yyyy-MM-dd")
    private Date accountDate;

    @Excel(name = "减值类型")
    private String impairmentType;

    @Excel(name = "拨备合计", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionTotal;

    @Excel(name = "上月余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lastMonthBalance;

    @Excel(name = "本月计提", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthProvision;

    @Excel(name = "处理状态",readConverterExp="1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;


}
