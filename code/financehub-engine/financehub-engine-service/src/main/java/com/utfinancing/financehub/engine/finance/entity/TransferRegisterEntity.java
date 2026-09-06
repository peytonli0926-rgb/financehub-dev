package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
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
 * 转入登记实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_transfer_register")
public class TransferRegisterEntity extends Model<TransferRegisterEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //资产编号
    private String assetNumber;

    //签约主体
    private String orgId;

    //入账时间
    private LocalDateTime accountDate;

    //原合同号
    private String contractCode;

    //房产地址
    private String propertyAddress;

    //抵债资产入账价值
    private BigDecimal debtAssetValue;

    //处理状态
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

    //是否删除（0-否，1-是）
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
