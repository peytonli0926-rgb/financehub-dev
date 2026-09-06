package com.utfinancing.financehub.engine.claim.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 报销系统-报销明细表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_claim_order_detail")
public class ClaimOrderDetailEntity extends Model<ClaimOrderDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //报销单主表ID
    private Long claimOrderId;

    //单据编号
    private String orderNo;

    //费用承担公司
    private String costBearCompany;

    //费用承担部门
    private String costBearDepartment;

    //开始时间
    private LocalDateTime startTime;

    //结束时间
    private LocalDateTime endTime;

    //单据状态
    private String orderStatus;

    //费用类型
    private String expenseType;

    //报销金额
    private String claimAmount;

    //接待时间
    private String receptionTime;

    //人数
    private Integer headCount;

    //人均
    private String headAvgAmount;

    //城市
    private String city;

    //招待活动项目
    private String serveActivityProject;

    //说明
    private String description;

    //备注
    private String comment;

    //出发城市
    private String departCity;

    //到达城市
    private String arriveCity;

    //交通工具
    private String transportMeans;

    //出发日期
    private LocalDateTime departDate;

    //出行人
    private String tripName;

    //月结金额
    private String monthlyAmount;

    //入住日期
    private LocalDateTime checkInDate;

    //离店日期
    private LocalDateTime checkOutDate;

    //住宿城市
    private String stayCity;

    //合同编号
    private String contractNo;

    //客户编号
    private String clientNo;

    //客户名称
    private String clientName;

    //发票类型
    private String invoiceType;

    //报销年月
    private LocalDateTime claimYearMonth;

    //人均标准
    private String perHeadStandard;

    //收款名称
    private String receiptTitle;

    //支付金额
    private String paymentAmount;

    //收款对象
    private String payee;

    //乘车日期
    private LocalDateTime rideDate;

    //用车城市
    private String rideCity;

    //上车地点
    private String getOnLocation;

    //下车地点
    private String getOutLocation;

    //里程
    private String mileage;

    //费用说明
    private String expenseDescription;

    //税率
    private BigDecimal taxRate;

    //同城订单状态
    private String lyOrderStatus;

    //出差城市
    private String travelCity;

    //补贴标准
    private String subsidyStandard;

    //开始日期
    private LocalDateTime startDate;

    //结束日期
    private LocalDateTime endDate;

    //出差天数
    private Integer tripDays;

    //补贴金额
    private String subsidyAmount;

    //结算方式
    private String settleMode;

    //拜访计划主键
    private String visitingPlanId;

    //内容
    private String content;

    //业务人员
    private String businessStaff;

    //拜访地址
    private String visitingAddress;

    //事前申请单
    private String beforehandApplyNo;

    //费用发生日期
    private LocalDateTime expenseHappenDate;

    //返回日期
    private LocalDateTime backDate;

    //天数
    private Integer days;

    //预计此次差旅总金额
    private String estimateTripTotalAmount;

    //预计金额
    private String estimateAmount;

    //招待日期
    private LocalDateTime serveDate;

    //招待对象
    private String serveTarget;

    //用车人
    private String passenger;

    //人员工号
    private String employeeNo;

    //内容摘要
    private String contentAbstract;

    //费用日期
    private LocalDateTime expenseDate;

    //不含税金额
    private BigDecimal noTaxAmount;

    //税额
    private BigDecimal taxAmount;

    //收款人名称
    private String receiptName;

    //支付方式
    private String paymentWay;

    //网银编号
    private String ebankNo;

    //费用金额
    private String expenseAmount;

    //发票不含税金额
    private String invoiceNoTaxAmount;

    //发票税额
    private String invoiceTaxAmount;

    //报销日期
    private LocalDateTime claimDate;

    //发票备注
    private String invoiceComment;

    //可抵扣税额
    private String taxDeductibleAmount;

    //预付金额
    private String prePaymentAmount;

    //发票日期
    private LocalDateTime invoiceDate;

    //预付对象
    private String prePaymentTarget;

    //本次冲销金额
    private String tradingAmount;

    //剩余可冲销金额
    private String surplusTradingAmount;

    //租赁合同号
    private String leaseContractNo;

    //转出税额
    private String transferOutTaxAmount;

    //累计已冲销金额
    private String allTradedAmount;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;

    //费用类型编码
    private String expenseTypeCode;

    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;

    // 附件id
    private String attachId;

    // 云盘附件定位neid
    private Long attachNeid;


    // 云盘附件定位nsid
    private Integer attachNsid;

    // 是否下载过文件
    private String isDownloadFile;

    // 文件路径
    private String filePath;
}
