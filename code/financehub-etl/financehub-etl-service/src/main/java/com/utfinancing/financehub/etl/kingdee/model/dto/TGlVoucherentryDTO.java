package com.utfinancing.financehub.etl.kingdee.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class TGlVoucherentryDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private Long fseq;

    @ApiModelProperty(value = "")
    private String fbillid;

    @ApiModelProperty(value = "")
    private String fcompanyid;

    @ApiModelProperty(value = "")
    private String fperiodid;

    @ApiModelProperty(value = "")
    private String faccountid;

    @ApiModelProperty(value = "")
    private String fcaa;

    @ApiModelProperty(value = "")
    private String fcurrencyid;

    @ApiModelProperty(value = "")
    private String fmeasureunitid;

    @ApiModelProperty(value = "")
    private Long fentrydc;

    @ApiModelProperty(value = "")
    private BigDecimal foriginalamount;

    @ApiModelProperty(value = "")
    private BigDecimal flocalamount;

    @ApiModelProperty(value = "")
    private BigDecimal freportingamount;

    @ApiModelProperty(value = "")
    private BigDecimal flocalexchangerate;

    @ApiModelProperty(value = "")
    private BigDecimal freportingexchangerate;

    @ApiModelProperty(value = "")
    private BigDecimal fprice;

    @ApiModelProperty(value = "")
    private BigDecimal fquantity;

    @ApiModelProperty(value = "")
    private BigDecimal fstandardquantity;

    @ApiModelProperty(value = "")
    private String fdescription;

    @ApiModelProperty(value = "")
    private Long fisverify;

    @ApiModelProperty(value = "")
    private Long fiscussent;

    @ApiModelProperty(value = "")
    private Long fischeck;

    @ApiModelProperty(value = "")
    private String fcashierid;

    @ApiModelProperty(value = "")
    private BigDecimal fmactrlamount;

    @ApiModelProperty(value = "")
    private String fcustomerid;

    @ApiModelProperty(value = "")
    private BigDecimal fmaholdamount;

    @ApiModelProperty(value = "")
    private Long fishand;

    @ApiModelProperty(value = "")
    private String fprofitcenterid;

    @ApiModelProperty(value = "")
    private LocalDateTime fcheckdate;

    @ApiModelProperty(value = "")
    private String fassgrp;

    @ApiModelProperty(value = "")
    private String fprimaryitemid;

    @ApiModelProperty(value = "")
    private String fsupplementaryitemid;

}
