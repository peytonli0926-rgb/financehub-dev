package com.utfinancing.financehub.engine.finance.entity;

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
 * 科目余额与明细余额对账记录表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_account_detail_record")
public class CheckAccountDetailRecordEntity extends Model<CheckAccountDetailRecordEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //会计期间
    private Integer periodCode;

    //对账提交人
    private String submitBy;

    //对账类型：JOB:定时任务执行,MANUAL:界面手动执行
    private String checkType;

    //执行状态：In-Progress,Finish,Error
    private String executeStatus;

    //对账开始时间
    private LocalDateTime startTime;

    //对账结束时间
    private LocalDateTime endTime;

    //删除标志
    @TableLogic
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //对账目标 明细余额 Detail 金蝶 Kingdee 金蝶中间表 Middle
    @TableField(fill = FieldFill.INSERT)
    private String checkTarget;
}
