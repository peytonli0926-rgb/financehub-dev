package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description : 平价转让详情VO对象
 * @Modified :
 */
@Data
public class ParityTransferDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "平价转让id")
    private Long parityTransferId;

    @ApiModelProperty(value = "转让批次")
    private String batch;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgIdName;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "应收租金余额")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值余额")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收销项税余额")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益余额")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "承租人保证金余额")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "应付经销商服务费-暂估余额")
    private BigDecimal payableAgencyEstimate;

    @ApiModelProperty(value = "应付收车费-暂估余额")
    private BigDecimal payableVehicleEstimate;

    @ApiModelProperty(value = "应付手环成本_暂估余额")
    private BigDecimal payableBandCostEstimate;

    @ApiModelProperty(value = "应付抵押费_暂估余额")
    private BigDecimal payablePledgeEstimate;

    @ApiModelProperty(value = "应付解抵押费_暂估余额")
    private BigDecimal payableUnpledgeEstimate;

    @ApiModelProperty(value = "应付其他租赁成本-暂估余额")
    private BigDecimal payableOtherCostEstimate;

    @ApiModelProperty(value = "减值准备余额-应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    @ApiModelProperty(value = "评估价")
    private BigDecimal appraisedValue;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherIds;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
