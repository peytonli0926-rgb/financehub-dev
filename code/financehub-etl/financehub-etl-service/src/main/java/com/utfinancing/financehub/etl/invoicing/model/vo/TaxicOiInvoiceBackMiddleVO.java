package com.utfinancing.financehub.etl.invoicing.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : VO对象
 * @Modified :
 */
@Data
public class TaxicOiInvoiceBackMiddleVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

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
