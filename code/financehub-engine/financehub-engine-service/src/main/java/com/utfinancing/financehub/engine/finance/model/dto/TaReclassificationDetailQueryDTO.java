package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :   TaReclassificationDetail查询from对象
 * @Modified :
 */
@ApiModel("TaReclassificationDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaReclassificationDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "重分类月份")
//    @JsonFormat(pattern = "yyyy-MM", timezone = "DTM+8")
//    @DateTimeFormat(pattern = "yyyy-MM")
    private String reclassificationMonth;

    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "到账主体")
    private String bankOrgId;

    @ApiModelProperty(value = "TA重分类金额（0代表为0；1代表不为0）")
    private String taReclassificationAmountStr;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "ta重分类idList")
    private List<Long> taReclassificationIdList;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "业务系统编码集合")
    private List<String> systemCodeList;

    @ApiModelProperty(value = "网银到账主体集合")
    private List<String> bankOrgIdList;

}
