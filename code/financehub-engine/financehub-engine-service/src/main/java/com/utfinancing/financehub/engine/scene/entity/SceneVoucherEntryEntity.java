package com.utfinancing.financehub.engine.scene.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import com.utfinancing.financehub.common.mybatis.handler.ListToVarcharTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 场景凭证分录配置;实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "eg_scene_voucher_entry", autoResultMap = true)
public class SceneVoucherEntryEntity extends Model<SceneVoucherEntryEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //场景ID
    private Long sceneVoucherId;

    //金额类型;refDict
    private String fundType;

    //是否银行账号相关(0:否 1是)
    private String relateBankFlag;

    //银行账号
    private String bankAccount;

    //现金流属性
    private String cashAttribute;

    //凭证摘要
    private String voucherSummary;

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

    //是否现金流属性相关(0:否 1:是)
    private String cashAttributeFlag;

    //凭证行维度
    @TableField(typeHandler = ListToVarcharTypeHandler.class)
    private List<String> assistFlags;

}
