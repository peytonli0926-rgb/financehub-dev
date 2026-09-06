package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 投保事件累计金额实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_payable_insurance_interface_total")
public class PayableInsuranceInterfaceTotalEntity extends Model<PayableInsuranceInterfaceTotalEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编码
    private String contractCode;

    //签约主体
    private String orgId;

    //来源系统
    private String systemCode;

    //实际计划支付保险费
    private String actualPayableInsuaranceAmount;

    //应付保险费
    private String payableInsuranceAmount;

    //应付保险费余额
    private String payableInsuranceBalance;

    //是否可用1:可用 0:不可用
    private String enableFlag;

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


}
