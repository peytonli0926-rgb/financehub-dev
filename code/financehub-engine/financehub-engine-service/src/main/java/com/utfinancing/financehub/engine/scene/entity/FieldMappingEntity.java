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
 * 字段映射表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_field_mapping")
public class FieldMappingEntity extends Model<FieldMappingEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //来源系统编码
    private String systemCode;

    //字段编码
    private String fieldCode;

    //字段名称
    private String fieldName;

    //业务系统源数据
    private String sourceValue;

    //财务中台数据
    private String targetValue;

    //默认数据
    private String defaultValue;

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

    //目标字段
    private String targetFieldCode;

}
