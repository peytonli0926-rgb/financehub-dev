package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * ta重分类实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_ta_reclassification")
public class TaReclassificationEntity extends Model<TaReclassificationEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //重分类月份
    private LocalDateTime reclassificationMonth;

    //签约主体
    private String orgId;

    //TA重分类金额
    private BigDecimal taReclassificationAmount;

    //处理状态
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

    //是否已生成凭证(0-否，1-是)
    private String isGenerateVoucher;

    //凭证id,多个按照逗号分隔
    private String voucherId;

    //生成凭证报错信息
    private String errorInfo;

    //记账日期
    private LocalDateTime accountDate;

    //是否删除（0-否，1-是）
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
