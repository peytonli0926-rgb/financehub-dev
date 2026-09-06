package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : 应交增值税VO对象
 * @Modified :
 */
@Data
public class PayVatCheckDataVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "执行时间")
    private LocalDateTime executeDate;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "业务系统金额")
    private BigDecimal amount;

}
