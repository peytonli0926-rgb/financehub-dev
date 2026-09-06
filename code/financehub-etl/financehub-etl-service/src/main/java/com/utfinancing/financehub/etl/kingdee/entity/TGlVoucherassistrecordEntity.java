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
@TableName(value = "T_GL_VOUCHERASSISTRECORD", schema = "HXORACLE")
public class TGlVoucherassistrecordEntity extends Model<TGlVoucherassistrecordEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FID", type = IdType.ASSIGN_ID)
    private String fid;

    @TableField("FSEQ")
    private Long fseq;

    @TableField("FBILLID")
    private String fbillid;

    @TableField("FENTRYID")
    private String fentryid;

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FPERIODID")
    private String fperiodid;

    @TableField("FACCOUNTID")
    private String faccountid;

    @TableField("FASSGRPID")
    private String fassgrpid;

    @TableField("FSETTLEMENTTYPEID")
    private String fsettlementtypeid;

    @TableField("FSETTLEMENTCODE")
    private String fsettlementcode;

    @TableField("FSTANDARDQUANTITY")
    private BigDecimal fstandardquantity;

    @TableField("FBIZNUMBER")
    private String fbiznumber;

    @TableField("FENDDATE")
    private LocalDateTime fenddate;

    @TableField("FCUSTOMERID")
    private String fcustomerid;

    @TableField("FSUPPLIERID")
    private String fsupplierid;

    @TableField("FORGUNITID")
    private String forgunitid;

    @TableField("FMATERIALID")
    private String fmaterialid;

    @TableField("FVERIFIEDCUSSENTID")
    private String fverifiedcussentid;

    @TableField("FISFULLPROP")
    private Long fisfullprop;

    @TableField("FREQSTATUS")
    private Long freqstatus;

    @TableField("FREQCHECKSTATUS")
    private Long freqcheckstatus;

    @TableField("FCHECKNUMBER")
    private String fchecknumber;

    @TableField("FINVOICENUMBER")
    private String finvoicenumber;

    @TableField("FTICKETNUMBER")
    private String fticketnumber;

    @TableField("FISVIERIFIED")
    private Long fisvierified;

    @TableField("FRECEIPTFORMID")
    private String freceiptformid;

    @TableField("FRECEIPTFORMENTRYID")
    private String freceiptformentryid;

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

    @TableField("FNUMBER")
    private String fnumber;

    @TableField("FHANDLERID")
    private String fhandlerid;

    @TableField("FHASEFFECTED")
    private Long fhaseffected;

    @TableField("FAUDITORID")
    private String fauditorid;

    @TableField("FSOURCEBILLID")
    private String fsourcebillid;

    @TableField("FSOURCEFUNCTION")
    private String fsourcefunction;

    @TableField("FMACTRLAMOUNT")
    private BigDecimal fmactrlamount;

    @TableField("FPRICE")
    private BigDecimal fprice;

    @TableField("FMEASUREUNITID")
    private String fmeasureunitid;

    @TableField("FMAHOLDAMOUNT")
    private BigDecimal fmaholdamount;

    @TableField("FFEETYPE")
    private Long ffeetype;

    @TableField("FASSISTPRICE")
    private BigDecimal fassistprice;

    @TableField("FASSISTQTY")
    private BigDecimal fassistqty;

    @TableField("FASSISTUNIT")
    private String fassistunit;

    @TableField("FBIZDATE")
    private LocalDateTime fbizdate;

    @TableField("FORIGINALAMOUNT")
    private BigDecimal foriginalamount;

    @TableField("FLOCALAMOUNT")
    private BigDecimal flocalamount;

    @TableField("FREPORTINGAMOUNT")
    private BigDecimal freportingamount;

    @TableField("FQUANTITY")
    private BigDecimal fquantity;

    @TableField("FDESCRIPTION")
    private String fdescription;

    @TableField("FPRIMARYITEMID")
    private String fprimaryitemid;

    @TableField("FSUPPLEMENTARYITEMID")
    private String fsupplementaryitemid;


}
