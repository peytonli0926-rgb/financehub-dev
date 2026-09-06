package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-13
 * @Description :   CostChannelFee查询from对象
 * @Modified :
 */
@ApiModel("CostChannelFee查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CostChannelFeeQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "渠道类型 1=经销商服务费,2=外部渠道费,3=海通渠道费,4=收车费,5=抵押费,6=解抵押费,7=安装费，8=服务费,9=设备款")
    private String channelType;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "渠道方编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道方名称")
    private String channelName;

    @ApiModelProperty(value = "主机厂")
    private String hostFactory;

    @ApiModelProperty(value = "实付金额（含税）")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "对应交易结构金额（不含税）")
    private BigDecimal noTaxTransactionAmount;

    @ApiModelProperty(value = "交易结构调整类型")
    private String structureType;

    @ApiModelProperty(value = "凭证id,多个以逗号分隔")
    private String voucherIds;

    @ApiModelProperty(value = "业务日期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startBusinessDate;

    @ApiModelProperty(value = "业务日期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endBusinessDate;

    @ApiModelProperty("费用大类（费用大类(1:GPS,2:手环设备款,3:经销商服务费、外部渠道费、海通渠道费,4:收车费、抵押费、解抵押费)）")
    private String expenseMainCategoryType;

    @ApiModelProperty(value = "是否区分合同状态")
    private String isContractStatus;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同状态")
    private List<String> contractStatusList;

    @ApiModelProperty("财务合同状态")
    private List<String> financialContractStatusList;

    @ApiModelProperty(value = "处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)")
    private List<String> processStatusList;

    @ApiModelProperty(value = "Id集合")
    private List<Long> idList;

    @ApiModelProperty(value = "财务日期")
    private Date financialDate;

    @ApiModelProperty(value = "财务日期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startFinancialDate;

    @ApiModelProperty(value = "财务日期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endFinancialDate;

    @ApiModelProperty("流程实例id")
    private Long processInstanceId;

    @ApiModelProperty("id")
    private Long id;

}
