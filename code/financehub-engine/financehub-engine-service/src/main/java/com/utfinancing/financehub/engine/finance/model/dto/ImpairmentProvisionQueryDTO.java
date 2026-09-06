package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description :   ImpairmentProvision查询from对象
 * @Modified :
 */
@ApiModel("ImpairmentProvision查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ImpairmentProvisionQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "财务日期")
    private Date accountDate;
}
