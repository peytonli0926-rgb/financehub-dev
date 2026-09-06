package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConvertTransferThirdPartDetailDTO {
    /**
     * 转让批次
     */
    @Excel(name = "转让批次")
    private String batch;

    /**
     * 原合同编码
     */
    @Excel(name = "原合同编码")
    private String contractCode;


    /**
     * 受让方
     */
    @Excel(name = "受让方")
    private String orgId;

    /**
     * 财务合同状态
     */
    @Excel(name = "财务合同状态")
    private String financialContractStatus;


    /**
     * 应收租金
     */
    @Excel(name = "应收租金")
    private BigDecimal receivableRent;

    /**
     * 应收期末残值
     */
    @Excel(name = "应收期末残值")
    private BigDecimal receivableResidualValue;

    /**
     * 应收销项税
     */
    @Excel(name = "应收销项税")
    private BigDecimal receivableOuttax;

    /**
     * 未实现融资租赁收益
     */
    @Excel(name = "未实现融资租赁收益")
    private BigDecimal unrealizedRevenue;

    /**
     * 承租人保证金
     */
    @Excel(name = "承租人保证金")
    private BigDecimal lesseeMargin;

    /**
     * 应收租赁款组合拨备
     */
    @Excel(name = "应收租赁款组合拨备")
    private BigDecimal depreciationReserves;

    /**
     * 评估价
     */
    @Excel(name = "评估价")
    private BigDecimal appraisedValue;

    /**
     * 转让时敞口
     */
    @Excel(name = "转让时敞口")
    private BigDecimal transferOpen;

    /**
     * 补提拨备
     */
    @Excel(name = "补提拨备")
    private BigDecimal supplementaryProvision;


    public ConvertTransferThirdPartDetailEntity toEntity() {
        ConvertTransferThirdPartDetailEntity entity = new ConvertTransferThirdPartDetailEntity();

        entity.setBatch(batch);
        entity.setContractCode(contractCode);
        entity.setOrgId(orgId);
        entity.setFinancialContractStatus(financialContractStatus);
        entity.setReceivableRent(receivableRent);
        entity.setReceivableResidualValue(receivableResidualValue);
        entity.setReceivableOuttax(receivableOuttax);
        entity.setUnrealizedRevenue(unrealizedRevenue);
        entity.setLesseeMargin(lesseeMargin);
        entity.setDepreciationReserves(depreciationReserves);
        entity.setAppraisedValue(appraisedValue);
        entity.setTransferOpen(transferOpen);
        entity.setSupplementaryProvision(supplementaryProvision);
        return entity;
    }
}
