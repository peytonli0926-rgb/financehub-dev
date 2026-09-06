package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QueryDetailListDataSubDTO implements Serializable {

    private Long id;

    //到账主体
    @ApiModelProperty(value = "到账主体", required=true)
    private String collectionAccountsBank;

    //网银编号-资金系统
    @ApiModelProperty(value = "网银编号-资金系统")
    private String ebankNumber;

    @ApiModelProperty(value = "认领主体")
    private String OrgId;

    @ApiModelProperty(value = "认领名称")
    private String OrgName;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "业务系统网银编号/批次号", required=true)
    private String ebankSerialNumber;

    //到账银行账号
    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    //业务系统
    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    //业务系统名称
    @ApiModelProperty(value = "业务系统名称")
    private String systemName;

    //网银确认日期
    @ApiModelProperty(value = "网银确认日期")
    private LocalDateTime businessHappenDate;

    @ApiModelProperty(value = "网银到账日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    // 网银到账金额
    @ApiModelProperty(value = "网银到账金额")
    private BigDecimal bankAmount;

    @ApiModelProperty(value = "剩余未确认金额")
    private BigDecimal remainNonConfirmAmount;

    @ApiModelProperty(value = "已确认金额")
    private BigDecimal confirmedAmount;

    @ApiModelProperty(value = "网银确认日期")
    private String ebankConfirmDate;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "网银确认内容")
    private String ebankConfirmComments;

    @ApiModelProperty(value = "确认金额")
    private BigDecimal claimAmount;

    @ApiModelProperty(value = "批处理号")
    private BigDecimal batchNo;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTime;

    /**
     * @description: 新增显示字段-业务系统的网银编号
     **/
    @ApiModelProperty(value = "业务系统的网银编号")
    private String businessEbankNumber;
}
