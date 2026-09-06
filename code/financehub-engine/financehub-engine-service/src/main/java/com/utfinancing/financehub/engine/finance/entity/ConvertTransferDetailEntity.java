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
import java.time.LocalDateTime;

/**
 * <p>
 * 折价转让-详情实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_convert_transfer_detail")
public class ConvertTransferDetailEntity extends Model<ConvertTransferDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //折价转让id
    private Long convertTransferId;

    //转让批次
    private String batch;

    //原合同编码
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
    private BigDecimal taxRate;

    //应收租金
    private BigDecimal receivableRent;

    //应收期末残值
    private BigDecimal receivableResidualValue;

    //应收销项税
    private BigDecimal receivableOuttax;

    //未实现融资租赁收益
    private BigDecimal unrealizedRevenue;

    //承租人保证金
    private BigDecimal lesseeMargin;

    //应收租赁款组合拨备
    private BigDecimal depreciationReserves;

    //评估价
    private BigDecimal appraisedValue;

    //转让时敞口
    private BigDecimal transferOpen;

    //补提拨备
    private BigDecimal supplementaryProvision;

    //收益确认
    private BigDecimal revenueRecognition;

    //应付经销商服务费-暂估
    private BigDecimal payableAgencyEstimate;

    //应付收车费-暂估
    private BigDecimal payableVehicleEstimate;

    //应付手环成本_暂估
    private BigDecimal payableBandCostEstimate;

    //应付抵押费_暂估
    private BigDecimal payablePledgeEstimate;

    //应付解抵押费_暂估
    private BigDecimal payableUnpledgeEstimate;

    //应付其他租赁成本-暂估
    private BigDecimal payableOtherCostEstimate;

    //基准日后计提收益
    private BigDecimal baseDateAccruedIncome;

    //基准日后计提拨备
    private BigDecimal baseDateProvision;

    //基准日后收款
    private BigDecimal baseDateReceive;

    //凭证id,多个按照逗号分隔
    private String voucherId;

    //生成凭证报错信息
    private String errorInfo;

    //记账日期
    private LocalDateTime accountDate;

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
