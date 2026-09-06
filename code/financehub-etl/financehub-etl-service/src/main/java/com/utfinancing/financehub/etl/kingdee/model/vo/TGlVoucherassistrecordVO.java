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
public class TGlVoucherassistrecordVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private Long fseq;

    @ApiModelProperty(value = "")
    private String fbillid;

    @ApiModelProperty(value = "")
    private String fentryid;

    @ApiModelProperty(value = "")
    private String fcompanyid;

    @ApiModelProperty(value = "")
    private String fperiodid;

    @ApiModelProperty(value = "")
    private String faccountid;

    @ApiModelProperty(value = "")
    private String fassgrpid;

    @ApiModelProperty(value = "")
    private String fsettlementtypeid;

    @ApiModelProperty(value = "")
    private String fsettlementcode;

    @ApiModelProperty(value = "")
    private BigDecimal fstandardquantity;

    @ApiModelProperty(value = "")
    private String fbiznumber;

    @ApiModelProperty(value = "")
    private LocalDateTime fenddate;

    @ApiModelProperty(value = "")
    private String fcustomerid;

    @ApiModelProperty(value = "")
    private String fsupplierid;

    @ApiModelProperty(value = "")
    private String forgunitid;

    @ApiModelProperty(value = "")
    private String fmaterialid;

    @ApiModelProperty(value = "")
    private String fverifiedcussentid;

    @ApiModelProperty(value = "")
    private Long fisfullprop;

    @ApiModelProperty(value = "")
    private Long freqstatus;

    @ApiModelProperty(value = "")
    private Long freqcheckstatus;

    @ApiModelProperty(value = "")
    private String fchecknumber;

    @ApiModelProperty(value = "")
    private String finvoicenumber;

    @ApiModelProperty(value = "")
    private String fticketnumber;

    @ApiModelProperty(value = "")
    private Long fisvierified;

    @ApiModelProperty(value = "")
    private String freceiptformid;

    @ApiModelProperty(value = "")
    private String freceiptformentryid;

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
    private String fnumber;

    @ApiModelProperty(value = "")
    private String fhandlerid;

    @ApiModelProperty(value = "")
    private Long fhaseffected;

    @ApiModelProperty(value = "")
    private String fauditorid;

    @ApiModelProperty(value = "")
    private String fsourcebillid;

    @ApiModelProperty(value = "")
    private String fsourcefunction;

    @ApiModelProperty(value = "")
    private BigDecimal fmactrlamount;

    @ApiModelProperty(value = "")
    private BigDecimal fprice;

    @ApiModelProperty(value = "")
    private String fmeasureunitid;

    @ApiModelProperty(value = "")
    private BigDecimal fmaholdamount;

    @ApiModelProperty(value = "")
    private Long ffeetype;

    @ApiModelProperty(value = "")
    private BigDecimal fassistprice;

    @ApiModelProperty(value = "")
    private BigDecimal fassistqty;

    @ApiModelProperty(value = "")
    private String fassistunit;

    @ApiModelProperty(value = "")
    private LocalDateTime fbizdate;

    @ApiModelProperty(value = "")
    private BigDecimal foriginalamount;

    @ApiModelProperty(value = "")
    private BigDecimal flocalamount;

    @ApiModelProperty(value = "")
    private BigDecimal freportingamount;

    @ApiModelProperty(value = "")
    private BigDecimal fquantity;

    @ApiModelProperty(value = "")
    private String fdescription;

    @ApiModelProperty(value = "")
    private String fprimaryitemid;

    @ApiModelProperty(value = "")
    private String fsupplementaryitemid;

}
