package com.utfinancing.financehub.engine.finance.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSecondDetailEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ManualProcessListDTO implements Serializable {

    //ID
    @ApiModelProperty(value = "DetailId")
    private Long id;
    @ApiModelProperty(value = "操作类型")
    private String operationType;
    @ApiModelProperty(value = "记账日期")
    private LocalDateTime businessHappenDate;

    @ApiModelProperty(value = "业务系统的网银编号")
    private String businessEbankNumber;

    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String ebankSerialNumber;
    @ApiModelProperty(value = "新业务系统批扣流水号")
    private String newEbankSerialNumber;
    @ApiModelProperty(value = "认领/冲销/调整金额")
    private String claimAmount;
    @ApiModelProperty(value = "原入账月份")
    private String incomeYmOld;
    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "手工凭证id(逗号分隔)")
    private String manualVoucherIds;

    @ApiModelProperty(value = "核销回款凭证id(逗号分隔)")
    private String writeOffVoucherId;

    @ApiModelProperty(value = "凭证id(逗号分隔)")
    private String voucherIds;
}
