package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel
public class ConvertTransferContractFeeDetailVO {

    private Long id;

    /**
     * 业务日期
     */
    @ApiModelProperty("业务日期")
    private LocalDate uploadDate;

    /**
     * 记账日期
     */
    @ApiModelProperty("记账日期")
    private LocalDate accountDate;

    /**
     * 签约主体
     */
    @ApiModelProperty("签约主体")
    private String orgId;

    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private String contractCode;

    /**
     * 费用类型
     */
    @ApiModelProperty("费用类型")
    private String transferFeeType;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal transferFee;

    private String voucherId;
    /**
     * 成本中心
     */
    @ApiModelProperty("成本中心")
    private String costCenter;

    public ConvertTransferContractFeeDetailVO(ConvertTransferContractFeeDetailEntity entity) {
        this.id = entity.getId();
        this.uploadDate = entity.getUploadDate();
        this.accountDate = entity.getAccountDate();
        this.orgId = entity.getOrgId();
        this.contractCode = entity.getContractCode();
        this.transferFeeType = entity.getTransferFeeType();
        this.transferFee = entity.getTransferFee();
        this.costCenter = entity.getCostCenter();
        this.voucherId = entity.getVoucherId();
    }
}
