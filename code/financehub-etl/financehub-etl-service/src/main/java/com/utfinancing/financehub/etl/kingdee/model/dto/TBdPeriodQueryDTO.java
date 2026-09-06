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
 * @Description :   TBdPeriod查询from对象
 * @Modified :
 */
@ApiModel("TBdPeriod查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TBdPeriodQueryDTO extends BaseQueryDTO{

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
