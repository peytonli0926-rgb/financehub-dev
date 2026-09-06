package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import com.utfinancing.financehub.engine.utils.ExcelExportUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ApiModel
public class ConvertTransferThirdPartVO {
    //业务日期	记账日期	转让批次	转让方	受让方	基准日	交易日	转让价格	合同数量	处理状态
    private Long id;

    /**
     * 批次
     */
    @ApiModelProperty("批次")
    private String batch;

    /**
     * 转让方
     */
    @ApiModelProperty("转让方")
    private String transferParty;

    /**
     * 受让方
     */
    @ApiModelProperty("受让方")
    private String transfereeParty;

    /**
     * 业务日期 上传
     */
    @ApiModelProperty("业务日期")
    private LocalDate businessDate;

    /**
     * 财务日期
     */
    @ApiModelProperty("财务日期")
    private LocalDate financeDate;

    /**
     * 记账日期 上传的业务日期
     */
    @ApiModelProperty("记账日期")
    private LocalDate accountDate;

    /**
     * 基准日 上传
     */
    @ApiModelProperty("基准日")
    private LocalDate referenceDate;

    /**
     * 交易日 上传
     */
    @ApiModelProperty("交易日")
    private LocalDate tradeDate;

    /**
     * 转让价格 上传
     */
    @ApiModelProperty("转让价格")
    private BigDecimal transferPrice;

    /**
     * 合同数量 上传
     */
    @ApiModelProperty("合同数量")
    private Integer contractNum;

    /**
     * 处理状态
     *
     * @see com.utfinancing.financehub.engine.enums.ProcessStatusEnum
     */
    @ApiModelProperty("处理状态")
    private String processStatus;


    /**
     * 是否已生成凭证（0：未生成1：已生成）默认0
     */
    private String isGenerateVoucher;

    /**
     * 凭证id,多个按照逗号分隔
     *
     * @see VoucherEntity
     */
    private String voucherId;

    public static ConvertTransferThirdPartVO from(ConvertTransferThirdPartEntity convertTransferThirdPartEntity) {
        ConvertTransferThirdPartVO vo = new ConvertTransferThirdPartVO();
        vo.setId(convertTransferThirdPartEntity.getId());
        vo.setBatch(convertTransferThirdPartEntity.getBatch());
        vo.setTransferParty(convertTransferThirdPartEntity.getTransferParty());
        vo.setTransfereeParty(convertTransferThirdPartEntity.getTransfereeParty());
        vo.setBusinessDate(convertTransferThirdPartEntity.getBusinessDate());
        vo.setFinanceDate(convertTransferThirdPartEntity.getFinanceDate());
        vo.setAccountDate(convertTransferThirdPartEntity.getAccountDate());
        vo.setReferenceDate(convertTransferThirdPartEntity.getReferenceDate());
        vo.setTradeDate(convertTransferThirdPartEntity.getTradeDate());
        vo.setTransferPrice(convertTransferThirdPartEntity.getTransferPrice());
        vo.setContractNum(convertTransferThirdPartEntity.getContractNum());
        vo.setProcessStatus(convertTransferThirdPartEntity.getProcessStatus());
        vo.setIsGenerateVoucher(convertTransferThirdPartEntity.getIsGenerateVoucher());
        vo.setVoucherId(convertTransferThirdPartEntity.getVoucherId());
        return vo;
    }
}
