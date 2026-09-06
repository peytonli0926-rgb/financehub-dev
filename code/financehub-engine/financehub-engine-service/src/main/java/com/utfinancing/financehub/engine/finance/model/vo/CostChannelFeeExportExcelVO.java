package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.CostChannelFeeExcelVO</li>
 * <li>CreateTime : 2023/12/13 16:39</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "成本类支付导出VO")
@Data
public class CostChannelFeeExportExcelVO {

    @ApiModelProperty(value = "渠道类型（1：经销商服务费，2：外部渠道费，3：海通渠道费）")
    @Excel(name = "渠道费类型",width = 20,readConverterExp ="1=经销商服务费,2=外部渠道费,3=海通渠道费,4=收车费,5=抵押费,6=解抵押费,7=安装费，8=服务费,9=设备款")
    private String channelType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    @Excel(name = "合同名称",width = 20)
    private String contractName;

    @ApiModelProperty("合同状态")
    @Excel(name = "合同状态",width = 20)
    private String contractStatus;

    @ApiModelProperty("财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "是否区分合同状态")
    @Excel(name = "是否区分合同状态",width = 20,readConverterExp="0=否,1=是")
    private String isContractStatus;

    @ApiModelProperty(value = "处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)")
    @Excel(name = "处理状态",width = 20,readConverterExp="1=已录入,2=已提交,3=已复核,4=已传至金蝶")
    private String processStatus;

    @ApiModelProperty(value = "渠道方编码")
    @Excel(name = "渠道方编码",width = 20)
    private String channelCode;

    @ApiModelProperty(value = "渠道方名称")
    @Excel(name = "渠道方名称",width = 20)
    private String channelName;

    //业务日期
    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    @ApiModelProperty(value = "主机厂")
    @Excel(name = "主机厂",width = 20)
    private String hostFactory;

    @ApiModelProperty(value = "实付金额（含税）")
    @Excel(name = "实付金额（含税）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "对应交易结构金额（不含税）")
    @Excel(name = "对应交易结构金额（不含税）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal noTaxTransactionAmount;

    @ApiModelProperty(value = "交易结构调整类型")
    @Excel(name = "交易结构调整类型",width = 20)
    private String structureType;

}
