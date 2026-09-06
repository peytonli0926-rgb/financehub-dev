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
 * 实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_recycling_equipment_in_detail")
public class RecyclingEquipmentInDetailEntity extends Model<RecyclingEquipmentInDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //入库日期
    private String inboundDate;

    //签约主体
    private String orgId;

    //合同编号
    private String contractCode;

    //财务合同状态
    private String financialContractStatus;

    //客户名称
    private String clientName;

    //应收租金
    private BigDecimal receivableRentBalance;

    //应收期末残值
    private BigDecimal receivableResidualValueBalance;

    //应收销项税
    private BigDecimal receivableOuttaxBalance;

    //未实现收益
    private BigDecimal unrealizedRevenueBalance;

    //承租人保证金
    private BigDecimal lesseeMarginBalance;

    //财务敞口
    private BigDecimal financialExposure;

    //回收设备成本
    private BigDecimal recyclingEquipmentCost;

    //入库时计提减值
    private BigDecimal provisionForImpairment;

    //生成凭证id 逗号隔开
    private String voucherId;

    //是否删除（0：未删除1：删除）默认0
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

    //客户代码
    private String clientCode;


    //生成凭证报错信息
    private String errorInfo;

    // 财务入库汇总表ID
    private Long recycleId;

}
