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
 * 出租登记实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_rent_register")
public class RentRegisterEntity extends Model<RentRegisterEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //房产租赁合同编号
    private String contractCode;

    //资产编号（存在多个用,隔开）
    private String assetNumber;

    //转出时间
    private LocalDate transferOutDate;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //起租日
    private Date leaseDateStart;

    //到期日
    private Date leaseDateEnd;

    //租金总额
    private BigDecimal rentTotal;

    //租赁保证金
    private BigDecimal rentBond;

    //处理状态
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

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

    @ApiModelProperty(value = "版本号")
    private Integer versionNum;

}
