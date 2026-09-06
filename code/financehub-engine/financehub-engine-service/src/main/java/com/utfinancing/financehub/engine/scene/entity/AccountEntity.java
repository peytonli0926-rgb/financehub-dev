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
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.utfinancing.financehub.common.mybatis.handler.ListToVarcharTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 科目实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "eg_account", autoResultMap = true)
public class AccountEntity extends Model<AccountEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //业务编码
    private String businessCode;

    //业务名称
    private String businessName;

    //金额类型
    private String fundType;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //科目性质
    private String accountCategory;

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

    //余额方向（DR/CR）
    private String debitCreditType;

    //凭证维度-客户(0:否 1是)
    private String clientFlag;

    //凭证维度-合同(0:否 1是)
    private String contractFlag;

    //凭证行维度
    @TableField(typeHandler = ListToVarcharTypeHandler.class)
    private List<String> assistFlags;

    //核算类型
    private String settlementType;

}
