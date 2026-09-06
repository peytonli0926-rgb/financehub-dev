package com.utfinancing.financehub.engine.finance.entity;

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
 * 长期应收款-分摊表实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_long_apportion")
public class LongApportionEntity extends Model<LongApportionEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //长期应收款编号
    private String longReceivableNumber;

    //合同编号
    private String contractCode;

    //应收日期
    private LocalDateTime receivableDate;

    //应收总额
    private BigDecimal receivableTotal;

    //应收本金
    private BigDecimal receivablePrincipal;

    //应收利息
    private BigDecimal receivableInterest;

    //剩余本金
    private BigDecimal residualPrincipal;

    //摊余成本
    private BigDecimal amortizedCost;

    //确认收入
    private BigDecimal confirmIncome;

    //凭证id(多个逗号分隔)
    private String voucherId;

    //财务日期
    private LocalDateTime accountDate;

    //生成凭证报错信息
    private String errorInfo;

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

    @ApiModelProperty(value = "长期应收款id")
    private Long longRegisterId;

}
