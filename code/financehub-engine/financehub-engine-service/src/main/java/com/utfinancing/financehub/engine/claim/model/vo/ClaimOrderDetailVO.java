package com.utfinancing.financehub.engine.claim.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description : 报销系统-报销明细表VO对象
 * @Modified :
 */
@Data
public class ClaimOrderDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "报销单主表ID")
    private Long claimOrderId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "费用承担公司")
    private String costBearCompany;

    @ApiModelProperty(value = "费用承担部门")
    private String costBearDepartment;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "单据状态")
    private String orderStatus;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "报销金额")
    private String claimAmount;

    @ApiModelProperty(value = "接待时间")
    private String receptionTime;

    @ApiModelProperty(value = "人数")
    private Integer headCount;

    @ApiModelProperty(value = "人均")
    private String headAvgAmount;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "招待活动项目")
    private String serveActivityProject;

    @ApiModelProperty(value = "说明")
    private String description;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "出发城市")
    private String departCity;

    @ApiModelProperty(value = "到达城市")
    private String arriveCity;

    @ApiModelProperty(value = "交通工具")
    private String transportMeans;

    @ApiModelProperty(value = "出发日期")
    private LocalDateTime departDate;

    @ApiModelProperty(value = "出行人")
    private String tripName;

    @ApiModelProperty(value = "月结金额")
    private String monthlyAmount;

    @ApiModelProperty(value = "入住日期")
    private LocalDateTime checkInDate;

    @ApiModelProperty(value = "离店日期")
    private LocalDateTime checkOutDate;

    @ApiModelProperty(value = "住宿城市")
    private String stayCity;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "客户编号")
    private String clientNo;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "报销年月")
    private LocalDateTime claimYearMonth;

    @ApiModelProperty(value = "人均标准")
    private String perHeadStandard;

    @ApiModelProperty(value = "收款名称")
    private String receiptTitle;

    @ApiModelProperty(value = "支付金额")
    private String paymentAmount;

    @ApiModelProperty(value = "收款对象")
    private String payee;

    @ApiModelProperty(value = "乘车日期")
    private LocalDateTime rideDate;

    @ApiModelProperty(value = "用车城市")
    private String rideCity;

    @ApiModelProperty(value = "上车地点")
    private String getOnLocation;

    @ApiModelProperty(value = "下车地点")
    private String getOutLocation;

    @ApiModelProperty(value = "里程")
    private String mileage;

    @ApiModelProperty(value = "费用说明")
    private String expenseDescription;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "同城订单状态")
    private String lyOrderStatus;

    @ApiModelProperty(value = "出差城市")
    private String travelCity;

    @ApiModelProperty(value = "补贴标准")
    private String subsidyStandard;

    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "出差天数")
    private Integer tripDays;

    @ApiModelProperty(value = "补贴金额")
    private String subsidyAmount;

    @ApiModelProperty(value = "结算方式")
    private String settleMode;

    @ApiModelProperty(value = "拜访计划主键")
    private String visitingPlanId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "业务人员")
    private String businessStaff;

    @ApiModelProperty(value = "拜访地址")
    private String visitingAddress;

    @ApiModelProperty(value = "事前申请单")
    private String beforehandApplyNo;

    @ApiModelProperty(value = "费用发生日期")
    private LocalDateTime expenseHappenDate;

    @ApiModelProperty(value = "返回日期")
    private LocalDateTime backDate;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "预计此次差旅总金额")
    private String estimateTripTotalAmount;

    @ApiModelProperty(value = "预计金额")
    private String estimateAmount;

    @ApiModelProperty(value = "招待日期")
    private LocalDateTime serveDate;

    @ApiModelProperty(value = "招待对象")
    private String serveTarget;

    @ApiModelProperty(value = "用车人")
    private String passenger;

    @ApiModelProperty(value = "人员工号")
    private String employeeNo;

    @ApiModelProperty(value = "内容摘要")
    private String contentAbstract;

    @ApiModelProperty(value = "费用日期")
    private LocalDateTime expenseDate;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "收款人名称")
    private String receiptName;

    @ApiModelProperty(value = "支付方式")
    private String paymentWay;

    @ApiModelProperty(value = "网银编号")
    private String ebankNo;

    @ApiModelProperty(value = "费用金额")
    private String expenseAmount;

    @ApiModelProperty(value = "发票不含税金额")
    private String invoiceNoTaxAmount;

    @ApiModelProperty(value = "发票税额")
    private String invoiceTaxAmount;

    @ApiModelProperty(value = "报销日期")
    private LocalDateTime claimDate;

    @ApiModelProperty(value = "发票备注")
    private String invoiceComment;

    @ApiModelProperty(value = "可抵扣税额")
    private String taxDeductibleAmount;

    @ApiModelProperty(value = "预付金额")
    private String prePaymentAmount;

    @ApiModelProperty(value = "发票日期")
    private LocalDateTime invoiceDate;

    @ApiModelProperty(value = "预付对象")
    private String prePaymentTarget;

    @ApiModelProperty(value = "本次冲销金额")
    private String tradingAmount;

    @ApiModelProperty(value = "剩余可冲销金额")
    private String surplusTradingAmount;

    @ApiModelProperty(value = "租赁合同号")
    private String leaseContractNo;

    @ApiModelProperty(value = "转出税额")
    private String transferOutTaxAmount;

    @ApiModelProperty(value = "累计已冲销金额")
    private String allTradedAmount;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    @ApiModelProperty("费用类型编码")
    private String expenseTypeCode;

}
