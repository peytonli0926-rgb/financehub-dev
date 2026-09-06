package com.utfinancing.financehub.etl.kingdee.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "T_BD_ACCOUNTBANKS", schema = "HXORACLE")
public class TBdAccountbanksEntity extends Model<TBdAccountbanksEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FID", type = IdType.ASSIGN_ID)
    private String fid;

    @TableField("FCREATORID")
    private String fcreatorid;

    @TableField("FCREATETIME")
    private LocalDateTime fcreatetime;

    @TableField("FLASTUPDATEUSERID")
    private String flastupdateuserid;

    @TableField("FLASTUPDATETIME")
    private LocalDateTime flastupdatetime;

    @TableField("FCONTROLUNITID")
    private String fcontrolunitid;

    @TableField("FNAME_L1")
    private String fnameL1;

    @TableField("FNAME_L2")
    private String fnameL2;

    @TableField("FNAME_L3")
    private String fnameL3;

    @TableField("FNUMBER")
    private String fnumber;

    @TableField("FDESCRIPTION_L1")
    private String fdescriptionL1;

    @TableField("FDESCRIPTION_L2")
    private String fdescriptionL2;

    @TableField("FDESCRIPTION_L3")
    private String fdescriptionL3;

    @TableField("FSIMPLENAME")
    private String fsimplename;

    @TableField("FBANKACCOUNTNUMBER")
    private String fbankaccountnumber;

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FISCLOSED")
    private Long fisclosed;

    @TableField("FOPENDATE")
    private LocalDateTime fopendate;

    @TableField("FCLOSEDATE")
    private LocalDateTime fclosedate;

    @TableField("FINITMAN")
    private String finitman;

    @TableField("FBANK")
    private String fbank;

    @TableField("FPHONENUMBER")
    private String fphonenumber;

    @TableField("FACCOUNTID")
    private String faccountid;

    @TableField("FISSETBANKINTERFACE")
    private Long fissetbankinterface;

    @TableField("FBANKINTERFACETYPE")
    private Long fbankinterfacetype;

    @TableField("FOPENAREAID")
    private String fopenareaid;

    @TableField("FISUSEGROUPPAYMENT")
    private Long fisusegrouppayment;

    @TableField("FPROPERTYID")
    private String fpropertyid;

    @TableField("FINNERACCTID")
    private String finneracctid;

    @TableField("FCTRLSTRATEGYID")
    private String fctrlstrategyid;

    @TableField("FCLASSIFICATIOID")
    private String fclassificatioid;

    @TableField("FISBYCURRENCY")
    private Long fisbycurrency;

    @TableField("FCURRENCYID")
    private String fcurrencyid;

    @TableField("FMAXPAYAMOUNT")
    private BigDecimal fmaxpayamount;

    @TableField("FISONLYREAD")
    private Long fisonlyread;

    @TableField("FISMOTHERACCOUNT")
    private Long fismotheraccount;

    @TableField("FACCOUNTTYPE")
    private Long faccounttype;

    @TableField("FNOTOUTPAY")
    private Long fnotoutpay;

    @TableField("FSUBACCOUNTID")
    private String fsubaccountid;

    @TableField("FISCASH")
    private Long fiscash;

    @TableField("FISBANK")
    private Long fisbank;

    @TableField("FRELEMOTHERACCTID")
    private String frelemotheracctid;

    @TableField("FACCTNAME")
    private String facctname;

    @TableField("FISDCPAY")
    private Long fisdcpay;

    @TableField("FISRECKONING")
    private Long fisreckoning;

    @TableField("FSIMPLECODE")
    private String fsimplecode;

    @TableField("FAGENCYCOMPANYID")
    private String fagencycompanyid;

    @TableField("FISDEFAULTRECK")
    private Long fisdefaultreck;

    @TableField("FBANKVERSION")
    private String fbankversion;

    @TableField("FBANKCERT")
    private String fbankcert;

    @TableField("FAPPLYBILLID")
    private String fapplybillid;

    @TableField("FREFERENCE")
    private String freference;

    @TableField("FISVIRTUALACCT")
    private Long fisvirtualacct;

    @TableField("FCOUNTRYID")
    private String fcountryid;

    @TableField("FBANKINTERFACE")
    private String fbankinterface;

    @TableField("FBANKINTERFACEID")
    private String fbankinterfaceid;

    @TableField("FSYNCSTATUS")
    private String fsyncstatus;

    @TableField("FSYNCSTATUSMSG")
    private String fsyncstatusmsg;

    @TableField("FISFOREDRAFONLY")
    private Long fisforedrafonly;

    @TableField("FSHAREACCTID")
    private String fshareacctid;

    @TableField("FSHARETYPE")
    private Long fsharetype;

    @TableField("FISDEFAULTPAYMENT")
    private Long fisdefaultpayment;

    @TableField("FISERECEIPT")
    private Long fisereceipt;

    @TableField("FACCOUNTBANKTYPE")
    private Long faccountbanktype;

    @TableField("FACCTMANAGEAMOUNT")
    private BigDecimal facctmanageamount;

    @TableField("CFFABANKCOUNTRY")
    private String cffabankcountry;

    @TableField("CFFABANKSWIFTCODE")
    private String cffabankswiftcode;

    @TableField("CFFABANKOPENPLACE")
    private String cffabankopenplace;

    @TableField("CFFABANKREASON")
    private String cffabankreason;

    @TableField("CFFABANKNETSILVER")
    private String cffabanknetsilver;

    @TableField("CFFABANKCANBESOLD")
    private String cffabankcanbesold;

    @TableField("FISDYNAMICNOTICE")
    private Long fisdynamicnotice;

    @TableField("FISSINGLEBALINTERFACE")
    private Long fissinglebalinterface;


}
