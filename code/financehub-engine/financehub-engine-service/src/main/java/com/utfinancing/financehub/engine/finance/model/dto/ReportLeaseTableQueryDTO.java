package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :   CheckAccountDetailRecord查询from对象
 * @Modified :
 */
@ApiModel("ReportLeaseTable查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportLeaseTableQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractCode;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "查询日期")
    private String queryDate;

    @ApiModelProperty(value = "查询类型")
    private String queryType;

    private Integer limit;

    private Integer offset;

    @ApiModelProperty(value = "查询日期下一日")
    private String queryNextDate;

    private Integer periodCode;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "exportType: export导出 query 查询")
    private String exportType;
}
