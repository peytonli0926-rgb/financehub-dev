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
 * 计提情况数据实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_accrual_situation_data")
public class AccrualSituationDataEntity extends Model<AccrualSituationDataEntity> {

    private static final long serialVersionUID = 1L;

    //id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //租赁类型
    private String businessCode;

    //开票/计提税率
    private BigDecimal accrualRate;

    //开票/计提项目
    private String accrualName;

    //提前开票类型
    private String preInvoiceType;

    //对应期数
    private String periodNum;

    //应开票主体id
    private String invoiceOrgId;

    //应开票主体名称
    private String invoiceOrgName;

    //应开票对象
    private String invoiceOrgObject;

    //应收日期
    private LocalDateTime receivableDate;

    //收款日期
    private LocalDateTime billingDate;

    //应收本金
    private BigDecimal receivablePrincipal;

    //应收利息
    private BigDecimal receivableIntrest;

    //应收租金/其他款项
    private BigDecimal receivableOther;

    //应开票/计提金额
    private BigDecimal accrualAmount;

    //应开票/计提税额
    private BigDecimal accrualTaxAmount;

    //实际开票主体id
    private String realInvoiceOrgId;

    //实际开票主体
    private String realInvoiceOrgName;

    //实际开票对象
    private String realInvoiceOrgObject;

    //实际开票/计提金额
    private BigDecimal realAccrualAmount;

    //实际开票/计提税率
    private BigDecimal realAccrualRate;

    //实际开票/计提税额
    private BigDecimal realAccrualTaxAmount;

    //开票日期
    private LocalDateTime invoiceDate;

    //发票号码
    private String invoiceNumber;

    //异常类型
    private String exceptType;

    //备注
    private String remarks;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标记
    @TableLogic
    private String delFlag;


}
