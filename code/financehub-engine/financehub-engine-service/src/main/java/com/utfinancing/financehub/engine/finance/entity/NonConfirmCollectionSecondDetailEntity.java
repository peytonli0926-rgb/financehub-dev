package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 未确认收款明细表(第二层明细)实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_non_confirm_collection_second_detail")
public class NonConfirmCollectionSecondDetailEntity extends Model<NonConfirmCollectionSecondDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //未确认收款汇总表id
    private Long sumId;

    //业务系统
    private String systemCode;

    //业务系统名称
    private String systemName;

    //网银到账日期
    private LocalDateTime businessDate;

    //网银确认日期
    private LocalDateTime businessHappenDate;

    //币种
    private String currencyType;

    //网银到账金额
    private BigDecimal bankAmount;

    //剩余未确认金额
    private BigDecimal remainNonConfirmAmount;

    //已确认金额
    private BigDecimal confirmAmount;

    // 处理状态
    private String processStatus;
    // 审核ID
    private Long approveId;
    // 手工凭证id
    private String manualVoucherIds;
    // 核销回款凭证id
    private String writeOffVoucherId;

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
    @TableLogic
    private String delFlag;

    // 核销回款凭证生成异常信息
    private String wirteOffVoucherException;

    // 操作类型
    private String operationType;

    private String auditor;
    private String auditorName;

    private LocalDateTime approveTime;

    private BigDecimal curClaimAmount;

    private String writeOffDetailIds;
    // 是否涉及其他客户及辅助帐
    private String isRelateClientAuxiliaryAccount;
    // 凭证id
    private String voucherIds;
    // 上传文件ID
    private Long uploadFileId;
}
