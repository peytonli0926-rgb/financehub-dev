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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 减值计提实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_impairment_provision")
public class ImpairmentProvisionEntity extends Model<ImpairmentProvisionEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //减值类型
    private String impairmentType;

    //拨备合计
    private BigDecimal provisionTotal;

    //上月余额
    private BigDecimal lastMonthBalance;

    //本月计提
    private BigDecimal thisMonthProvision;

    //处理状态
    private String processStatus;

    //是否已生成凭证(0-否，1-是)
    private String isGenerateVoucher;

    //流程实例id
    private Long processInstanceId;

    //凭证id,多个按照逗号分隔
    private String voucherId;

    //生成凭证报错信息
    private String errorInfo;

    //财务日期
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

    //冲销来源id
    private Long writeOffOriginalId;

    //是否被冲销（0：否，1：是）
    private String isWriteOff;

    /**
     * @description: 减值计提汇总凭证ID
     **/
    private String summaryId;

    /**
     * @description: 汇总凭证ID,多个按照逗号分隔
     **/
    private String summaryVoucherId;


}
