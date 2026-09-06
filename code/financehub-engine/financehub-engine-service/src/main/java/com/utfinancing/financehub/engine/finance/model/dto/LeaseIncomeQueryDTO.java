package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-10
 * @Description :   LeaseIncome查询from对象
 * @Modified :
 */
@ApiModel("LeaseIncome查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaseIncomeQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    private String contractCode;

    @ApiModelProperty(value = "是否更新上次计提数据")
    private String isUpdateProvisionData;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "系统来源编码列表")
    private List<String> systemCodeList = new ArrayList<>();

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "租赁收益")
    private BigDecimal leaseIncomeAmount;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;
    private List<String> processStatusList;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "提交人")
    private String submitBy;

    @ApiModelProperty(value = "签约主体（组织机构编码）")
    private List<String> orgIdList;

    private List<Long> idList;

    private List<String> notGenerateOrgIds;

    private Integer planDatePeriod;
}
