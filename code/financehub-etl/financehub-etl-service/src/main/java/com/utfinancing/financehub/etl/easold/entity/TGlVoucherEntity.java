package com.utfinancing.financehub.etl.easold.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@TableName(value = "T_GL_VOUCHER", schema = "HXORACLE")
public class TGlVoucherEntity extends Model<TGlVoucherEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FID", type = IdType.ASSIGN_ID)
    private String fid;

    @TableField("FNUMBER")
    private String fnumber;

    @TableField("FBIZDATE")
    private LocalDateTime fbizdate;

    @TableField("FCREATORID")
    private String fcreatorid;

    @TableField("FCREATETIME")
    private LocalDateTime fcreatetime;

    @TableField("FLASTUPDATEUSERID")
    private String flastupdateuserid;

    @TableField("FLASTUPDATETIME")
    private LocalDateTime flastupdatetime;

    @TableField("FHANDLERID")
    private String fhandlerid;

    @TableField("FDESCRIPTION")
    private String fdescription;

    @TableField("FHASEFFECTED")
    private Long fhaseffected;

    @TableField("FAUDITORID")
    private String fauditorid;

    @TableField("FSOURCEBILLID")
    private String fsourcebillid;

    @TableField("FSOURCEFUNCTION")
    private String fsourcefunction;

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FPERIODID")
    private String fperiodid;

    @TableField("FVOUCHERTYPEID")
    private String fvouchertypeid;

    @TableField("FCASHIERID")
    private String fcashierid;

    @TableField("FPOSTERID")
    private String fposterid;

    @TableField("FCANCELLERID")
    private String fcancellerid;

    @TableField("FSOURCESYS")
    private Long fsourcesys;

    @TableField("FSOURCETYPE")
    private Long fsourcetype;

    @TableField("FBOOKEDDATE")
    private LocalDateTime fbookeddate;

    @TableField("FCASHFLOWFLAG")
    private Long fcashflowflag;

    @TableField("FISREVERSEVOUCHER")
    private Long fisreversevoucher;

    @TableField("FATTACHMENTS")
    private Long fattachments;

    @TableField("FENTRYCOUNT")
    private Long fentrycount;

    @TableField("FLOCALDEBITAMOUNT")
    private BigDecimal flocaldebitamount;

    @TableField("FLOCALCREDITAMOUNT")
    private BigDecimal flocalcreditamount;

    @TableField("FREPORTINGDEBITAMOUNT")
    private BigDecimal freportingdebitamount;

    @TableField("FREPORTINGCREDITAMOUNT")
    private BigDecimal freportingcreditamount;

    @TableField("FBIZSTATUS")
    private Long fbizstatus;

    @TableField("FCURRENCYID")
    private String fcurrencyid;

    @TableField("FHASREVERSED")
    private Long fhasreversed;

    @TableField("FBGSTATUS")
    private Long fbgstatus;

    @TableField("FHASCASHACCOUNT")
    private Long fhascashaccount;

    @TableField("FISAC")
    private Long fisac;

    @TableField("FCONTROLUNITID")
    private String fcontrolunitid;

    @TableField("FABSTRACT")
    private String fabstract;

    @TableField("FCALSUBMITBAL")
    private Long fcalsubmitbal;

    @TableField("FISACNOTICE")
    private Long fisacnotice;

    @TableField("FISCUSSENT")
    private Long fiscussent;

    @TableField("FISCHECK")
    private Long fischeck;

    @TableField("FISCLOSE")
    private Long fisclose;

    @TableField("FISACCOUNTCOPY")
    private Long fisaccountcopy;

    @TableField("FISMGCTRL")
    private Long fismgctrl;

    @TableField("FISAUDITBACK")
    private Long fisauditback;

    @TableField("FPLSPLITED")
    private Long fplsplited;

    @TableField("FFILESTATE")
    private Long ffilestate;

    @TableField("FCATEGORY")
    private Long fcategory;

    @TableField("FRELATEDVOUCHERID")
    private String frelatedvoucherid;

    @TableField("FRELATEDVOUCHERNUMBER")
    private String frelatedvouchernumber;

    @TableField("FDIFFITEMFLAG")
    private Long fdiffitemflag;

    @TableField("CFBXBILLID")
    private String cfbxbillid;

    @TableField("CFBXMAINID")
    private String cfbxmainid;


}
