package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-22
 * @Description :   ReportFinanceInOut查询from对象
 * @Modified :
 */
@ApiModel("ReportFinanceInOut查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportFinanceInOutQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "执行时间yyyy-MM-dd")
    private String executeDate;

    @ApiModelProperty(value = "执行数据期间yyyyMM")
    private Integer periodCode;

    @ApiModelProperty(value = "合同代码")
    private String contractCode;

    @ApiModelProperty(value = "客户代码")
    private String clientCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "入库时间")
    private String inboundDate;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "承租人保证金")
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "财务敞口")
    private BigDecimal financialExposure;

    @ApiModelProperty(value = "回收设备成本")
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "入库时计提减值")
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "回收设备减值")
    private BigDecimal substractBalance;

    @ApiModelProperty(value = "净值")
    private BigDecimal netWorth;

    @ApiModelProperty(value = "暂收款项")
    private BigDecimal provisionalReceiptsBalance;

    @ApiModelProperty(value = "出库日期")
    private String outboundDate;

    @ApiModelProperty(value = "出库类型")
    private String outboundType;

    @ApiModelProperty(value = "处置金额")
    private BigDecimal dealAmount;

    @ApiModelProperty(value = "应交销项税")
    private BigDecimal receivableServiceOuttaxAmount;

    @ApiModelProperty(value = "融资租赁资产处置损益")
    private BigDecimal profitLoss;

    private Integer limit;

    private Integer offset;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "入库日期开始")
    private String inboundDateStart;

    @ApiModelProperty(value = "入库日期结束")
    private String inboundDateEnd;

    @ApiModelProperty(value = "出库日期开始")
    private String outboundDateStart;

    @ApiModelProperty(value = "出库日期结束")
    private String outboundDateEnd;

//    @ApiModelProperty(value = "查询类型 分页 query 下载 export")
//    private String queryType;
}
