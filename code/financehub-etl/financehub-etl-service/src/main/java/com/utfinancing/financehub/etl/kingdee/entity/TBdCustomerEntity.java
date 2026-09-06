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
 * @since 2023-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_BD_CUSTOMER")
public class TBdCustomerEntity extends Model<TBdCustomerEntity> {

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

    @TableField("FCOUNTRYID")
    private String fcountryid;

    @TableField("FCITYID")
    private String fcityid;

    @TableField("FPROVINCE")
    private String fprovince;

    @TableField("FREGIONID")
    private String fregionid;

    @TableField("FTAXDATAID")
    private String ftaxdataid;

    @TableField("FUSEDSTATUS")
    private Long fusedstatus;

    @TableField("FBIZANALYSISCODEID")
    private String fbizanalysiscodeid;

    @TableField("FINDUSTRYID")
    private String findustryid;

    @TableField("FINTERNALCOMPANYID")
    private String finternalcompanyid;

    @TableField("FFREEZEORGUNITID")
    private String ffreezeorgunitid;

    @TableField("FBROWSEGROUPID")
    private String fbrowsegroupid;

    @TableField("FARTIFICIALPERSON")
    private String fartificialperson;

    @TableField("FBIZREGISTERNO")
    private String fbizregisterno;

    @TableField("FISINTERNALCOMPANY")
    private Long fisinternalcompany;

    @TableField("FTXREGISTERNO")
    private String ftxregisterno;

    @TableField("FVERSION")
    private Long fversion;

    @TableField("FEFFECTEDSTATUS")
    private Long feffectedstatus;

    @TableField("FSUPERIORUNIT")
    private String fsuperiorunit;

    @TableField("FBARCODE")
    private String fbarcode;

    @TableField("FMNEMONICCODE")
    private String fmnemoniccode;

    @TableField("FBUSILICENCE")
    private String fbusilicence;

    @TableField("FBUSIEXEQUATUR")
    private String fbusiexequatur;

    @TableField("FGSPAUTHENTICATION")
    private String fgspauthentication;

    @TableField("FCUSTOMERKIND")
    private String fcustomerkind;

    @TableField("FFOREIGNNAME")
    private String fforeignname;

    @TableField("FADMINCUID")
    private String fadmincuid;

    @TableField("FADDRESS")
    private String faddress;

    @TableField("FPARENTID")
    private String fparentid;

    @TableField("FINVOICETYPE")
    private String finvoicetype;

    @TableField("FISCREDITED")
    private Long fiscredited;

    @TableField("FTAXRATE")
    private BigDecimal ftaxrate;

    @TableField("FISMULTICOPY")
    private Long fismulticopy;

    @TableField("FISSWITCH")
    private Long fisswitch;

    @TableField("FOLDNUMBER")
    private String foldnumber;

    @TableField("FNAMEPINYIN")
    private String fnamepinyin;

    @TableField("FNAMESHORTPINYIN")
    private String fnameshortpinyin;

    @TableField("FINTERNALPROFITCENTER")
    private String finternalprofitcenter;

    @TableField("FINTERNALCUSTOMERTYPE")
    private Long finternalcustomertype;

    @TableField("CFGBINDUSTRY")
    private String cfgbindustry;

    @TableField("CFHT_LISTED_INFOID")
    private String cfhtListedInfoid;

    @TableField("CFHT_CUST_NATUREID")
    private String cfhtCustNatureid;

    @TableField("CFGLKH")
    private Long cfglkh;

    @TableField("FTAXPAYERTYPE")
    private String ftaxpayertype;

    @TableField("CFISAFFILIATEDCOMPNAY")
    private Long cfisaffiliatedcompnay;


}
