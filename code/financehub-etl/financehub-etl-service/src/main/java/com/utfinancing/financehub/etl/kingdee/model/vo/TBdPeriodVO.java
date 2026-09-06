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
public class TBdPeriodVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private Long fperiodyear;

    @ApiModelProperty(value = "")
    private Long fperiodquarter;

    @ApiModelProperty(value = "")
    private Long fperiodnumber;

    @ApiModelProperty(value = "")
    private LocalDateTime fbegindate;

    @ApiModelProperty(value = "")
    private LocalDateTime fenddate;

    @ApiModelProperty(value = "")
    private Long fisadjustperiod;

    @ApiModelProperty(value = "")
    private String ftypeid;

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
    private Long fnumber;

    @ApiModelProperty(value = "")
    private String fdescriptionL1;

    @ApiModelProperty(value = "")
    private String fdescriptionL2;

    @ApiModelProperty(value = "")
    private String fdescriptionL3;

}
