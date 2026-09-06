package com.utfinancing.financehub.etl.invoicing.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description :   TaxicOiInvoiceBackMiddle查询from对象
 * @Modified :
 */
@ApiModel("TaxicOiInvoiceBackMiddle查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaxicOiInvoiceBackMiddleQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String beiz;

    @ApiModelProperty(value = "")
    private String cankzd1;

    @ApiModelProperty(value = "")
    private String cankzd2;

    @ApiModelProperty(value = "")
    private String cankzd3;

    @ApiModelProperty(value = "")
    private String cankzd4;

    @ApiModelProperty(value = "")
    private String cankzd5;

    @ApiModelProperty(value = "")
    private String caozr;

    @ApiModelProperty(value = "")
    private LocalDateTime caozrq;

    @ApiModelProperty(value = "")
    private LocalDateTime chuangjrq;

    @ApiModelProperty(value = "")
    private String contractno;

    @ApiModelProperty(value = "")
    private String danjh;

    @ApiModelProperty(value = "")
    private Long duqzt;

    @ApiModelProperty(value = "")
    private String fapdm;

    @ApiModelProperty(value = "")
    private String fapdzpath;

    @ApiModelProperty(value = "")
    private String faphm;

    @ApiModelProperty(value = "")
    private String faplx;

    @ApiModelProperty(value = "")
    private BigDecimal hansjine;

    @ApiModelProperty(value = "")
    private LocalDateTime kaiprq;

    @ApiModelProperty(value = "")
    private String kaipxmm;

    @ApiModelProperty(value = "")
    private String pich;

    @ApiModelProperty(value = "")
    private String qic;

    @ApiModelProperty(value = "")
    private BigDecimal shuie;

    @ApiModelProperty(value = "")
    private String yewly;

    @ApiModelProperty(value = "")
    private String shangpmc;

    @ApiModelProperty(value = "备注2")
    private String beiz2;
}
