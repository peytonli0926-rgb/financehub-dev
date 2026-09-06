package com.utfinancing.financehub.engine.scene.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 业务场景实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_scene")
public class SceneEntity extends Model<SceneEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //场景编码
    private String sceneCode;

    //场景名称
    private String sceneName;

    //场景周期
    private String scenePeriod;

    //是否有效(0:无效,1:有效)
    private String enableFlag;

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

    @Version
    private Integer version;

}
