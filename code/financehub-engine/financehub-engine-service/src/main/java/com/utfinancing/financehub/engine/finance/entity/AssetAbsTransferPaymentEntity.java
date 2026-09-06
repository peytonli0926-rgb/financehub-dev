package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资产转付实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_asset_abs_transfer_payment")
public class AssetAbsTransferPaymentEntity extends Model<AssetAbsTransferPaymentEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //业务日期
    private LocalDateTime businessDate;

    //记账日期
    private LocalDateTime accountDate;

    //借款合同编号
    private String loanContractCode;

    //出表期数
    private String periods;

    //流程id
    private Long processInstanceId;

    //1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
    private String processStatus;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //会计期间
    private Integer periodCode;

    //审批报错信息
    private String approveErrorInfo;

    //是否删除（0：未删除1：删除）默认0
    @TableLogic
    private String delFlag;

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


}
