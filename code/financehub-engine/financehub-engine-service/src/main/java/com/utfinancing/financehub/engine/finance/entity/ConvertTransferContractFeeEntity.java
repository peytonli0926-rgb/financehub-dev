package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@TableName("eg_convert_transfer_contract_fee")
public class ConvertTransferContractFeeEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 业务日期
     */
    private LocalDate uploadDate;

    /**
     * 记账日期
     */
    private LocalDate accountDate;

    /**
     * 签约主体
     */
    private String orgId;

    /**
     * 金额
     */
    private BigDecimal transferFeeAmount;

    private String voucherId;

    private String errorInfo;
    /**
     * 审批id
     */
    private Long processInstanceId;
    /**
     * 审批状态
     */
    private String processStatus;

    /**
     * 是否删除（0-否，1-是）
     */
    @TableLogic
    private String delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
