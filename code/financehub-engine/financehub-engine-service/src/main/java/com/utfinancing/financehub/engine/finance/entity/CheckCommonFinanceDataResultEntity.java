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
 * 中台对账数据结果表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_common_finance_data_result")
public class CheckCommonFinanceDataResultEntity extends Model<CheckCommonFinanceDataResultEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //记录ID
    private Long recordId;

    //执行日期， 分区字段
    private LocalDateTime executeDate;

    //业务场景
    private String businessType;

    //关联字段
    private String joinField;

    //查询字段
    private String queryField;

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

    //对接业务系统代码
    private String dbCode;

    //展示字段
    private String showField;

    //金额对比结果字段
    private String compareResultField;

    //金额一致结果字段
    private String compareFlagField;

    private Integer periodCode;
}
