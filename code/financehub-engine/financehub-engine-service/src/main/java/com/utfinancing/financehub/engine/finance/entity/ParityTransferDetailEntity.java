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
 * 平价转让详情实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_parity_transfer_detail")
public class ParityTransferDetailEntity extends Model<ParityTransferDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //平价转让id
    private Long parityTransferId;

    //转让批次
    private String batch;

    //合同编码
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //签约主体
    private String orgId;

    //财务合同状态
    private String financialContractStatus;

    //税率
    private String taxRate;

    //应收租金余额
    private BigDecimal receivableRent;

    //应收期末残值余额
    private BigDecimal receivableResidualValue;

    //应收销项税余额
    private BigDecimal receivableOuttax;

    //未实现收益余额
    private BigDecimal unrealizedRevenue;

    //承租人保证金余额
    private BigDecimal lesseeMargin;

    //应付经销商服务费-暂估余额
    private BigDecimal payableAgencyEstimate;

    //应付收车费-暂估余额
    private BigDecimal payableVehicleEstimate;

    //应付手环成本_暂估余额
    private BigDecimal payableBandCostEstimate;

    //应付抵押费_暂估余额
    private BigDecimal payablePledgeEstimate;

    //应付解抵押费_暂估余额
    private BigDecimal payableUnpledgeEstimate;

    //应付其他租赁成本-暂估余额
    private BigDecimal payableOtherCostEstimate;

    //减值准备余额-应收租赁款组合拨备
    private BigDecimal depreciationReserves;

    //评估价
    private BigDecimal appraisedValue;

    //凭证id,多个按照逗号分隔
    private String voucherIds;

    //是否删除（0：未删除1：删除）默认0
    @TableLogic
    private String delFlag;

    //报错信息
    private String errorInfo;

    //会计期间
    private Integer periodCode;

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
