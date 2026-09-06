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
 * 报表-财务入库出库实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_report_finance_in_out")
public class ReportFinanceInOutEntity extends Model<ReportFinanceInOutEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //执行时间yyyy-MM-dd
    private String executeDate;

    //执行数据期间yyyyMM
    private Integer periodCode;

    //合同代码
    private String contractCode;

    //客户代码
    private String clientCode;
    //客户代码
    private String clientName;

    //签约主体
    private String orgId;

    //入库时间
    private String inboundDate;

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

    //回收设备减值
    private BigDecimal substractBalance;

    //净值
    private BigDecimal netWorth;

    //暂收款项
    private BigDecimal provisionalReceiptsBalance;

    //出库日期
    private String outboundDate;

    //出库类型
    private String outboundType;

    //处置金额
    private BigDecimal dealAmount;

    //应交销项税
    private BigDecimal receivableServiceOuttaxAmount;

    //融资租赁资产处置损益
    private BigDecimal profitLoss;

    //删除标记
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

    // 回收设备成本科目余额
    private BigDecimal recyclingEquipmentAccountBalance;

}
