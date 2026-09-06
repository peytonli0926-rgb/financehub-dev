package com.utfinancing.financehub.etl.kingdee.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : VO对象
 * @Modified :
 */
@Data
public class TBdAccountbanksVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private String fcreatorid;

    @ApiModelProperty(value = "")
    private LocalDateTime fcreatetime;

    @ApiModelProperty(value = "")
    private String flastupdateuserid;

    @ApiModelProperty(value = "")
    private LocalDateTime flastupdatetime;

    @ApiModelProperty(value = "")
    private String fcontrolunitid;

    @ApiModelProperty(value = "")
    private String fnameL1;

    @ApiModelProperty(value = "")
    private String fnameL2;

    @ApiModelProperty(value = "")
    private String fnameL3;

    @ApiModelProperty(value = "")
    private String fnumber;

    @ApiModelProperty(value = "")
    private String fdescriptionL1;

    @ApiModelProperty(value = "")
    private String fdescriptionL2;

    @ApiModelProperty(value = "")
    private String fdescriptionL3;

    @ApiModelProperty(value = "")
    private String fsimplename;

    @ApiModelProperty(value = "")
    private String fbankaccountnumber;

    @ApiModelProperty(value = "")
    private String fcompanyid;

    @ApiModelProperty(value = "")
    private Long fisclosed;

    @ApiModelProperty(value = "")
    private LocalDateTime fopendate;

    @ApiModelProperty(value = "")
    private LocalDateTime fclosedate;

    @ApiModelProperty(value = "")
    private String finitman;

    @ApiModelProperty(value = "")
    private String fbank;

    @ApiModelProperty(value = "")
    private String fphonenumber;

    @ApiModelProperty(value = "")
    private String faccountid;

    @ApiModelProperty(value = "")
    private Long fissetbankinterface;

    @ApiModelProperty(value = "")
    private Long fbankinterfacetype;

    @ApiModelProperty(value = "")
    private String fopenareaid;

    @ApiModelProperty(value = "")
    private Long fisusegrouppayment;

    @ApiModelProperty(value = "")
    private String fpropertyid;

    @ApiModelProperty(value = "")
    private String finneracctid;

    @ApiModelProperty(value = "")
    private String fctrlstrategyid;

    @ApiModelProperty(value = "")
    private String fclassificatioid;

    @ApiModelProperty(value = "")
    private Long fisbycurrency;

    @ApiModelProperty(value = "")
    private String fcurrencyid;

    @ApiModelProperty(value = "")
    private BigDecimal fmaxpayamount;

    @ApiModelProperty(value = "")
    private Long fisonlyread;

    @ApiModelProperty(value = "")
    private Long fismotheraccount;

    @ApiModelProperty(value = "")
    private Long faccounttype;

    @ApiModelProperty(value = "")
    private Long fnotoutpay;

    @ApiModelProperty(value = "")
    private String fsubaccountid;

    @ApiModelProperty(value = "")
    private Long fiscash;

    @ApiModelProperty(value = "")
    private Long fisbank;

    @ApiModelProperty(value = "")
    private String frelemotheracctid;

    @ApiModelProperty(value = "")
    private String facctname;

    @ApiModelProperty(value = "")
    private Long fisdcpay;

    @ApiModelProperty(value = "")
    private Long fisreckoning;

    @ApiModelProperty(value = "")
    private String fsimplecode;

    @ApiModelProperty(value = "")
    private String fagencycompanyid;

    @ApiModelProperty(value = "")
    private Long fisdefaultreck;

    @ApiModelProperty(value = "")
    private String fbankversion;

    @ApiModelProperty(value = "")
    private String fbankcert;

    @ApiModelProperty(value = "")
    private String fapplybillid;

    @ApiModelProperty(value = "")
    private String freference;

    @ApiModelProperty(value = "")
    private Long fisvirtualacct;

    @ApiModelProperty(value = "")
    private String fcountryid;

    @ApiModelProperty(value = "")
    private String fbankinterface;

    @ApiModelProperty(value = "")
    private String fbankinterfaceid;

    @ApiModelProperty(value = "")
    private String fsyncstatus;

    @ApiModelProperty(value = "")
    private String fsyncstatusmsg;

    @ApiModelProperty(value = "")
    private Long fisforedrafonly;

    @ApiModelProperty(value = "")
    private String fshareacctid;

    @ApiModelProperty(value = "")
    private Long fsharetype;

    @ApiModelProperty(value = "")
    private Long fisdefaultpayment;

    @ApiModelProperty(value = "")
    private Long fisereceipt;

    @ApiModelProperty(value = "")
    private Long faccountbanktype;

    @ApiModelProperty(value = "")
    private BigDecimal facctmanageamount;

    @ApiModelProperty(value = "")
    private String cffabankcountry;

    @ApiModelProperty(value = "")
    private String cffabankswiftcode;

    @ApiModelProperty(value = "")
    private String cffabankopenplace;

    @ApiModelProperty(value = "")
    private String cffabankreason;

    @ApiModelProperty(value = "")
    private String cffabanknetsilver;

    @ApiModelProperty(value = "")
    private String cffabankcanbesold;

    @ApiModelProperty(value = "")
    private Long fisdynamicnotice;

    @ApiModelProperty(value = "")
    private Long fissinglebalinterface;

}
