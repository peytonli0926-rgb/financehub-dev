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
@TableName(value = "T_BD_ACCOUNTVIEW", schema = "HXORACLE")
public class TBdAccountviewEntity extends Model<TBdAccountviewEntity> {

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

    @TableField("FISLEAF")
    private Long fisleaf;

    @TableField("FLEVEL")
    private Long flevel;

    @TableField("FLONGNUMBER")
    private String flongnumber;

    @TableField("FCREATORID")
    private String fcreatorid;

    @TableField("FCREATETIME")
    private LocalDateTime fcreatetime;

    @TableField("FLASTUPDATEUSERID")
    private String flastupdateuserid;

    @TableField("FLASTUPDATETIME")
    private LocalDateTime flastupdatetime;

    @TableField("FACCOUNTTABLEID")
    private String faccounttableid;

    @TableField("FACCOUNTTYPEID")
    private String faccounttypeid;

    @TableField("FLONGNAME_L1")
    private String flongnameL1;

    @TableField("FLONGNAME_L2")
    private String flongnameL2;

    @TableField("FLONGNAME_L3")
    private String flongnameL3;

    @TableField("FPARENTID")
    private String fparentid;

    @TableField("FISGFREEZE")
    private Long fisgfreeze;

    @TableField("FGAA")
    private String fgaa;

    @TableField("FISCFREEZE")
    private Long fiscfreeze;

    @TableField("FHELPCODE")
    private String fhelpcode;

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FCURRENCYID")
    private String fcurrencyid;

    @TableField("FDC")
    private Long fdc;

    @TableField("FISCHANGECURRENCY")
    private Long fischangecurrency;

    @TableField("FISCASHEQUIVALENT")
    private Long fiscashequivalent;

    @TableField("FISCOUNTACCRUAL")
    private Long fiscountaccrual;

    @TableField("FACCRUALPER")
    private BigDecimal faccrualper;

    @TableField("FISQTY")
    private Long fisqty;

    @TableField("FMEASUREUNITGROUPID")
    private String fmeasureunitgroupid;

    @TableField("FMEASUREUNITID")
    private String fmeasureunitid;

    @TableField("FAC")
    private Long fac;

    @TableField("FPLTYPE")
    private Long fpltype;

    @TableField("FCONTROL")
    private Long fcontrol;

    @TableField("FCAA")
    private String fcaa;

    @TableField("FACCTCURRENCY")
    private Long facctcurrency;

    @TableField("FISBANK")
    private Long fisbank;

    @TableField("FISCASH")
    private Long fiscash;

    @TableField("FHASUSERPROPERTY")
    private Long fhasuserproperty;

    @TableField("FACCOUNTID")
    private String faccountid;

    @TableField("FGLEVEL")
    private Long fglevel;

    @TableField("FISALLOWCA")
    private Long fisallowca;

    @TableField("FCONTROLUNITID")
    private String fcontrolunitid;

    @TableField("FUPPERID")
    private String fupperid;

    @TableField("FPARENTAAID")
    private String fparentaaid;

    @TableField("FISUPPERALLOWCA")
    private Long fisupperallowca;

    @TableField("FISSELFFREEZE")
    private Long fisselffreeze;

    @TableField("FISPARENTFREEZE")
    private Long fisparentfreeze;

    @TableField("FREFID")
    private String frefid;

    @TableField("FCONTROLLEVEL")
    private Long fcontrollevel;

    @TableField("FISOUTDAILYACCOUNT")
    private Long fisoutdailyaccount;

    @TableField("FDISPLAYNAME_L1")
    private String fdisplaynameL1;

    @TableField("FDISPLAYNAME_L2")
    private String fdisplaynameL2;

    @TableField("FDISPLAYNAME_L3")
    private String fdisplaynameL3;

    @TableField("FACNOTICE")
    private Long facnotice;

    @TableField("FBW")
    private Long fbw;

    @TableField("FMAINCASHFLOWITEMID")
    private String fmaincashflowitemid;

    @TableField("FATTCASHFLOWITEMID")
    private String fattcashflowitemid;

    @TableField("FBORROWERMAINCASHFLOWITEMID")
    private String fborrowermaincashflowitemid;

    @TableField("FBORROWERATTCASHFLOWITEMID")
    private String fborrowerattcashflowitemid;

    @TableField("FLENDERMAINCASHFLOWITEMID")
    private String flendermaincashflowitemid;

    @TableField("FLENDERATTCASHFLOWITEMID")
    private String flenderattcashflowitemid;

    @TableField("FISCONTROL")
    private Long fiscontrol;

    @TableField("FNAMEPINYIN")
    private String fnamepinyin;

    @TableField("FNAMESHORTPINYIN")
    private String fnameshortpinyin;

    @TableField("FACCRUALDIRECTION")
    private Long faccrualdirection;

    @TableField("FISBIZCHANGECURRENCY")
    private Long fisbizchangecurrency;

    @TableField("FISPROFITCENTER")
    private Long fisprofitcenter;

    @TableField("FCATEGORY")
    private Long fcategory;

    @TableField("FDIFFTYPE")
    private Long fdifftype;


}
