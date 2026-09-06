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
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 邮储手续费详情实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_postal_storage_fee_details")
public class PostalStorageFeeDetailsEntity extends Model<PostalStorageFeeDetailsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //邮储手续费表id
    private Long postalStorageFeeId;

    //记账日期
    private Date accountDate;

    //业务日期
    private Date businessDate;

    //签约主体
    private String orgId;

    //合同编号
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //合同状态
    private String contractStatus;

    //邮储项目类型
    private String postalStorageProjectType;

    //起租日
    private Date leaseDateStart;

    //租赁期限
    private Integer leaseTerm;

    //支付手续费金额
    private BigDecimal payableProcedureCost;

    //当期分摊金额
    private BigDecimal allocationAmount;

    //分摊余额
    private BigDecimal allocationBalance;

    //已分摊期数
    private Integer allocatedPeriods;

    //凭证id
    private String voucherId;

    //是否删除 0：未删除1：已删除
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

    // 异常信息
    private String exceptionType;
}
