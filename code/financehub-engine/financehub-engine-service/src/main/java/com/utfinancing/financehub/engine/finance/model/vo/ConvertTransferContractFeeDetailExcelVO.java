package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferContractFeeDetailExcelVO {
    /**
     * 业务日期
     */
    @Excel(name = "业务日期", dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("业务日期")
    private LocalDate uploadDate;

    /**
     * 记账日期
     */
    @Excel(name = "记账日期", dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("记账日期")
    private LocalDate accountDate;

    /**
     * 签约主体
     */
    @Excel(name = "签约主体")
    @ApiModelProperty("签约主体")
    private String orgId;

    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    @ApiModelProperty("合同编号")
    private String contractCode;

    /**
     * 费用类型
     */
    @Excel(name = "费用类型")
    @ApiModelProperty("费用类型")
    private String transferFeeType;

    /**
     * 金额
     */
    @Excel(name = "金额")
    @ApiModelProperty("金额")
    private BigDecimal transferFee;

    /**
     * 成本中心
     */
    @Excel(name = "成本中心")
    @ApiModelProperty("成本中心")
    private String costCenter;

    public ConvertTransferContractFeeDetailExcelVO(ConvertTransferContractFeeDetailEntity entity) {
        this.uploadDate = entity.getUploadDate();
        this.accountDate = entity.getAccountDate();
        this.orgId = entity.getOrgId();
        this.contractCode = entity.getContractCode();
        this.transferFeeType = entity.getTransferFeeType();
        this.transferFee = entity.getTransferFee();
        this.costCenter = entity.getCostCenter();
    }
}
