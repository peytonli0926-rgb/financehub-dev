package com.utfinancing.financehub.etl.kingdee.entity;

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
@TableName(value = "T_BD_ASSTACCOUNT", schema = "HXORACLE")
public class TBdAsstaccountEntity extends Model<TBdAsstaccountEntity> {

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

    @TableField("FISCOMMON")
    private Long fiscommon;

    @TableField("FISCOMPANY")
    private Long fiscompany;

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

    @TableField("FCOMPANYID")
    private String fcompanyid;

    @TableField("FISACTCUSSENT")
    private Long fisactcussent;

    @TableField("FCOUNT")
    private Long fcount;

    @TableField("FCONTROLUNITID")
    private String fcontrolunitid;

    @TableField("FISQTY")
    private Long fisqty;

    @TableField("FMEASUREUNITID")
    private String fmeasureunitid;

    @TableField("FMEASUREUNITGROUPID")
    private String fmeasureunitgroupid;


}
