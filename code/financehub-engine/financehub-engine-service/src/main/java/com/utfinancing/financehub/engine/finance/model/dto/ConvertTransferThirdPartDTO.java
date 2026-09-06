package com.utfinancing.financehub.engine.finance.model.dto;

import cn.hutool.core.date.DateUtil;
import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.utils.ValidationUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class ConvertTransferThirdPartDTO {
//    转让批次	受让方	基准日	交易日	转让价格	合同数量

    /**
     * 批次
     */
    @Excel(name = "批次")
    @NotNull(message = "批次不能为空")
    private String batch;

    /**
     * 受让方
     */
    @Excel(name = "受让方")
    @NotNull(message = "受让方不能为空")
    private String transfereeParty;


    /**
     * 基准日
     */
    @Excel(name = "基准日", dateFormat = "yyyy-MM-dd")
    @NotNull(message = "基准日不能为空")
    private Date referenceDate;

    /**
     * 交易日
     */
    @Excel(name = "交易日", dateFormat = "yyyy-MM-dd")
    @NotNull(message = "交易日不能为空")
    private Date tradeDate;

    /**
     * 转让价格
     */
    @Excel(name = "转让价格")
    @NotNull(message = "转让价格不能为空")
    private BigDecimal transferPrice;

    public ConvertTransferThirdPartEntity toEntity() {
        ValidationUtils.validate(this);
        ConvertTransferThirdPartEntity entity = new ConvertTransferThirdPartEntity();
        entity.setBatch(batch);
        entity.setTransfereeParty(transfereeParty);
        entity.setReferenceDate(DateUtil.toLocalDateTime(referenceDate).toLocalDate());
        entity.setBusinessDate(LocalDate.now());
        entity.setAccountDate(DateUtil.toLocalDateTime(tradeDate).toLocalDate());
        entity.setTradeDate(DateUtil.toLocalDateTime(tradeDate).toLocalDate());
        entity.setTransferPrice(transferPrice);
        entity.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        return entity;
    }
}
