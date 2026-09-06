package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-17
 * @Description :   InternalTransfer查询from对象
 * @Modified :
 */
@ApiModel("InternalTransfer查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class InternalTransferQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "批次")
    private String batch;

    @ApiModelProperty(value = "支付日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;


}
