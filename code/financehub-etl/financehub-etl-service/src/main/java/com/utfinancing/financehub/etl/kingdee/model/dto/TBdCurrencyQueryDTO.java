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
 * @Description :   TBdCurrency查询from对象
 * @Modified :
 */
@ApiModel("TBdCurrency查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TBdCurrencyQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String fid;

    @ApiModelProperty(value = "")
    private String fnumber;

    @ApiModelProperty(value = "")
    private String fnameL1;

    @ApiModelProperty(value = "")
    private String fnameL2;

    @ApiModelProperty(value = "")
    private String fnameL3;

    @ApiModelProperty(value = "")
    private String fisocode;

    @ApiModelProperty(value = "")
    private String fsign;

    @ApiModelProperty(value = "")
    private String fbaseunitL1;

    @ApiModelProperty(value = "")
    private String fbaseunitL2;

    @ApiModelProperty(value = "")
    private String fbaseunitL3;

    @ApiModelProperty(value = "")
    private Long fprecision;

    @ApiModelProperty(value = "")
    private String fdescriptionL1;

    @ApiModelProperty(value = "")
    private String fdescriptionL2;

    @ApiModelProperty(value = "")
    private String fdescriptionL3;

    @ApiModelProperty(value = "")
    private String fsimplename;

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
    private Long fdeletedstatus;

    @ApiModelProperty(value = "")
    private Long fsortnumber;
}
