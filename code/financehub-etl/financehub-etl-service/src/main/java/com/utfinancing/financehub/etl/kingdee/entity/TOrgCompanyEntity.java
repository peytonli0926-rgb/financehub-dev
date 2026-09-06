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
@TableName(value = "T_ORG_COMPANY", schema = "HXORACLE")
public class TOrgCompanyEntity extends Model<TOrgCompanyEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FID", type = IdType.ASSIGN_ID)
    private String fid;

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

    @TableField("FISGROUPING")
    private Long fisgrouping;

    @TableField("FEFFECTDATE")
    private LocalDateTime feffectdate;

    @TableField("FINVALIDDATE")
    private LocalDateTime finvaliddate;

    @TableField("FISFREEZE")
    private Long fisfreeze;

    @TableField("FISCOMPANYORGUNIT")
    private Long fiscompanyorgunit;

    @TableField("FISADMINORGUNIT")
    private Long fisadminorgunit;

    @TableField("FISSALEORGUNIT")
    private Long fissaleorgunit;

    @TableField("FISPURCHASEORGUNIT")
    private Long fispurchaseorgunit;

    @TableField("FISSTORAGEORGUNIT")
    private Long fisstorageorgunit;

    @TableField("FISPROFITORGUNIT")
    private Long fisprofitorgunit;

    @TableField("FISCOSTORGUNIT")
    private Long fiscostorgunit;

    @TableField("FISCU")
    private Long fiscu;

    @TableField("FISUNION")
    private Long fisunion;

    @TableField("FISHRORGUNIT")
    private Long fishrorgunit;

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

    @TableField("FISLEAF")
    private Long fisleaf;

    @TableField("FLEVEL")
    private Long flevel;

    @TableField("FLONGNUMBER")
    private String flongnumber;

    @TableField("FPARENTID")
    private String fparentid;

    @TableField("FTAXNUMBER")
    private String ftaxnumber;

    @TableField("FISGROUP")
    private Long fisgroup;

    @TableField("FISONLYUNION")
    private Long fisonlyunion;

    @TableField("FINDUSTRY")
    private String findustry;

    @TableField("FBANKID")
    private String fbankid;

    @TableField("FREPORTCURRENCYID")
    private String freportcurrencyid;

    @TableField("FACCOUNTPERIODID")
    private String faccountperiodid;

    @TableField("FJURIDICALPERSONID")
    private String fjuridicalpersonid;

    @TableField("FACCOUNTTABLEID")
    private String faccounttableid;

    @TableField("FADDRESSID")
    private String faddressid;

    @TableField("FBASECURRENCYID")
    private String fbasecurrencyid;

    @TableField("FBASEEXGTABLEID")
    private String fbaseexgtableid;

    @TableField("FADJUSTEXGTABLEID")
    private String fadjustexgtableid;

    @TableField("FREPORTCONVERTMODE")
    private Long freportconvertmode;

    @TableField("FREPORTEXGTABLEID")
    private String freportexgtableid;

    @TableField("FISSEALUP")
    private Long fissealup;

    @TableField("FISBIZUNIT")
    private Long fisbizunit;

    @TableField("FISSTART")
    private Long fisstart;

    @TableField("FISOUSEALUP")
    private Long fisousealup;

    @TableField("FDISPLAYNAME_L1")
    private String fdisplaynameL1;

    @TableField("FDISPLAYNAME_L2")
    private String fdisplaynameL2;

    @TableField("FDISPLAYNAME_L3")
    private String fdisplaynameL3;

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FREGION")
    private String fregion;

    @TableField("FECONOMICTYPE")
    private Long feconomictype;

    @TableField("FREGISTEREDCAPITAL")
    private BigDecimal fregisteredcapital;

    @TableField("FSETUPDATE")
    private LocalDateTime fsetupdate;

    @TableField("FENDUPDATE")
    private LocalDateTime fendupdate;

    @TableField("FTERRITORY_L1")
    private String fterritoryL1;

    @TableField("FTERRITORY_L2")
    private String fterritoryL2;

    @TableField("FTERRITORY_L3")
    private String fterritoryL3;

    @TableField("FISCHURCHYARD")
    private Long fischurchyard;

    @TableField("FREGISTEREDCODE")
    private String fregisteredcode;

    @TableField("FPROPERTYSEALUPDATE")
    private LocalDateTime fpropertysealupdate;

    @TableField("FVERSIONNUMBER")
    private String fversionnumber;

    @TableField("FCODE")
    private String fcode;

    @TableField("FISASSISTANTORG")
    private Long fisassistantorg;

    @TableField("FMAINORGID")
    private String fmainorgid;

    @TableField("FACCOUNTSCHEMEID")
    private String faccountschemeid;

    @TableField("FISTRANSPORTORGUNIT")
    private Long fistransportorgunit;

    @TableField("FISQUALITYORGUNIT")
    private Long fisqualityorgunit;

    @TableField("FORGTYPESTR")
    private String forgtypestr;

    @TableField("FACTIVITYDATE")
    private LocalDateTime factivitydate;

    @TableField("FCONTACT")
    private String fcontact;

    @TableField("FINVOICENUMBER")
    private String finvoicenumber;

    @TableField("FINVOICECOUNT")
    private String finvoicecount;

    @TableField("FTAXTYPE")
    private Long ftaxtype;

    @TableField("FBANKNAME")
    private String fbankname;

    @TableField("FTAXCODE")
    private String ftaxcode;

    @TableField("FCONTACTPHONE")
    private String fcontactphone;

    @TableField("FBANKACCOUNT")
    private String fbankaccount;

    @TableField("FTAXADDRESS")
    private String ftaxaddress;

    @TableField("FAUTHORIZATIONCODE")
    private String fauthorizationcode;

    @TableField("FAUTHORIZATIONKEY")
    private String fauthorizationkey;

    @TableField("FTAXREGISTERNAME")
    private String ftaxregistername;

    @TableField("FTAXPAYERID")
    private String ftaxpayerid;


}
