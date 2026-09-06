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
 * ta其他应付款明细实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_ta_other_payable_detail")
public class TaOtherPayableDetailEntity extends Model<TaOtherPayableDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //重分类月份
    private LocalDateTime reclassificationMonth;

    //网银到账主体
    private String bankOrgId;

    //业务系统编码
    private String systemCode;

    //业务系统网银编号
    private String ebankSerialNumber;

    //业务系统批扣流水号
    private String ebankBatchNo;

    //财务初分类
    private String financialPrimaryClassic;

    //运营部确认款项性质
    private String confirmAccountProperty;

    //重分类金额
    private BigDecimal reclassificationAmount;

    //重分类科目编码
    private String accountCode;

    //重分类科目名称
    private String accountName;

    //入账日期
    private LocalDateTime accountDate;

    //账龄
    private Integer accountAge;

    //账龄分类
    private String accountAgeClass;

    //付款客户
    private String payClientName;

    //处理状态
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

    //是否已生成凭证(0-否，1-是)
    private String isGenerateVoucher;

    //凭证id,多个按照逗号分隔
    private String voucherId;

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

    //ta其他应付款汇总id
    private Long taOtherPayableId;


}
