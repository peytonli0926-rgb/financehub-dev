package com.utfinancing.financehub.engine.verification.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO</li>
 * <li>CreateTime : 2023/10/19 14:35</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "核销回款详细VO")
@Getter
@Setter
public class VerificationPaybackDetailsVO {

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体名称",width = 20)
    private String orgName;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称",width = 20)
    private String clientName;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "回款月份")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @Excel(name = "回款月份",width = 20, dateFormat = "yyyy-MM-dd")
    private String businessDate;

    @ApiModelProperty(value = "应收租金")
    @Excel(name = "应收租金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableLeaseAmount;

    @ApiModelProperty(value = "应收期末残值")
    @Excel(name = "应收期末残值",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableLastAmount;

    @ApiModelProperty(value = "应收销项税 待定")
    @Excel(name = "应收销项税",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableLastAmountd;

    @ApiModelProperty(value = "应收首付款")
    @Excel(name = "应收首付款",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableFirstAmount;

    @ApiModelProperty(value = "应收保险费")
    @Excel(name = "应收保险费",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableInsuranceAmount;

    @ApiModelProperty(value = "应收手续费")
    @Excel(name = "应收手续费",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableProcedureAmount;

    @ApiModelProperty(value = "应收其他收入")
    @Excel(name = "应收其他收入",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receiveOtherRevenues;

    @ApiModelProperty(value = "未实现融资租赁收益 待定")
    @Excel(name = "未实现融资租赁收益",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receiveOtherRevenuesd;

    @ApiModelProperty(value = "应收罚息")
    @Excel(name = "应收罚息",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal recycleDefaultInterestAmount;

    @ApiModelProperty(value = "应收变更手续费")
    @Excel(name = "应收变更手续费",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receiveTerminateProcedureAmount;

    @ApiModelProperty(value = "承租人保证金")
    @Excel(name = "承租人保证金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableMarginAmount;

    @ApiModelProperty(value = "确认收入金额(融资租赁收益余额)")
    @Excel(name = "确认收入金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal leaseRevenueAmount;

    @ApiModelProperty(value = "计提税金（应收销项税余额）")
    @Excel(name = "计提税金",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableOuttaxAmount;

    //拨备转回金额 = 应收融资租赁款减值损失(depreciationLossBalance)-坏账注销转回 减值损失发生额
    @ApiModelProperty(value = "拨备转回金额")
    @Excel(name = "拨备转回金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depreciationLossAmount;

    //减值准备
    @ApiModelProperty(value = "回款前核销余额")
    @Excel(name = "回款前核销余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depreciationReservesAmount;

    //回款前核销余额-拨备转回金额
    @ApiModelProperty(value = "回款后核销余额")
    @Excel(name = "回款后核销余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal depreciationAmount;

    @ApiModelProperty(value = "是否异常，0：否 1：是")
    @Excel(name = "是否异常",width = 20,readConverterExp = "0=否,1=是")
    private String isAbnormal;

    @ApiModelProperty(value = "回款后核销余额大于0的合同编号集合")
    private List<String> greaterThanZeroList;

    @ApiModelProperty(value = "回款后核销余额大于回款前的合同编号集合")
    private List<String> greaterThanReservesList;

    @ApiModelProperty(value = "客户编码")
    @Excel(name = "客户编码",width = 20)
    private String clientCode;
}
