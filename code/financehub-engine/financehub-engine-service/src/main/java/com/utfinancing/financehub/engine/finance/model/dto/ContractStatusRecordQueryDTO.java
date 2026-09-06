package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description :   ContractStatusRecord查询from对象
 * @Modified :
 */
@ApiModel("ContractStatusRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractStatusRecordQueryDTO extends BaseQueryDTO{


    @ApiModelProperty(value = "财务合同状态更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date financialContractStatusUpdateTime;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "公司")
    private List<String> orgIdList;

    @ApiModelProperty(value = "财务合同状态")
    private List<String> financialContractStatusList;

    private List<Long> idList;

    @ApiModelProperty(value = "开始财务合同状态更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startFinancialContractStatusUpdateTime;

    @ApiModelProperty(value = "结束财务合同状态更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endFinancialContractStatusUpdateTime;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> recordStatusList;

}
