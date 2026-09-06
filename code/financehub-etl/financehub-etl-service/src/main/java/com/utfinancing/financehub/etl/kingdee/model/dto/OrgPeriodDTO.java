package com.utfinancing.financehub.etl.kingdee.model.dto;

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
public class OrgPeriodDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "机构ID")
    private String orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "会计期间")
    private String periodCode;

}
