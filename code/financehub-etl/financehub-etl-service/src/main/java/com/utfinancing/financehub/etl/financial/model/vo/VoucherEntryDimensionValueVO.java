package com.utfinancing.financehub.etl.financial.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-19
 * @Description : 凭证维度和值对应表VO对象
 * @Modified :
 */
@Data
public class VoucherEntryDimensionValueVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "维度:用|分隔")
    private String dim;

    @ApiModelProperty(value = "维度值:用|分隔")
    private String dimValue;

    @ApiModelProperty(value = "来源")
    private String sourceSystem;

}
