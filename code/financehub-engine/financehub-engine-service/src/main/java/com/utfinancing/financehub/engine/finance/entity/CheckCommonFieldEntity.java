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
 * 对账对接其他系统查询sql里的字段顺序实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_common_field")
public class CheckCommonFieldEntity extends Model<CheckCommonFieldEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //对接系统db
    private String dbCode;

    //业务场景
    private String businessType;

    //查询字段
    private String commonField;

    //字段类型 1 连接字段， 2 查询字段,  3显示字段
    private String fieldType;

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

    //sql里查询的字段顺序
    private Integer fieldOrder;


}
