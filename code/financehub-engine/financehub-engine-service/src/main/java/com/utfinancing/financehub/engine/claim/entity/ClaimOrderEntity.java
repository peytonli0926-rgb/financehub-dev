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
@TableName("eg_claim_order")
public class ClaimOrderEntity extends Model<ClaimOrderEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //消息ID
    private String messageId;

    //单据编号
    private String orderNo;

    //单据类型
    private String orderType;

    //申请单号
    private String applyNo;

    //报销人
    private String claimName;

    //个人所属公司
    private String belongCompany;

    //部门
    private String department;

    //本位币金额
    private BigDecimal baseAmount;

    //申请金额
    private BigDecimal applyAmount;

    //申请日期
    private LocalDateTime applyDate;

    //单据状态
    private String orderStatus;

    //报销金额
    private BigDecimal claimAmount;

    //凭证头编号
    private String voucherHeaderNo;

    //币种
    private String currencyType;

    //报销金额合计
    private BigDecimal claimTotalAmount;

    //联系电话
    private String contactPhone;

    //网约车发票张数
    private Integer onlineCarInvoiceCount;

    //申请人
    private String applyName;

    //出差类型
    private String travelType;

    //目的
    private String destination;

    //费用承担公司
    private String costBearCompany;

    //费用承担部门
    private String costBearDepartment;

    //开始时间
    private LocalDateTime startTime;

    //结束时间
    private LocalDateTime endTime;

    //跨公司报销
    private String crossCompanyClaim;

    //出差事由
    private String travelReason;

    //事由
    private String reason;

    //公司
    private String company;

    //提交日期
    private LocalDateTime submitDate;

    //是否有QA申请
    private String hasApplyOa;

    //客户
    private String clientName;

    //进项税额
    private BigDecimal inputTaxAmount;

    //付款币别
    private String paymentCurrencyType;

    //备注
    private String comment;

    //是否明确事项
    private String hasExplicitItem;

    //付款币种
    private String paymentCurrencyCategory;

    //是否银行托收
    private String hasBankMandate;

    //金额合计
    private BigDecimal totalAmount;

    //不含税金额
    private BigDecimal noTaxAmount;

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

    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;


}
