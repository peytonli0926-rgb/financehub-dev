package com.utfinancing.financehub.engine.rule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-14
 * @Description : 接口数据表公共对象
 * @Modified :
 */
@Getter
@Setter
public class ExecuteCommonDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "来源系统名称")
    private String systemName;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "交易流水号")
    private String orderId;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    private String sceneName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

//    @ApiModelProperty(value = "财务日期")
//    private Date financeDate;
//
//    @ApiModelProperty(value = "客户类型")
//    private String clientType;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "批次id")
    private Long batchId;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty(value = "财务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime financeDate;

    @ApiModelProperty(value = "是否提交凭证0：否，1：是")
    private String isSubmit;

    @ApiModelProperty(value = "外部接口创建时间")
    private LocalDateTime interfaceCreateTime;

    //制单人工号
    @ApiModelProperty(value = "制单人工号")
    private String createUserNo;

    //制单人姓名
    @ApiModelProperty(value = "制单人姓名")
    private String createUserName;

    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "是否逾期")
    private String isOverdued;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "外部接口id")
    private Long interfaceId;


}
