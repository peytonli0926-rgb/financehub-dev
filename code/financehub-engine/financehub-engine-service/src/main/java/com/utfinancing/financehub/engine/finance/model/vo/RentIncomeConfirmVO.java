package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-10
 * @Description : 租金收入确认VO对象
 * @Modified :
 */
@Data
public class RentIncomeConfirmVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "记账月份")
    private Date accountMonth;

    @ApiModelProperty(value = "当月应收租金")
    private BigDecimal thisMonthReceivableRent;

    @ApiModelProperty(value = "当月计提税金")
    private BigDecimal thisMonthTax;

    @ApiModelProperty(value = "确认收入金额")
    private BigDecimal thisMonthRentIncome;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "凭证id(多个逗号分隔)")
    private String voucherId;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

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

    @ApiModelProperty(value = "凭证场景（多个逗号分隔）")
    private String sceneCode;

    @ApiModelProperty(value = "抵债资产收入凭证id")
    private String voucherIdDzzcsr;

    @ApiModelProperty(value = "抵债资产结转凭证id")
    private String voucherIdDzzcjz;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;
}
