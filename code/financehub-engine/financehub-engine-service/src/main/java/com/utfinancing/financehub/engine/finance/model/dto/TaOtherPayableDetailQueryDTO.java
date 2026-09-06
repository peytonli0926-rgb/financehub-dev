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
import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :   TaOtherPayableDetail查询from对象
 * @Modified :
 */
@ApiModel("TaOtherPayableDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaOtherPayableDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "重分类月份")
    @JsonFormat(pattern = "yyyy-MM", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date reclassificationMonth;

    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankBatchNo;

    @ApiModelProperty(value = "账龄")
    private Integer accountAge;

    @ApiModelProperty(value = "账龄分类")
    private String accountAgeClass;

    @ApiModelProperty(value = "ta其他应付款汇总idList")
    private List<Long> taOtherPayableIdList;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "ID")
    private Long id;
}
