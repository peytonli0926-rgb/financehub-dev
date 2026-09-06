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
import java.time.LocalDateTime;

/**
 * <p>
 * 折价转让-汇总实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_convert_transfer")
public class ConvertTransferEntity extends Model<ConvertTransferEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //批次
    private String batch;

    //业务日期
    private LocalDateTime businessDate;

    //财务日期
    private LocalDateTime financeDate;

    //转让方
    private String transferParty;

    //受让方
    private String transfereeParty;

    //基准日
    private LocalDateTime referenceDate;

    //交易日
    private LocalDateTime tradeDate;

    //转让价格
    private BigDecimal transferPrice;

    //合同数量
    private Integer contractNum;

    //转让后是否开发票（0：否，1：是）
    private String isInvoiceFlag;

    //流程id
    private Long processInstanceId;

    //1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
    private String processStatus;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //凭证id,多个按照逗号分隔
    private String voucherId;

    //生成凭证报错信息
    private String errorInfo;

    //记账日期
    private LocalDateTime accountDate;

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
