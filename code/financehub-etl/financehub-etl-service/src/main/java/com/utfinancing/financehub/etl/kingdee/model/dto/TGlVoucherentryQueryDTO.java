package com.utfinancing.financehub.etl.kingdee.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :   TGlVoucherentry查询from对象
 * @Modified :
 */
@ApiModel("TGlVoucherentry查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TGlVoucherentryQueryDTO extends BaseQueryDTO{

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
