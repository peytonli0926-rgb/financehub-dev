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
public class TGlVoucherDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private String fnumber;

    @ApiModelProperty(value = "")
    private LocalDateTime fbizdate;

    @ApiModelProperty(value = "")
    private String fcreatorid;

    @ApiModelProperty(value = "")
    private LocalDateTime fcreatetime;

    @ApiModelProperty(value = "")
    private String flastupdateuserid;

    @ApiModelProperty(value = "")
    private LocalDateTime flastupdatetime;

    @ApiModelProperty(value = "")
    private String fhandlerid;

    @ApiModelProperty(value = "")
    private String fdescription;

    @ApiModelProperty(value = "")
    private Long fhaseffected;

    @ApiModelProperty(value = "")
    private String fauditorid;

    @ApiModelProperty(value = "")
    private String fsourcebillid;

    @ApiModelProperty(value = "")
    private String fsourcefunction;

    @ApiModelProperty(value = "")
    private String fcompanyid;

    @ApiModelProperty(value = "")
    private String fperiodid;

    @ApiModelProperty(value = "")
    private String fvouchertypeid;

    @ApiModelProperty(value = "")
    private String fcashierid;

    @ApiModelProperty(value = "")
    private String fposterid;

    @ApiModelProperty(value = "")
    private String fcancellerid;

    @ApiModelProperty(value = "")
    private Long fsourcesys;

    @ApiModelProperty(value = "")
    private Long fsourcetype;

    @ApiModelProperty(value = "")
    private LocalDateTime fbookeddate;

    @ApiModelProperty(value = "")
    private Long fcashflowflag;

    @ApiModelProperty(value = "")
    private Long fisreversevoucher;

    @ApiModelProperty(value = "")
    private Long fattachments;

    @ApiModelProperty(value = "")
    private Long fentrycount;

    @ApiModelProperty(value = "")
    private BigDecimal flocaldebitamount;

    @ApiModelProperty(value = "")
    private BigDecimal flocalcreditamount;

    @ApiModelProperty(value = "")
    private BigDecimal freportingdebitamount;

    @ApiModelProperty(value = "")
    private BigDecimal freportingcreditamount;

    @ApiModelProperty(value = "")
    private Long fbizstatus;

    @ApiModelProperty(value = "")
    private String fcurrencyid;

    @ApiModelProperty(value = "")
    private Long fhasreversed;

    @ApiModelProperty(value = "")
    private Long fbgstatus;

    @ApiModelProperty(value = "")
    private Long fhascashaccount;

    @ApiModelProperty(value = "")
    private Long fisac;

    @ApiModelProperty(value = "")
    private String fcontrolunitid;

    @ApiModelProperty(value = "")
    private String fabstract;

    @ApiModelProperty(value = "")
    private Long fcalsubmitbal;

    @ApiModelProperty(value = "")
    private Long fisacnotice;

    @ApiModelProperty(value = "")
    private Long fiscussent;

    @ApiModelProperty(value = "")
    private Long fischeck;

    @ApiModelProperty(value = "")
    private Long fisclose;

    @ApiModelProperty(value = "")
    private Long fisaccountcopy;

    @ApiModelProperty(value = "")
    private Long fismgctrl;

    @ApiModelProperty(value = "")
    private Long fisauditback;

    @ApiModelProperty(value = "")
    private Long fplsplited;

    @ApiModelProperty(value = "")
    private Long ffilestate;

    @ApiModelProperty(value = "")
    private Long fcategory;

    @ApiModelProperty(value = "")
    private String frelatedvoucherid;

    @ApiModelProperty(value = "")
    private String frelatedvouchernumber;

    @ApiModelProperty(value = "")
    private Long fdiffitemflag;

    @ApiModelProperty(value = "")
    private String cfbxbillid;

    @ApiModelProperty(value = "")
    private String cfbxmainid;

}
