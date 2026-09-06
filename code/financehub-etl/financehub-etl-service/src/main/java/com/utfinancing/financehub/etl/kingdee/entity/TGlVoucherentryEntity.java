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
@TableName(value = "T_GL_VOUCHERENTRY", schema = "HXORACLE")
public class TGlVoucherentryEntity extends Model<TGlVoucherentryEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FID", type = IdType.ASSIGN_ID)
    private String fid;

    @TableField("FSEQ")
    private Long fseq;

    @TableField("FBILLID")
    private String fbillid;

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FPERIODID")
    private String fperiodid;

    @TableField("FACCOUNTID")
    private String faccountid;

    @TableField("FCAA")
    private String fcaa;

    @TableField("FCURRENCYID")
    private String fcurrencyid;

    @TableField("FMEASUREUNITID")
    private String fmeasureunitid;

    @TableField("FENTRYDC")
    private Long fentrydc;

    @TableField("FORIGINALAMOUNT")
    private BigDecimal foriginalamount;

    @TableField("FLOCALAMOUNT")
    private BigDecimal flocalamount;

    @TableField("FREPORTINGAMOUNT")
    private BigDecimal freportingamount;

    @TableField("FLOCALEXCHANGERATE")
    private BigDecimal flocalexchangerate;

    @TableField("FREPORTINGEXCHANGERATE")
    private BigDecimal freportingexchangerate;

    @TableField("FPRICE")
    private BigDecimal fprice;

    @TableField("FQUANTITY")
    private BigDecimal fquantity;

    @TableField("FSTANDARDQUANTITY")
    private BigDecimal fstandardquantity;

    @TableField("FDESCRIPTION")
    private String fdescription;

    @TableField("FISVERIFY")
    private Long fisverify;

    @TableField("FISCUSSENT")
    private Long fiscussent;

    @TableField("FISCHECK")
    private Long fischeck;

    @TableField("FCASHIERID")
    private String fcashierid;

    @TableField("FMACTRLAMOUNT")
    private BigDecimal fmactrlamount;

    @TableField("FCUSTOMERID")
    private String fcustomerid;

    @TableField("FMAHOLDAMOUNT")
    private BigDecimal fmaholdamount;

    @TableField("FISHAND")
    private Long fishand;

    @TableField("FPROFITCENTERID")
    private String fprofitcenterid;

    @TableField("FCHECKDATE")
    private LocalDateTime fcheckdate;

    @TableField("FASSGRP")
    private String fassgrp;

    @TableField("FPRIMARYITEMID")
    private String fprimaryitemid;

    @TableField("FSUPPLEMENTARYITEMID")
    private String fsupplementaryitemid;


}
