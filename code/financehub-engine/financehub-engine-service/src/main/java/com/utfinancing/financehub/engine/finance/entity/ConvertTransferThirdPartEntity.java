package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_convert_transfer_third")
public class ConvertTransferThirdPartEntity extends Model<ConvertTransferThirdPartEntity> {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 批次
     */
    private String batch;

    /**
     * 转让方
     */
    private String transferParty;

    /**
     * 受让方
     */
    private String transfereeParty;

    /**
     * 业务日期 上传
     */
    private LocalDate businessDate;

    /**
     * 财务日期
     */
    private LocalDate financeDate;

    /**
     * 记账日期 上传的业务日期
     */
    private LocalDate accountDate;

    /**
     * 基准日 上传
     */
    private LocalDate referenceDate;

    /**
     * 交易日 上传
     */
    private LocalDate tradeDate;

    /**
     * 转让价格 上传
     */
    private BigDecimal transferPrice;

    /**
     * 合同数量 上传
     */
    private Integer contractNum;

    /**
     * 流程id
     */
    private Long processInstanceId;

    /**
     * 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
     *
     * @see com.utfinancing.financehub.engine.enums.ProcessStatusEnum
     */
    private String processStatus;

    /**
     * 是否已生成凭证（0：未生成1：已生成）默认0
     */
    private String isGenerateVoucher;

    /**
     * 凭证id,多个按照逗号分隔
     *
     * @see VoucherEntity
     */
    private String voucherId;

    /**
     * 生成凭证报错信息
     */
    private String errorInfo;

    /**
     * 是否删除（0：未删除1：删除）默认0
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
