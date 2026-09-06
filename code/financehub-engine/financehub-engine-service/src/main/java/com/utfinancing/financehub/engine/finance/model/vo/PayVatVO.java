package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : 应交增值税VO对象
 * @Modified :
 */
@Data
public class PayVatVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同id")
    private Long contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同类型")
    private String contractType;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "开票主体")
    private String orgId;

    @ApiModelProperty(value = "开票主体名称")
    private String orgName;

    @ApiModelProperty(value = "应开税额")
    private BigDecimal taxPayable;

    @ApiModelProperty(value = "已计提税额")
    private BigDecimal taxAccrued;

    @ApiModelProperty(value = "已开票税额")
    private BigDecimal taxInvoiced;

    @ApiModelProperty(value = "实际剩余税额")
    private BigDecimal actualTaxBalance;

    @ApiModelProperty(value = "应剩余税额（按计划）")
    private BigDecimal shouldTaxBalance;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "报表余额")
    private BigDecimal reportBalance;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "租赁合同编号")
    private String contractCodeM;

    @ApiModelProperty(value = "服务费实收金额")
    private BigDecimal serviceFeeReceived;

    @ApiModelProperty(value = "服务费实收日期")
    private Date serviceFeeReceivedDate;

    @ApiModelProperty(value = "留购价余额")
    private BigDecimal retainedPriceBalance;

    @ApiModelProperty(value = "合同起租日")
    private LocalDateTime leaseDateStart;

    @ApiModelProperty(value = "合同到期日")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("凭证id 多个以逗号分隔")
    private String voucherId;

    @ApiModelProperty("凭证报错信息")
    private String errorInfo;

    @ApiModelProperty("记账日期")
    private LocalDateTime accountDate;

}
