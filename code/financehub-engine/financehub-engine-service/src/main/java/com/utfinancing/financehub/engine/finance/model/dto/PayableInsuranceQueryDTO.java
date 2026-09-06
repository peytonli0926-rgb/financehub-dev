package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description :   PayableInsurance查询from对象
 * @Modified :
 */
@ApiModel("PayableInsurance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayableInsuranceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

    @ApiModelProperty(value = "业务日期")
    @NotNull(message = "无业务日期,不能生成")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "签约主体（组织机构编码）")
    private List<String> orgIdList;

    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "上个月的会计期间")
    private int lastPeriodCode;

}
