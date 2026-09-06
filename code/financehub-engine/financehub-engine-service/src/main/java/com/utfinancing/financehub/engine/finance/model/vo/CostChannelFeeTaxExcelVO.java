package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.CostChannelFeeTaxExcelVO</li>
 * <li>CreateTime : 2023/12/16 12:42</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "成本类税率导出VO")
@Data
public class CostChannelFeeTaxExcelVO {
    @ApiModelProperty(value = "渠道类型（1：经销商服务费，2：外部渠道费，3：海通渠道费）")
    @Excel(name = "渠道费类型",width = 20,readConverterExp = "1=经销商服务费,2=外部渠道费,3=海通渠道费,4=收车费,5=抵押费,6=解抵押费,7=安装费，8=服务费,9=设备款")
    private String channelType;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgId;

    @ApiModelProperty(value = "渠道方编码")
    @Excel(name = "渠道方编码",width = 20)
    private String channelCode;

    @ApiModelProperty(value = "渠道方名称")
    @Excel(name = "渠道方名称",width = 20)
    private String channelName;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20)
    private Date businessDate;

    @ApiModelProperty(value = "金额（不含税）")
    @Excel(name = "金额（不含税）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    @Excel(name = "税额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxAmount;
}
