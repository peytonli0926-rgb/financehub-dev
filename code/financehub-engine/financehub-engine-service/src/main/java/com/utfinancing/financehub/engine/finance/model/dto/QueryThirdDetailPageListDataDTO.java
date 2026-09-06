package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QueryThirdDetailPageListDataDTO implements Serializable {

    private Long id;

    //到账主体
    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "认领主体")
    private String OrgId;

    @ApiModelProperty(value = "认领主体")
    private String OrgName;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String businessEbankNumber;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankSerialNumber;

    //业务系统
    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    //业务系统名称
    @ApiModelProperty(value = "业务系统名称")
    private String systemName;

    @ApiModelProperty(value = "最后变化日期")
    private Date lastChangeDate;

    @ApiModelProperty(value = "网银到账日期")
    private Date businessDate;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    // 网银到账金额
    @ApiModelProperty(value = "网银到账金额")
    private BigDecimal bankAmount;

    @ApiModelProperty(value = "剩余未确认金额")
    private BigDecimal remainNonConfirmAmount;

    @ApiModelProperty(value = "已确认金额")
    private BigDecimal confirmedAmount;

    @ApiModelProperty(value = "网银确认记录")
    private List<EbankConfirmRecordDTO> ebankConfirmRecord = new ArrayList<>();

    @ApiModelProperty(value = "网银是否业务相关")
    private String isRelationAndEbank;

    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    @ApiModelProperty(value = "到账银行账号名称")
    private String collectionAccountsBankNoName;

    @ApiModelProperty(value = "付款客户编码")
    private String clientCode;

    @ApiModelProperty(value = "付款客户")
    private String clientName;

    @ApiModelProperty(value = "银行交易摘要")
    private String bankSummary;

    @ApiModelProperty(value = "银行交易备注")
    private String comment;

    @ApiModelProperty(value = "付款方账户")
    private String clientAccountsBankNo;

    @ApiModelProperty(value = "历史网银编号调整记录")
    private String adjustmentRecordHistory;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;
}
