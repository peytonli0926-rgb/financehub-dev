package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 折价转让-租金计划实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_convert_transfer_plan")
public class ConvertTransferPlanEntity extends Model<ConvertTransferPlanEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //折价转让id
    private Long convertTransferId;

    //转让批次
    private String batch;

    //原合同编码
    private String oldContractCode;

    //新合同编码
    private String newContractCode;

    //计划日期
    private LocalDateTime planDate;

    //期数
    private Integer periods;

    //应收租金
    private BigDecimal receivableRent;

    //应收本金
    private BigDecimal receivablePrincipal;

    //应收利息
    private BigDecimal receivableInterest;

    //应收期末残值
    private BigDecimal receivableEndingSalvage;

    //凭证id(多个逗号分隔)
    private String voucherId;

    //财务日期
    private LocalDate accountDate;

    //生成凭证报错信息
    private String errorInfo;

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
