package com.utfinancing.financehub.engine.verification.model.dto;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.catalina.LifecycleState;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-12
 * @Description :   VerificationDetails查询from对象
 * @Modified :
 */
@ApiModel("VerificationDetails查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VerificationDetailsQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "核销表id")
    private Long verificationId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "应收首付款")
    private BigDecimal receivableDownpayment;

    @ApiModelProperty(value = "应收手续费")
    private BigDecimal receivableCommission;

    @ApiModelProperty(value = "应收保险费")
    private BigDecimal receivableInsurance;

    @ApiModelProperty(value = "应收其他收入")
    private BigDecimal receivableOtherincome;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "应付设备款-暂估")
    private BigDecimal payableDeviceEstimate;

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

    @ApiModelProperty(value = "财务核销敞口")
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "补偿提备")
    private BigDecimal compensationProvisionAmount;

    @ApiModelProperty(value = "核销详情id集合")
    private List<Long> idList;

    @ApiModelProperty(value = "核销id集合")
    private List<Long> verificationIdList;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "应收租赁款组合拨备")
    private String depreciationReserves;
}
