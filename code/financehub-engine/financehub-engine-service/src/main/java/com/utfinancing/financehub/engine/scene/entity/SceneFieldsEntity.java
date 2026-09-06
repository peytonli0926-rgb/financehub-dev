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
 * 业务场景接口字段实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_scene_fields")
public class SceneFieldsEntity extends Model<SceneFieldsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //场景ID
    private Long sceneId;

    //场景编码
    private String sceneCode;

    //场景名称
    private String sceneName;

    //字段名称
    private String fieldName;

    //字段编码
    private String fieldCode;

    //数据类型
    private String dataType;

    //父字段ID；为空表示接口顶层字段，非空表示List字段的明细字段
    private Long parentId;

    //是否必填(0:否 1:是)
    private String requiredFlag;

    //明细字段排序
    private Integer sortNo;

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
