package com.utfinancing.financehub.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 中台内部角色和签约实体关联表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_internal_role_org")
public class SysInternalRoleOrgEntity extends Model<SysInternalRoleOrgEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //签约实体
    private String orgId;

    //签约实体名称
    private String orgName;

    //系统内部角色id
    private Long roleId;

    //是否删除（0：否：1：是）
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


}
