package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description : 出售登记DTO 导入对象
 * @Modified :
 */
@Data
public class SellRegisterExcelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资产编号")
    @Excel(name = "资产编号")
    private String assetNumber;

    @ApiModelProperty(value = "转出时间")
    @Excel(name = "转出时间", dateFormat = "yyyy-MM-dd")
    private Date transferOutDate;

    @ApiModelProperty(value = "买售人")
    @Excel(name = "买售人")
    private String buyOrSellPerson;

    @ApiModelProperty(value = "售价")
    @Excel(name = "售价")
    private BigDecimal sellPrice;

}
