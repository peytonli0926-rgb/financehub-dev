package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description : 资产转让-内部调拨DTO对象
 * @Modified :
 */
@Data
public class InternalTransferGenerateDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "批次")
    private List<String> batchList;

    @ApiModelProperty(value = "财务日期", example = "2024-12-19")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date financeDate;


}
