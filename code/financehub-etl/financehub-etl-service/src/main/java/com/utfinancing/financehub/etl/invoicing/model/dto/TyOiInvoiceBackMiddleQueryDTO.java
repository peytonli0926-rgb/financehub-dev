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
 * @Description :   TyOiInvoiceBackMiddle查询from对象
 * @Modified :
 */
@ApiModel("TyOiInvoiceBackMiddle查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TyOiInvoiceBackMiddleQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String danjh;

    @ApiModelProperty(value = "")
    private String contractno;

    @ApiModelProperty(value = "")
    private String fapdm;

    @ApiModelProperty(value = "")
    private String faphm;

    @ApiModelProperty(value = "票据类型(收据:222;专票:004;普票:007;数电专票:008;数电普票:009)")
    private String faplx;

    @ApiModelProperty(value = "")
    private BigDecimal hansjine;

    @ApiModelProperty(value = "")
    private BigDecimal jine;

    @ApiModelProperty(value = "")
    private BigDecimal shuie;

    @ApiModelProperty(value = "")
    private String caozlx;

    @ApiModelProperty(value = "")
    private String yuanfpdm;

    @ApiModelProperty(value = "")
    private String yuanfphm;

    @ApiModelProperty(value = "")
    private LocalDateTime kaiprq;

    @ApiModelProperty(value = "")
    private String kaipxmm;

    @ApiModelProperty(value = "")
    private String shangpmc;

    @ApiModelProperty(value = "")
    private String goufsh;

    @ApiModelProperty(value = "")
    private String goufmc;

    @ApiModelProperty(value = "")
    private String pich;

    @ApiModelProperty(value = "")
    private String qic;

    @ApiModelProperty(value = "")
    private String beiz;

    @ApiModelProperty(value = "")
    private String fapdzpath;

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
    private String yewly;

    @ApiModelProperty(value = "")
    private Long duqzt;

    @ApiModelProperty(value = "")
    private String duqsbyy;

    @ApiModelProperty(value = "")
    private String chuangjr;

    @ApiModelProperty(value = "")
    private LocalDateTime chuangjrq;

    @ApiModelProperty(value = "")
    private String gengxr;

    @ApiModelProperty(value = "")
    private LocalDateTime gengxrq;

    @ApiModelProperty(value = "xml路径")
    private String xmlpath;

    @ApiModelProperty(value = "备注2")
    private String beiz2;
}
