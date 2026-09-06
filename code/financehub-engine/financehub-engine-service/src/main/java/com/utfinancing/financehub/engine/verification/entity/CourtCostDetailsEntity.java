package com.utfinancing.financehub.engine.verification.entity;

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
 * 诉讼费转费用详情表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_court_cost_details")
public class CourtCostDetailsEntity extends Model<CourtCostDetailsEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //诉讼费转费用id
    private Long courtCostId;

    //签约主体
    private String orgId;

    //合同编号
    private String contractCode;

    //记账日期
    private LocalDateTime accountDate;

    //转费用金额
    private BigDecimal transgerCostAmount;

    //成本中心
    private String costCenter;

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

    //凭证id
    private String voucherId;


}
