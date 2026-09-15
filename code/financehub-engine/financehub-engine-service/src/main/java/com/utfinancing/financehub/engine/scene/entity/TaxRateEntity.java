package com.utfinancing.financehub.engine.scene.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
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
 * 税率配置表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_tax_rate")
public class TaxRateEntity extends Model<TaxRateEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //业务编码
    private String businessCode;

    //金额类型
    private String fundType;

    //可选资产类别维度；金额类型不编码动产/不动产
    private String assetCategory;

    //是否有效(0:无效,1:有效)
    private String enableFlag;

    //生效时间
    private LocalDate enableDate;

    //税率
    private BigDecimal taxRate;

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

    //租赁类型
    private String leaseType;

    //租赁细类
    private String leaseSubType;


}
