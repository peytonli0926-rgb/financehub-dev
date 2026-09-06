package com.utfinancing.financehub.etl.kingdee.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :   TBdAccountview查询from对象
 * @Modified :
 */
@ApiModel("TBdAccountview查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TBdAccountviewQueryDTO extends BaseQueryDTO{

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
    private Long fisleaf;

    @ApiModelProperty(value = "")
    private Long flevel;

    @ApiModelProperty(value = "")
    private String flongnumber;

    @ApiModelProperty(value = "")
    private String fcreatorid;

    @ApiModelProperty(value = "")
    private LocalDateTime fcreatetime;

    @ApiModelProperty(value = "")
    private String flastupdateuserid;

    @ApiModelProperty(value = "")
    private LocalDateTime flastupdatetime;

    @ApiModelProperty(value = "")
    private String faccounttableid;

    @ApiModelProperty(value = "")
    private String faccounttypeid;

    @ApiModelProperty(value = "")
    private String flongnameL1;

    @ApiModelProperty(value = "")
    private String flongnameL2;

    @ApiModelProperty(value = "")
    private String flongnameL3;

    @ApiModelProperty(value = "")
    private String fparentid;

    @ApiModelProperty(value = "")
    private Long fisgfreeze;

    @ApiModelProperty(value = "")
    private String fgaa;

    @ApiModelProperty(value = "")
    private Long fiscfreeze;

    @ApiModelProperty(value = "")
    private String fhelpcode;

    @ApiModelProperty(value = "")
    private String fcompanyid;

    @ApiModelProperty(value = "")
    private String fcurrencyid;

    @ApiModelProperty(value = "")
    private Long fdc;

    @ApiModelProperty(value = "")
    private Long fischangecurrency;

    @ApiModelProperty(value = "")
    private Long fiscashequivalent;

    @ApiModelProperty(value = "")
    private Long fiscountaccrual;

    @ApiModelProperty(value = "")
    private BigDecimal faccrualper;

    @ApiModelProperty(value = "")
    private Long fisqty;

    @ApiModelProperty(value = "")
    private String fmeasureunitgroupid;

    @ApiModelProperty(value = "")
    private String fmeasureunitid;

    @ApiModelProperty(value = "")
    private Long fac;

    @ApiModelProperty(value = "")
    private Long fpltype;

    @ApiModelProperty(value = "")
    private Long fcontrol;

    @ApiModelProperty(value = "")
    private String fcaa;

    @ApiModelProperty(value = "")
    private Long facctcurrency;

    @ApiModelProperty(value = "")
    private Long fisbank;

    @ApiModelProperty(value = "")
    private Long fiscash;

    @ApiModelProperty(value = "")
    private Long fhasuserproperty;

    @ApiModelProperty(value = "")
    private String faccountid;

    @ApiModelProperty(value = "")
    private Long fglevel;

    @ApiModelProperty(value = "")
    private Long fisallowca;

    @ApiModelProperty(value = "")
    private String fcontrolunitid;

    @ApiModelProperty(value = "")
    private String fupperid;

    @ApiModelProperty(value = "")
    private String fparentaaid;

    @ApiModelProperty(value = "")
    private Long fisupperallowca;

    @ApiModelProperty(value = "")
    private Long fisselffreeze;

    @ApiModelProperty(value = "")
    private Long fisparentfreeze;

    @ApiModelProperty(value = "")
    private String frefid;

    @ApiModelProperty(value = "")
    private Long fcontrollevel;

    @ApiModelProperty(value = "")
    private Long fisoutdailyaccount;

    @ApiModelProperty(value = "")
    private String fdisplaynameL1;

    @ApiModelProperty(value = "")
    private String fdisplaynameL2;

    @ApiModelProperty(value = "")
    private String fdisplaynameL3;

    @ApiModelProperty(value = "")
    private Long facnotice;

    @ApiModelProperty(value = "")
    private Long fbw;

    @ApiModelProperty(value = "")
    private String fmaincashflowitemid;

    @ApiModelProperty(value = "")
    private String fattcashflowitemid;

    @ApiModelProperty(value = "")
    private String fborrowermaincashflowitemid;

    @ApiModelProperty(value = "")
    private String fborrowerattcashflowitemid;

    @ApiModelProperty(value = "")
    private String flendermaincashflowitemid;

    @ApiModelProperty(value = "")
    private String flenderattcashflowitemid;

    @ApiModelProperty(value = "")
    private Long fiscontrol;

    @ApiModelProperty(value = "")
    private String fnamepinyin;

    @ApiModelProperty(value = "")
    private String fnameshortpinyin;

    @ApiModelProperty(value = "")
    private Long faccrualdirection;

    @ApiModelProperty(value = "")
    private Long fisbizchangecurrency;

    @ApiModelProperty(value = "")
    private Long fisprofitcenter;

    @ApiModelProperty(value = "")
    private Long fcategory;

    @ApiModelProperty(value = "")
    private Long fdifftype;
}
