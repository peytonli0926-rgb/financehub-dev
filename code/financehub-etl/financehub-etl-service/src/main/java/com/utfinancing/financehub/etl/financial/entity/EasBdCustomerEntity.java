package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("eas_bd_customer")
public class EasBdCustomerEntity extends Model<EasBdCustomerEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fid", type = IdType.ASSIGN_ID)
    private String fid;

    private String fcreatorid;

    private LocalDateTime fcreatetime;

    private String flastupdateuserid;

    private LocalDateTime flastupdatetime;

    private String fcontrolunitid;

    private String fnameL1;

    private String fnameL2;

    private String fnameL3;

    private String fnumber;

    private String fdescriptionL1;

    private String fdescriptionL2;

    private String fdescriptionL3;

    private String fsimplename;

    private String fcountryid;

    private String fcityid;

    private String fprovince;

    private String fregionid;

    private String ftaxdataid;

    private String fusedstatus;

    private String fbizanalysiscodeid;

    private String findustryid;

    private String finternalcompanyid;

    private String ffreezeorgunitid;

    private String fbrowsegroupid;

    private String fartificialperson;

    private String fbizregisterno;

    private String fisinternalcompany;

    private String ftxregisterno;

    private String fversion;

    private String feffectedstatus;

    private String fsuperiorunit;

    private String fbarcode;

    private String fmnemoniccode;

    private String fbusilicence;

    private String fbusiexequatur;

    private String fgspauthentication;

    private String fcustomerkind;

    private String fforeignname;

    private String fadmincuid;

    private String faddress;

    private String fparentid;

    private String finvoicetype;

    private String fiscredited;

    private String ftaxrate;

    private String fismulticopy;

    private String fisswitch;

    private String foldnumber;

    private String fnamepinyin;

    private String fnameshortpinyin;

    private String finternalprofitcenter;

    private String finternalcustomertype;

    private String cfgbindustry;

    private String cfhtListedInfoid;

    private String cfhtCustNatureid;

    private String cfglkh;

    private String ftaxpayertype;

    private String cfisaffiliatedcompnay;


}
