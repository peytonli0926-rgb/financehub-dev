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
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.CostGpsAndBraceleteFeeExcelVO</li>
 * <li>CreateTime : 2023/12/20 10:24</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("GPS/手环设备款导出VO")
@Data
public class CostGpsAndBraceleteFeeExcelVO {

    @ApiModelProperty(value = "费用类型（1：经销商服务费，2：外部渠道费，3：海通渠道费，4：收车费，5：抵押费，6：解抵押费，7：GPS-安装费，8：GPS-服务费,9:手环设备款-安装费，10：手环设备款-服务费）")
    @Excel(name = "费用类型",width = 20)
    private String channelType;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgId;

    //业务日期
    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期",width = 20,dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    @ApiModelProperty(value = "实付金额（含税）")
    @Excel(name = "实付金额（含税）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "对应交易结构金额（不含税）")
    @Excel(name = "对应交易结构金额（不含税）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal noTaxTransactionAmount;

    @ApiModelProperty(value = "交易结构调整类型")
    @Excel(name = "交易结构调整类型",width = 20)
    private String structureType;

    //金额（不含税）
    @ApiModelProperty(value = "发票金额（不含税）")
    @Excel(name = "发票金额（不含税）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal noTaxAmount;

    //税额
    @ApiModelProperty(value = "税额")
    @Excel(name = "税额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taxAmount;
}
