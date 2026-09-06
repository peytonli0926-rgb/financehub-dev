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
 * 资金系统、业务系统网银编号映射表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_fund_business_system_ebank_mapping")
public class FundBusinessSystemEbankMappingEntity extends Model<FundBusinessSystemEbankMappingEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //勾稽编号
    private String matchNumber;

    //资金系统网银编号
    private String ebankNumber;

    //业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
    private String ebankSerialNumber;

    //勾稽金额
    private BigDecimal matchAmount;

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

    @ApiModelProperty("消息状态：NOT_EXECUTE:未执行,RUNNING:进行中,SUCCESS:成功,FAILED:失败")
    private String messageStatus;

    @ApiModelProperty("错误信息")
    private String errorInfo;


}
