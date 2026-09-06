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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租金收入确认实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_rent_income_confirm")
public class RentIncomeConfirmEntity extends Model<RentIncomeConfirmEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //签约主体
    private String orgId;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //记账月份
    private Date accountMonth;

    //当月应收租金
    private BigDecimal thisMonthReceivableRent;

    //当月计提税金
    private BigDecimal thisMonthTax;

    //确认收入金额
    private BigDecimal thisMonthRentIncome;

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

    //凭证场景（多个逗号分隔）
    private String sceneCode;
    //抵债资产收入凭证id
    private String voucherIdDzzcsr;
    //抵债资产结转凭证id
    private String voucherIdDzzcjz;
    //是否已生成凭证(0-否，1-是)
    private String isGenerateVoucher;


}
