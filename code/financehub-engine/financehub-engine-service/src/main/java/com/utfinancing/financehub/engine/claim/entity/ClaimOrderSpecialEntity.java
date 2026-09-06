package com.utfinancing.financehub.engine.claim.entity;

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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 报销系统-报销单专项费明细实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_claim_order_special")
public class ClaimOrderSpecialEntity extends Model<ClaimOrderSpecialEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //报销单主表ID
    private Long claimOrderId;

    //单据编号
    private String orderNo;

    //序号
    private String no;

    //组织
    private String org;

    //系统
    private String sourceSystem;

    //合同号
    private String contractNum;

    //承租人
    private String tenant;

    //账期
    private String accountDate;

    //单价
    private BigDecimal unitPrice;

    //设备款不含税额
    private BigDecimal equipNotaxAmount;

    //设备款进项税
    private BigDecimal equipInputTax;

    //安装费
    private BigDecimal installAmount;

    //安装费不含税额
    private BigDecimal installNotaxAmount;

    //安装费进项税
    private BigDecimal installInputTax;

    //服务费
    private BigDecimal serviceAmount;

    //服务费不含税额
    private BigDecimal serviceNotaxAmount;

    //服务费进项税
    private BigDecimal serviceInputTax;

    //供应商
    private String supplier;

    //是否转天津
    private String tianjiFlag;

    //付款金额
    private String paymentAmount;

    //不含税金额
    private String paymentNotaxAmount;

    //进项税
    private String paymentInputTax;

    //成本中心
    private String costCenter;

    //凭证标识
    private String voucherFlag;

    //item01
    private String item01;

    //item01
    private String item02;

    //item01
    private String item03;

    //item01
    private String item04;

    //item01
    private String item05;

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

    private String isGenerateVoucher;


}
