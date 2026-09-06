package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 长期应收款-收入确认实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_long_income_confirm")
public class LongIncomeConfirmEntity extends Model<LongIncomeConfirmEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //长期应收款编号
    private String longReceivableNumber;

    //合同编号
    private String contractCode;

    //项目名称
    private String projectName;

    //记账月份
    private Date accountMonth;

    //确认收入金额
    private BigDecimal confirmIncomeAmount;

    //处理状态
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

    //凭证id(多个逗号分隔)
    private String voucherId;

    //财务日期
    private LocalDateTime accountDate;

    //生成凭证报错信息
    private String errorInfo;

    //客户名称
    private String clientName;

    //客户编号
    private String clientCode;

    //签约主体
    private String orgId;

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

    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;

}
