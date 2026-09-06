package com.utfinancing.financehub.etl.invoicing.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-09
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class TyOiInvoiceMiddleDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "")
    private String danjh;

    @ApiModelProperty(value = "票据类型(收据:222;专票:004;普票:007;数电专票:008;数电普票:009)")
    private String faplx;

    @ApiModelProperty(value = "")
    private String contractno;

    @ApiModelProperty(value = "")
    private String fugr;

    @ApiModelProperty(value = "")
    private String kehhy;

    @ApiModelProperty(value = "")
    private String contractzt;

    @ApiModelProperty(value = "")
    private String yuxkaipstate;

    @ApiModelProperty(value = "")
    private String quanjnjstate;

    @ApiModelProperty(value = "")
    private String zbkpstate;

    @ApiModelProperty(value = "")
    private String goufnsrsbh;

    @ApiModelProperty(value = "")
    private String goufmc;

    @ApiModelProperty(value = "")
    private String goufdz;

    @ApiModelProperty(value = "")
    private String goufdh;

    @ApiModelProperty(value = "")
    private String goufyhmc;

    @ApiModelProperty(value = "")
    private String goufyhzh;

    @ApiModelProperty(value = "")
    private LocalDateTime shisrq;

    @ApiModelProperty(value = "")
    private String qishu;

    @ApiModelProperty(value = "")
    private String shangpbh;

    @ApiModelProperty(value = "")
    private String shangpmc;

    @ApiModelProperty(value = "")
    private String danw;

    @ApiModelProperty(value = "")
    private String shuil;

    @ApiModelProperty(value = "")
    private BigDecimal jiashj;

    @ApiModelProperty(value = "")
    private String shul;

    @ApiModelProperty(value = "")
    private BigDecimal jine;

    @ApiModelProperty(value = "")
    private BigDecimal shuie;

    @ApiModelProperty(value = "")
    private BigDecimal danj;

    @ApiModelProperty(value = "")
    private String beiz;

    @ApiModelProperty(value = "")
    private String keymemo;

    @ApiModelProperty(value = "")
    private String shuisflbm;

    @ApiModelProperty(value = "")
    private String pich;

    @ApiModelProperty(value = "")
    private LocalDateTime danjrq;

    @ApiModelProperty(value = "")
    private String guigxh;

    @ApiModelProperty(value = "")
    private Long jijfs;

    @ApiModelProperty(value = "")
    private String qiybh;

    @ApiModelProperty(value = "")
    private String xfsh;

    @ApiModelProperty(value = "")
    private String kpddm;

    @ApiModelProperty(value = "")
    private String shuisflbmbbh;

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
    private String cankzd6;

    @ApiModelProperty(value = "")
    private String cankzd7;

    @ApiModelProperty(value = "")
    private String cankzd8;

    @ApiModelProperty(value = "")
    private String cankzd9;

    @ApiModelProperty(value = "")
    private String cankzd10;

    @ApiModelProperty(value = "")
    private String yewlx;

    @ApiModelProperty(value = "")
    private Long duqzt;

    @ApiModelProperty(value = "")
    private String duqsbyy;

    @ApiModelProperty(value = "")
    private String yewcjr;

    @ApiModelProperty(value = "")
    private LocalDateTime yewcjrq;

    @ApiModelProperty(value = "")
    private String gengxr;

    @ApiModelProperty(value = "")
    private LocalDateTime gengxrq;

    @ApiModelProperty(value = "备注2")
    private String beiz2;

}
