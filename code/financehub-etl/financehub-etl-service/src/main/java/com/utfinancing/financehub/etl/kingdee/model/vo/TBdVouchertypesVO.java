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
public class TBdVouchertypesVO implements Serializable{
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
    private Long fpretermit;

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
    private String fadmincuid;

    @ApiModelProperty(value = "")
    private Long flevel;

    @ApiModelProperty(value = "")
    private String fcompanyid;

}
