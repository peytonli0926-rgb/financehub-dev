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
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 出租登记-租金计划实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_rent_register_detail")
public class RentRegisterDetailEntity extends Model<RentRegisterDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //应付日期
    private LocalDate planDate;

    //所属期
    private String period;

    //应收租金
    private BigDecimal receivableRent;

    //当月应收租金
    private BigDecimal thisMonthReceivableRent;

    //当月计提税金
    private BigDecimal thisMonthTax;

    //当月租金收入
    private BigDecimal thisMonthRentIncome;

    //凭证id(多个逗号分隔)
    private String voucherId;

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

    @ApiModelProperty(value = "出租登记id")
    private Long rentRegisterId;

}
