package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 偿还计划变更记录实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_repayment_plan_change_records")
public class RepaymentPlanChangeRecordsEntity extends Model<RepaymentPlanChangeRecordsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //变更时间
    private LocalDateTime changeTime;

    //合同编码
    private String contractCode;

    //系统编码
    private String systemCode;

    //签约主体
    private String orgId;

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
    private String delFlag;

    // 0:起租|1:偿还计划变更
    private String leaseStartOrChange;

}
