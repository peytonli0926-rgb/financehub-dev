package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 业务系统对还款认领记录实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_business_claim_repayment_record")
public class BusinessClaimRepaymentRecordEntity extends Model<BusinessClaimRepaymentRecordEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String systemCode;

    //业务系统网银编号
    private String ebankSerialNumber;


    //认领金额
    private BigDecimal claimAmount;

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

    //是否删除（0:否，1：是）
    private String delFlag;

    //客户编号
    private String clientCode;

    //合同号
    private String contractCode;

    //币种
    private String currencyType;

    //业务系统交易流水号
    private String orderId;

    //业务日期(yyyy-MM-dd HH:mm:ss)
    private LocalDateTime businessDate;

    // 签约主体
    private String orgId;

    // 签约主体名称
    private String orgName;

    // 场景编码
    private String sceneCode;

    // 场景名称
    private String sceneName;

    // 处理批次号
    private BigDecimal batchNo;

    // 操作类型
    private String operationType;

    // 未确认收款明细表Id
    private Long nonConfirmSecondDetailId;

    // 处理状态
    private String processStatus;
    // 备注
    private String remark;
    // 新业务系统批扣流水号
    private String newEbankSerialNumber;
    // 原入账月份
    private String incomeYmOld;
    // 是否存在跨主体(0;否;1:是)
    private String isCrossOrg;
    // 系统自动认领的记录的凭证ids
    private String voucherIds;
}
