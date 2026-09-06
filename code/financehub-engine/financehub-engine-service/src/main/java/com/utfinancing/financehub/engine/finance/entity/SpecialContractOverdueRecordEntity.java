package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 特殊合同逾期上传记录实体对象
 * </p>
 *
 * @author robjiang
 * @since 2025-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_special_contract_overdue_record")
public class SpecialContractOverdueRecordEntity extends Model<SpecialContractOverdueRecordEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //计提月份
    private Date businessDate;

    //合同编号
    private String contractCode;

    //上传账期
    private String uploadPeriods;

    //是否逾期
    private String laborOverdueMark;

    //上期实收期间
    private Date previousPaidPeriod;

    //是否删除 0：未删除1：已删除
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
