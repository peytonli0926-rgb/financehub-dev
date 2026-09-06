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
 * @Description :   TaxicIiInvoiceMiddle查询from对象
 * @Modified :
 */
@ApiModel("TaxicIiInvoiceMiddle查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaxicIiInvoiceMiddleQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String cank1;

    @ApiModelProperty(value = "")
    private String cank10;

    @ApiModelProperty(value = "")
    private String cank2;

    @ApiModelProperty(value = "")
    private String cank3;

    @ApiModelProperty(value = "")
    private String cank4;

    @ApiModelProperty(value = "")
    private String cank5;

    @ApiModelProperty(value = "")
    private String cank6;

    @ApiModelProperty(value = "")
    private String cank7;

    @ApiModelProperty(value = "")
    private String cank8;

    @ApiModelProperty(value = "")
    private String cank9;

    @ApiModelProperty(value = "")
    private LocalDateTime caozrq;

    @ApiModelProperty(value = "")
    private String contractno;

    @ApiModelProperty(value = "")
    private Long duqzt;

    @ApiModelProperty(value = "")
    private LocalDateTime editimgrq;

    @ApiModelProperty(value = "")
    private String fapdm;

    @ApiModelProperty(value = "")
    private String faphm;

    @ApiModelProperty(value = "")
    private String faplx;

    @ApiModelProperty(value = "")
    private String goufnsrsbh;

    @ApiModelProperty(value = "")
    private String hejjine;

    @ApiModelProperty(value = "")
    private String imagepath;

    @ApiModelProperty(value = "")
    private String isdelete;

    @ApiModelProperty(value = "")
    private LocalDateTime kaiprq;

    @ApiModelProperty(value = "")
    private String message;

    @ApiModelProperty(value = "")
    private BigDecimal notaxjine;

    @ApiModelProperty(value = "")
    private String qiybh;

    @ApiModelProperty(value = "")
    private LocalDateTime scimgrq;

    @ApiModelProperty(value = "")
    private String shuie;

    @ApiModelProperty(value = "")
    private BigDecimal taxjine;

    @ApiModelProperty(value = "")
    private Long yewly;

    @ApiModelProperty(value = "")
    private LocalDateTime ywxyeditrq;

    @ApiModelProperty(value = "")
    private String xiaofsbh;

    @ApiModelProperty(value = "")
    private String faplydzxz;

    @ApiModelProperty(value = "0-Normal 1-OFD")
    private String isofd;
}
