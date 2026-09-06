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
@TableName(value = "T_BD_ASSISTANTHG", schema = "HXORACLE")
public class TBdAssistanthgEntity extends Model<TBdAssistanthgEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FID", type = IdType.ASSIGN_ID)
    private String fid;

    @TableField("FCOUNT")
    private Long fcount;

    @TableField("FLONGNAMEGROUP_L1")
    private String flongnamegroupL1;

    @TableField("FLONGNAMEGROUP_L2")
    private String flongnamegroupL2;

    @TableField("FLONGNAMEGROUP_L3")
    private String flongnamegroupL3;

    @TableField("FCREATORID")
    private String fcreatorid;

    @TableField("FCREATETIME")
    private LocalDateTime fcreatetime;

    @TableField("FLASTUPDATEUSERID")
    private String flastupdateuserid;

    @TableField("FLASTUPDATETIME")
    private LocalDateTime flastupdatetime;

    @TableField("FASSTACCOUNTID")
    private String fasstaccountid;

    @TableField("FMATERIALID")
    private String fmaterialid;

    @TableField("FCUSTOMERID")
    private String fcustomerid;

    @TableField("FPROVIDERID")
    private String fproviderid;

    @TableField("FPROJECTID")
    private String fprojectid;

    @TableField("FBANKACCOUNTID")
    private String fbankaccountid;

    @TableField("FINDUSTRYID")
    private String findustryid;

    @TableField("FREGIONID")
    private String fregionid;

    @TableField("FCASHFLOWITEMID")
    private String fcashflowitemid;

    @TableField("FCOUNTRYID")
    private String fcountryid;

    @TableField("FPROVINCEID")
    private String fprovinceid;

    @TableField("FCITYID")
    private String fcityid;

    @TableField("FACCOUNTCUSSENTID")
    private String faccountcussentid;

    @TableField("FCOSTOBJECTID")
    private String fcostobjectid;

    @TableField("FADMINORGID")
    private String fadminorgid;

    @TableField("FCOMPANYORGID")
    private String fcompanyorgid;

    @TableField("FSALEORGID")
    private String fsaleorgid;

    @TableField("FPURCHASEORGID")
    private String fpurchaseorgid;

    @TableField("FSTOREAGEORGID")
    private String fstoreageorgid;

    @TableField("FCOSTORGID")
    private String fcostorgid;

    @TableField("FPROFITORGID")
    private String fprofitorgid;

    @TableField("FPERSONID")
    private String fpersonid;

    @TableField("FCONTROLUNITID")
    private String fcontrolunitid;

    @TableField("FGENERALASSACTTYPE1ID")
    private String fgeneralassacttype1id;

    @TableField("FGENERALASSACTTYPE2ID")
    private String fgeneralassacttype2id;

    @TableField("FGENERALASSACTTYPE3ID")
    private String fgeneralassacttype3id;

    @TableField("FGENERALASSACTTYPE4ID")
    private String fgeneralassacttype4id;

    @TableField("FGENERALASSACTTYPE5ID")
    private String fgeneralassacttype5id;

    @TableField("FGENERALASSACTTYPE6ID")
    private String fgeneralassacttype6id;

    @TableField("FGENERALASSACTTYPE7ID")
    private String fgeneralassacttype7id;

    @TableField("FGENERALASSACTTYPE8ID")
    private String fgeneralassacttype8id;

    @TableField("FGENERALASSACTTYPE9ID")
    private String fgeneralassacttype9id;

    @TableField("FGENERALASSACTTYPE10ID")
    private String fgeneralassacttype10id;

    @TableField("FGENERALASSACTTYPE11ID")
    private String fgeneralassacttype11id;

    @TableField("FGENERALASSACTTYPE12ID")
    private String fgeneralassacttype12id;

    @TableField("FGENERALASSACTTYPE13ID")
    private String fgeneralassacttype13id;

    @TableField("FGENERALASSACTTYPE14ID")
    private String fgeneralassacttype14id;

    @TableField("FGENERALASSACTTYPE15ID")
    private String fgeneralassacttype15id;

    @TableField("FGENERALASSACTTYPE16ID")
    private String fgeneralassacttype16id;

    @TableField("FGENERALASSACTTYPE17ID")
    private String fgeneralassacttype17id;

    @TableField("FGENERALASSACTTYPE18ID")
    private String fgeneralassacttype18id;

    @TableField("FGENERALASSACTTYPE19ID")
    private String fgeneralassacttype19id;

    @TableField("FGENERALASSACTTYPE20ID")
    private String fgeneralassacttype20id;

    @TableField("FGENERALASSACTTYPE21ID")
    private String fgeneralassacttype21id;

    @TableField("FGENERALASSACTTYPE22ID")
    private String fgeneralassacttype22id;

    @TableField("FGENERALASSACTTYPE23ID")
    private String fgeneralassacttype23id;

    @TableField("FGENERALASSACTTYPE24ID")
    private String fgeneralassacttype24id;

    @TableField("FGENERALASSACTTYPE25ID")
    private String fgeneralassacttype25id;

    @TableField("FGENERALASSACTTYPE26ID")
    private String fgeneralassacttype26id;

    @TableField("FGENERALASSACTTYPE27ID")
    private String fgeneralassacttype27id;

    @TableField("FGENERALASSACTTYPE28ID")
    private String fgeneralassacttype28id;

    @TableField("FGENERALASSACTTYPE29ID")
    private String fgeneralassacttype29id;

    @TableField("FGENERALASSACTTYPE30ID")
    private String fgeneralassacttype30id;

    @TableField("FGENERALASSACTTYPE31ID")
    private String fgeneralassacttype31id;

    @TableField("FGENERALASSACTTYPE32ID")
    private String fgeneralassacttype32id;

    @TableField("FGENERALASSACTTYPE33ID")
    private String fgeneralassacttype33id;

    @TableField("FGENERALASSACTTYPE34ID")
    private String fgeneralassacttype34id;

    @TableField("FGENERALASSACTTYPE35ID")
    private String fgeneralassacttype35id;

    @TableField("FGENERALASSACTTYPE36ID")
    private String fgeneralassacttype36id;

    @TableField("FGENERALASSACTTYPE37ID")
    private String fgeneralassacttype37id;

    @TableField("FGENERALASSACTTYPE38ID")
    private String fgeneralassacttype38id;

    @TableField("FGENERALASSACTTYPE39ID")
    private String fgeneralassacttype39id;

    @TableField("FGENERALASSACTTYPE40ID")
    private String fgeneralassacttype40id;

    @TableField("FGENERALASSACTTYPE41ID")
    private String fgeneralassacttype41id;

    @TableField("FGENERALASSACTTYPE42ID")
    private String fgeneralassacttype42id;

    @TableField("FGENERALASSACTTYPE43ID")
    private String fgeneralassacttype43id;

    @TableField("FGENERALASSACTTYPE44ID")
    private String fgeneralassacttype44id;

    @TableField("FGENERALASSACTTYPE45ID")
    private String fgeneralassacttype45id;

    @TableField("FGENERALASSACTTYPE46ID")
    private String fgeneralassacttype46id;

    @TableField("FGENERALASSACTTYPE47ID")
    private String fgeneralassacttype47id;

    @TableField("FGENERALASSACTTYPE48ID")
    private String fgeneralassacttype48id;

    @TableField("FGENERALASSACTTYPE49ID")
    private String fgeneralassacttype49id;

    @TableField("FGENERALASSACTTYPE50ID")
    private String fgeneralassacttype50id;

    @TableField("FGENERALASSACTTYPE51ID")
    private String fgeneralassacttype51id;

    @TableField("FGENERALASSACTTYPE52ID")
    private String fgeneralassacttype52id;

    @TableField("FGENERALASSACTTYPE53ID")
    private String fgeneralassacttype53id;

    @TableField("FGENERALASSACTTYPE54ID")
    private String fgeneralassacttype54id;

    @TableField("FGENERALASSACTTYPE55ID")
    private String fgeneralassacttype55id;

    @TableField("FGENERALASSACTTYPE56ID")
    private String fgeneralassacttype56id;

    @TableField("FGENERALASSACTTYPE57ID")
    private String fgeneralassacttype57id;

    @TableField("FGENERALASSACTTYPE58ID")
    private String fgeneralassacttype58id;

    @TableField("FGENERALASSACTTYPE59ID")
    private String fgeneralassacttype59id;

    @TableField("FGENERALASSACTTYPE60ID")
    private String fgeneralassacttype60id;

    @TableField("FGENERALASSACTTYPE61ID")
    private String fgeneralassacttype61id;

    @TableField("FGENERALASSACTTYPE62ID")
    private String fgeneralassacttype62id;

    @TableField("FGENERALASSACTTYPE63ID")
    private String fgeneralassacttype63id;

    @TableField("FGENERALASSACTTYPE64ID")
    private String fgeneralassacttype64id;

    @TableField("FGENERALASSACTTYPE65ID")
    private String fgeneralassacttype65id;

    @TableField("FGENERALASSACTTYPE66ID")
    private String fgeneralassacttype66id;

    @TableField("FGENERALASSACTTYPE67ID")
    private String fgeneralassacttype67id;

    @TableField("FGENERALASSACTTYPE68ID")
    private String fgeneralassacttype68id;

    @TableField("FGENERALASSACTTYPE69ID")
    private String fgeneralassacttype69id;

    @TableField("FGENERALASSACTTYPE70ID")
    private String fgeneralassacttype70id;

    @TableField("FGENERALASSACTTYPE71ID")
    private String fgeneralassacttype71id;

    @TableField("FGENERALASSACTTYPE72ID")
    private String fgeneralassacttype72id;

    @TableField("FGENERALASSACTTYPE73ID")
    private String fgeneralassacttype73id;

    @TableField("FGENERALASSACTTYPE74ID")
    private String fgeneralassacttype74id;

    @TableField("FGENERALASSACTTYPE75ID")
    private String fgeneralassacttype75id;

    @TableField("FGENERALASSACTTYPE76ID")
    private String fgeneralassacttype76id;

    @TableField("FGENERALASSACTTYPE77ID")
    private String fgeneralassacttype77id;

    @TableField("FGENERALASSACTTYPE78ID")
    private String fgeneralassacttype78id;

    @TableField("FGENERALASSACTTYPE79ID")
    private String fgeneralassacttype79id;

    @TableField("FGENERALASSACTTYPE80ID")
    private String fgeneralassacttype80id;

    @TableField("FGENERALASSACTTYPE81ID")
    private String fgeneralassacttype81id;

    @TableField("FGENERALASSACTTYPE82ID")
    private String fgeneralassacttype82id;

    @TableField("FGENERALASSACTTYPE83ID")
    private String fgeneralassacttype83id;

    @TableField("FGENERALASSACTTYPE84ID")
    private String fgeneralassacttype84id;

    @TableField("FGENERALASSACTTYPE85ID")
    private String fgeneralassacttype85id;

    @TableField("FGENERALASSACTTYPE86ID")
    private String fgeneralassacttype86id;

    @TableField("FGENERALASSACTTYPE87ID")
    private String fgeneralassacttype87id;

    @TableField("FGENERALASSACTTYPE88ID")
    private String fgeneralassacttype88id;

    @TableField("FGENERALASSACTTYPE89ID")
    private String fgeneralassacttype89id;

    @TableField("FGENERALASSACTTYPE90ID")
    private String fgeneralassacttype90id;

    @TableField("FGENERALASSACTTYPE91ID")
    private String fgeneralassacttype91id;

    @TableField("FGENERALASSACTTYPE92ID")
    private String fgeneralassacttype92id;

    @TableField("FGENERALASSACTTYPE93ID")
    private String fgeneralassacttype93id;

    @TableField("FGENERALASSACTTYPE94ID")
    private String fgeneralassacttype94id;

    @TableField("FGENERALASSACTTYPE95ID")
    private String fgeneralassacttype95id;

    @TableField("FGENERALASSACTTYPE96ID")
    private String fgeneralassacttype96id;

    @TableField("FGENERALASSACTTYPE97ID")
    private String fgeneralassacttype97id;

    @TableField("FGENERALASSACTTYPE98ID")
    private String fgeneralassacttype98id;

    @TableField("FGENERALASSACTTYPE99ID")
    private String fgeneralassacttype99id;

    @TableField("FGENERALASSACTTYPE100ID")
    private String fgeneralassacttype100id;

    @TableField("FDISPLAYNAMEGROUP_L1")
    private String fdisplaynamegroupL1;

    @TableField("FDISPLAYNAMEGROUP_L2")
    private String fdisplaynamegroupL2;

    @TableField("FDISPLAYNAMEGROUP_L3")
    private String fdisplaynamegroupL3;

    @TableField("FINNERACCOUNTID")
    private String finneraccountid;

    @TableField("FFPITEMID")
    private String ffpitemid;

    @TableField("FNTTYPEID")
    private String fnttypeid;

    @TableField("FNUMBERGROUP_L1")
    private String fnumbergroupL1;

    @TableField("FNUMBERGROUP_L2")
    private String fnumbergroupL2;

    @TableField("FNUMBERGROUP_L3")
    private String fnumbergroupL3;

    @TableField("FFEETYPEID")
    private String ffeetypeid;

    @TableField("FCOSTITEMID")
    private String fcostitemid;

    @TableField("FGENERALASSACTTYPE101ID")
    private String fgeneralassacttype101id;

    @TableField("FGENERALASSACTTYPE102ID")
    private String fgeneralassacttype102id;

    @TableField("FGENERALASSACTTYPE103ID")
    private String fgeneralassacttype103id;

    @TableField("FGENERALASSACTTYPE104ID")
    private String fgeneralassacttype104id;

    @TableField("FGENERALASSACTTYPE105ID")
    private String fgeneralassacttype105id;

    @TableField("FGENERALASSACTTYPE106ID")
    private String fgeneralassacttype106id;

    @TableField("FGENERALASSACTTYPE107ID")
    private String fgeneralassacttype107id;

    @TableField("FGENERALASSACTTYPE108ID")
    private String fgeneralassacttype108id;

    @TableField("FGENERALASSACTTYPE109ID")
    private String fgeneralassacttype109id;

    @TableField("FGENERALASSACTTYPE110ID")
    private String fgeneralassacttype110id;

    @TableField("FNATIONID")
    private String fnationid;

    @TableField("FAREAID")
    private String fareaid;

    @TableField("FVILLAGEID")
    private String fvillageid;


}
