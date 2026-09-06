package com.utfinancing.financehub.etl.kingdee.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : VO对象
 * @Modified :
 */
@Data
public class TOrgCompanyVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private String fnameL1;

    @ApiModelProperty(value = "")
    private String fnameL2;

    @ApiModelProperty(value = "")
    private String fnameL3;

    @ApiModelProperty(value = "")
    private String fnumber;

    @ApiModelProperty(value = "")
    private String fdescriptionL1;

    @ApiModelProperty(value = "")
    private String fdescriptionL2;

    @ApiModelProperty(value = "")
    private String fdescriptionL3;

    @ApiModelProperty(value = "")
    private String fsimplename;

    @ApiModelProperty(value = "")
    private Long fisgrouping;

    @ApiModelProperty(value = "")
    private LocalDateTime feffectdate;

    @ApiModelProperty(value = "")
    private LocalDateTime finvaliddate;

    @ApiModelProperty(value = "")
    private Long fisfreeze;

    @ApiModelProperty(value = "")
    private Long fiscompanyorgunit;

    @ApiModelProperty(value = "")
    private Long fisadminorgunit;

    @ApiModelProperty(value = "")
    private Long fissaleorgunit;

    @ApiModelProperty(value = "")
    private Long fispurchaseorgunit;

    @ApiModelProperty(value = "")
    private Long fisstorageorgunit;

    @ApiModelProperty(value = "")
    private Long fisprofitorgunit;

    @ApiModelProperty(value = "")
    private Long fiscostorgunit;

    @ApiModelProperty(value = "")
    private Long fiscu;

    @ApiModelProperty(value = "")
    private Long fisunion;

    @ApiModelProperty(value = "")
    private Long fishrorgunit;

    @ApiModelProperty(value = "")
    private String fcreatorid;

    @ApiModelProperty(value = "")
    private LocalDateTime fcreatetime;

    @ApiModelProperty(value = "")
    private String flastupdateuserid;

    @ApiModelProperty(value = "")
    private LocalDateTime flastupdatetime;

    @ApiModelProperty(value = "")
    private String fcontrolunitid;

    @ApiModelProperty(value = "")
    private Long fisleaf;

    @ApiModelProperty(value = "")
    private Long flevel;

    @ApiModelProperty(value = "")
    private String flongnumber;

    @ApiModelProperty(value = "")
    private String fparentid;

    @ApiModelProperty(value = "")
    private String ftaxnumber;

    @ApiModelProperty(value = "")
    private Long fisgroup;

    @ApiModelProperty(value = "")
    private Long fisonlyunion;

    @ApiModelProperty(value = "")
    private String findustry;

    @ApiModelProperty(value = "")
    private String fbankid;

    @ApiModelProperty(value = "")
    private String freportcurrencyid;

    @ApiModelProperty(value = "")
    private String faccountperiodid;

    @ApiModelProperty(value = "")
    private String fjuridicalpersonid;

    @ApiModelProperty(value = "")
    private String faccounttableid;

    @ApiModelProperty(value = "")
    private String faddressid;

    @ApiModelProperty(value = "")
    private String fbasecurrencyid;

    @ApiModelProperty(value = "")
    private String fbaseexgtableid;

    @ApiModelProperty(value = "")
    private String fadjustexgtableid;

    @ApiModelProperty(value = "")
    private Long freportconvertmode;

    @ApiModelProperty(value = "")
    private String freportexgtableid;

    @ApiModelProperty(value = "")
    private Long fissealup;

    @ApiModelProperty(value = "")
    private Long fisbizunit;

    @ApiModelProperty(value = "")
    private Long fisstart;

    @ApiModelProperty(value = "")
    private Long fisousealup;

    @ApiModelProperty(value = "")
    private String fdisplaynameL1;

    @ApiModelProperty(value = "")
    private String fdisplaynameL2;

    @ApiModelProperty(value = "")
    private String fdisplaynameL3;

    @ApiModelProperty(value = "")
    private String fcompanyid;

    @ApiModelProperty(value = "")
    private String fregion;

    @ApiModelProperty(value = "")
    private Long feconomictype;

    @ApiModelProperty(value = "")
    private BigDecimal fregisteredcapital;

    @ApiModelProperty(value = "")
    private LocalDateTime fsetupdate;

    @ApiModelProperty(value = "")
    private LocalDateTime fendupdate;

    @ApiModelProperty(value = "")
    private String fterritoryL1;

    @ApiModelProperty(value = "")
    private String fterritoryL2;

    @ApiModelProperty(value = "")
    private String fterritoryL3;

    @ApiModelProperty(value = "")
    private Long fischurchyard;

    @ApiModelProperty(value = "")
    private String fregisteredcode;

    @ApiModelProperty(value = "")
    private LocalDateTime fpropertysealupdate;

    @ApiModelProperty(value = "")
    private String fversionnumber;

    @ApiModelProperty(value = "")
    private String fcode;

    @ApiModelProperty(value = "")
    private Long fisassistantorg;

    @ApiModelProperty(value = "")
    private String fmainorgid;

    @ApiModelProperty(value = "")
    private String faccountschemeid;

    @ApiModelProperty(value = "")
    private Long fistransportorgunit;

    @ApiModelProperty(value = "")
    private Long fisqualityorgunit;

    @ApiModelProperty(value = "")
    private String forgtypestr;

    @ApiModelProperty(value = "")
    private LocalDateTime factivitydate;

    @ApiModelProperty(value = "")
    private String fcontact;

    @ApiModelProperty(value = "")
    private String finvoicenumber;

    @ApiModelProperty(value = "")
    private String finvoicecount;

    @ApiModelProperty(value = "")
    private Long ftaxtype;

    @ApiModelProperty(value = "")
    private String fbankname;

    @ApiModelProperty(value = "")
    private String ftaxcode;

    @ApiModelProperty(value = "")
    private String fcontactphone;

    @ApiModelProperty(value = "")
    private String fbankaccount;

    @ApiModelProperty(value = "")
    private String ftaxaddress;

    @ApiModelProperty(value = "")
    private String fauthorizationcode;

    @ApiModelProperty(value = "")
    private String fauthorizationkey;

    @ApiModelProperty(value = "")
    private String ftaxregistername;

    @ApiModelProperty(value = "")
    private String ftaxpayerid;

}
