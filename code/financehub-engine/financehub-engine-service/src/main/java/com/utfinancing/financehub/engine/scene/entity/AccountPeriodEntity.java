package com.utfinancing.financehub.engine.scene.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 会计期间实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_account_period")
public class AccountPeriodEntity extends Model<AccountPeriodEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //期间编码
    private Integer periodCode;

    //会计年度
    private Integer periodYear;

    //会计季度
    private Integer periodQuarter;

    //期间
    private Integer periodNumber;

    //开始日期
    private LocalDateTime beginDate;

    //结束日期
    private LocalDateTime endDate;

    //期间名称
    private String periodName;

    //金蝶主键ID
    private String easId;

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

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;


}
