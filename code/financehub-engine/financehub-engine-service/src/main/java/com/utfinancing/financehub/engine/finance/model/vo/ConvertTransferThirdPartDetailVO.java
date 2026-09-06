package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel
public class ConvertTransferThirdPartDetailVO {
    //业务日期	记账日期	转让批次	转让方	受让方	基准日	交易日	转让价格	合同数量	处理状态
    @Excel(isExport = false)
    private Long id;

    /**
     * 转让批次
     */
    @Excel(name = "转让批次")
    @ApiModelProperty("转让批次")
    private String batch;

    /**
     * 原合同编码
     */
    @Excel(name = "原合同编码")
    @ApiModelProperty("原合同编码")
    private String contractCode;

    /**
     * 客户编码
     */
    @Excel(name = "客户编码")
    @ApiModelProperty("客户编码")
    private String clientCode;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    @ApiModelProperty("客户名称")
    private String clientName;

    /**
     * 签约主体
     */
    @Excel(name = "签约主体")
    @ApiModelProperty("签约主体")
    private String orgId;

    /**
     * 财务合同状态
     */
    @Excel(name = "财务合同状态")
    @ApiModelProperty("财务合同状态")
    private String financialContractStatus;


    /**
     * 应收租金
     */
    @Excel(name = "应收租金")
    @ApiModelProperty("应收租金")
    private BigDecimal receivableRent;

    /**
     * 应收期末残值
     */
    @Excel(name = "应收期末残值")
    @ApiModelProperty("应收期末残值")
    private BigDecimal receivableResidualValue;

    /**
     * 应收销项税
     */
    @Excel(name = "应收销项税")
    @ApiModelProperty("应收销项税")
    private BigDecimal receivableOuttax;

    /**
     * 未实现融资租赁收益
     */
    @Excel(name = "未实现融资租赁收益")
    @ApiModelProperty("未实现融资租赁收益")
    private BigDecimal unrealizedRevenue;

    /**
     * 承租人保证金
     */
    @Excel(name = "承租人保证金")
    @ApiModelProperty("承租人保证金")
    private BigDecimal lesseeMargin;

    /**
     * 应收租赁款组合拨备
     */
    @Excel(name = "应收租赁款组合拨备")
    @ApiModelProperty("应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    /**
     * 评估价
     */
    @Excel(name = "评估价")
    @ApiModelProperty("评估价")
    private BigDecimal appraisedValue;

    /**
     * 转让时敞口
     */
    @Excel(name = "转让时敞口")
    @ApiModelProperty("转让时敞口")
    private BigDecimal transferOpen;

    /**
     * 补提拨备
     */
    @Excel(name = "补提拨备")
    @ApiModelProperty("补提拨备")
    private BigDecimal supplementaryProvision;


    /**
     * 基准日后计提收益
     * 场景为SYJT，lease_revenue6_amount+lease_revenue_amount
     */
    @Excel(name = "基准日后计提收益")
    @ApiModelProperty("基准日后计提收益")
    private BigDecimal baseDateAccruedIncome;

    /**
     * 基准日后计提拨备
     * 场景为JZJT，depreciation_reserves_amount汇总
     */
    @Excel(name = "基准日后计提拨备")
    @ApiModelProperty("基准日后计提拨备")
    private BigDecimal baseDateProvision;

    /**
     * 基准日后收款
     */
    @Excel(name = "基准日后收款")
    @ApiModelProperty("基准日后收款")
    private BigDecimal baseDateReceive;

    /**
     * 基准日后开票
     * <p>
     * 场景为KJFP，receivable_service_outtax_amount + receivable_outtax_amount + 特殊状态使用科目的汇总金额
     */
    @Excel(name = "基准日后开票")
    @ApiModelProperty("基准日后开票")
    private BigDecimal baseDateInvoices;

    public static ConvertTransferThirdPartDetailVO from(ConvertTransferThirdPartDetailEntity entity) {
        ConvertTransferThirdPartDetailVO vo = new ConvertTransferThirdPartDetailVO();
        vo.setId(entity.getId());
        vo.setBatch(entity.getBatch());
        vo.setContractCode(entity.getContractCode());
        vo.setClientCode(entity.getClientCode());
        vo.setClientName(entity.getClientName());
        vo.setOrgId(entity.getOrgId());
        vo.setFinancialContractStatus(entity.getFinancialContractStatus());
        vo.setReceivableRent(entity.getReceivableRent());
        vo.setReceivableResidualValue(entity.getReceivableResidualValue());
        vo.setReceivableOuttax(entity.getReceivableOuttax());
        vo.setUnrealizedRevenue(entity.getUnrealizedRevenue());
        vo.setLesseeMargin(entity.getLesseeMargin());
        vo.setDepreciationReserves(entity.getDepreciationReserves());
        vo.setAppraisedValue(entity.getAppraisedValue());
        vo.setTransferOpen(entity.getTransferOpen());
        vo.setSupplementaryProvision(entity.getSupplementaryProvision());

        vo.setBaseDateAccruedIncome(entity.getBaseDateAccruedIncome());
        vo.setBaseDateProvision(entity.getBaseDateProvision());
        vo.setBaseDateReceive(entity.getBaseDateReceive());
        vo.setBaseDateInvoices(entity.getBaseDateInvoices());
        return vo;
    }
}
