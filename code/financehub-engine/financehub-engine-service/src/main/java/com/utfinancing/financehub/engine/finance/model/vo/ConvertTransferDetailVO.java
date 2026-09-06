package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-详情VO对象
 * @Modified :
 */
@Data
public class ConvertTransferDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "折价转让id")
    private Long convertTransferId;

    @ApiModelProperty(value = "转让批次")
    private String batch;

    @ApiModelProperty(value = "原合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现融资租赁收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "承租人保证金")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    @ApiModelProperty(value = "评估价")
    private BigDecimal appraisedValue;

    @ApiModelProperty(value = "转让时敞口")
    private BigDecimal transferOpen;

    @ApiModelProperty(value = "补提拨备")
    private BigDecimal supplementaryProvision;

    @ApiModelProperty(value = "收益确认")
    private BigDecimal revenueRecognition;

    @ApiModelProperty(value = "应付经销商服务费-暂估")
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付收车费-暂估")
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估")
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估")
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应付解抵押费_暂估")
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估")
    private BigDecimal payableOtherCostEstimate;

    @ApiModelProperty(value = "基准日后计提收益")
    private BigDecimal baseDateAccruedIncome;

    @ApiModelProperty(value = "基准日后计提拨备")
    private BigDecimal baseDateProvision;

    @ApiModelProperty(value = "基准日后收款")
    private BigDecimal baseDateReceive;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
