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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 线下合同实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_offline_contract")
public class OfflineContractEntity extends Model<OfflineContractEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //主合同编号
    private String contractCodeM;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //客户类别
    private String clientType;

    //签约主体
    private String orgId;

    //起租日
    private Date leaseDateStart;

    //到期日
    private Date leaseDateEnd;

    //发票类型
    private String invoiceType;

    //租赁类型
    private String leaseType;

    //币种
    private String currencyType;

    //税率
    private BigDecimal taxRate;

    //收益计算
    private String incomeCalculate;

    //收益计提方式
    private String incomeProvisionMethod;

    //开票标识
    private String invoicingFlag;

    //还款标识 期初(下还),期末(上还)
    private String payMethod;

    //处理状态
    private String processStatus;

    //是否已生成凭证（0：未生成1：已生成）默认0
    private String isGenerateVoucher;

    //提交人
    private String submitBy;

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

    //凭证id
    private String voucherId;

    // 异常信息
    private String exceptionType;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

}
