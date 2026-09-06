package com.utfinancing.financehub.etl.financial.entity;

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
 * 金蝶科目表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_kingdee_account")
public class KingdeeAccountEntity extends Model<KingdeeAccountEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //金蝶T_BD_ACCOUNTVIEW主键ID
    private String easId;

    //科目名称
    private String accountName;

    //科目编码
    private String accountCode;

    //签约主体编码
    private String orgId;

    //凭证类型编码
    private String accountTypeCode;

    //凭证类型名称
    private String accountTypeName;

    //借贷方向
    private String drcrType;

    //是否叶子节点 1:是 0:否
    private String leafFlag;

    //科目层级
    private Integer accountLevel;

    //科目长名称
    private String accountFullName;

    //科目长编码
    private String accountFullCode;

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
    private String delFlag;


}
