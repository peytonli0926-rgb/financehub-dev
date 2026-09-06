package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-22
 * @Description : 报表-财务入库出库VO对象
 * @Modified :
 */
@Data
public class ReportFinanceInOutExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同代码")
    private String contractCode;

    @ApiModelProperty(value = "客户代码")
    private String clientCode;

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

    @ApiModelProperty(value = "回收设备成本科目余额")
    private BigDecimal recyclingEquipmentAccountBalance;

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

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;


}
