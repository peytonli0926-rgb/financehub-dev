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
public class ConvertTransferThirdPartExcelVO {
    /**
     * 批次
     */
    @ApiModelProperty("批次")
    @Excel(name = "批次")
    private String batch;

    /**
     * 转让方
     */
    @ApiModelProperty("转让方")
    @Excel(name = "转让方")
    private String transferParty;

    /**
     * 受让方
     */
    @ApiModelProperty("受让方")
    @Excel(name = "受让方")
    private String transfereeParty;

    /**
     * 业务日期 上传
     */
    @ApiModelProperty("业务日期")
    @Excel(name = "业务日期", dateFormat = "yyyy-MM-dd")
    private LocalDate businessDate;

    /**
     * 财务日期
     */
    @ApiModelProperty("财务日期")
    @Excel(name = "财务日期", dateFormat = "yyyy-MM-dd")
    private LocalDate financeDate;

    /**
     * 记账日期 上传的业务日期
     */
    @ApiModelProperty("记账日期")
    @Excel(name = "记账日期", dateFormat = "yyyy-MM-dd")
    private LocalDate accountDate;

    /**
     * 基准日 上传
     */
    @ApiModelProperty("基准日")
    @Excel(name = "基准日", dateFormat = "yyyy-MM-dd")
    private LocalDate referenceDate;

    /**
     * 交易日 上传
     */
    @ApiModelProperty("交易日")
    @Excel(name = "交易日", dateFormat = "yyyy-MM-dd")
    private LocalDate tradeDate;

    /**
     * 转让价格 上传
     */
    @ApiModelProperty("转让价格")
    @Excel(name = "转让价格")
    private BigDecimal transferPrice;

    /**
     * 合同数量 上传
     */
    @ApiModelProperty("合同数量")
    @Excel(name = "合同数量")
    private Integer contractNum;

    /**
     * 处理状态
     *
     * @see com.utfinancing.financehub.engine.enums.ProcessStatusEnum
     */
    @ApiModelProperty("处理状态")
    @Excel(name = "处理状态", handler = ExcelExportUtil.ProcessStatusExcelHandlerAdapter.class)
    private String processStatus;


    public static ConvertTransferThirdPartExcelVO from(ConvertTransferThirdPartEntity convertTransferThirdPartEntity) {
        ConvertTransferThirdPartExcelVO vo = new ConvertTransferThirdPartExcelVO();
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
        return vo;
    }
}
