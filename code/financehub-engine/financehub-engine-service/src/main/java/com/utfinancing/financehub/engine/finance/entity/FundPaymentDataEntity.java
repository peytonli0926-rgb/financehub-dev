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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网银收付款数据表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_fund_payment_data")
public class FundPaymentDataEntity extends Model<FundPaymentDataEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //交易流水号(必填，需保证唯一)
    private String orderId;

    //业务事件
    private String businessOperation;

    //网银编号
    private String ebankNumber;

    //付款账号
    private String paymentBankNo;

    //收款开户行
    private String collectionAccountsBank;

    //合同号
    private String contractCode;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //网银金额
    private BigDecimal bankAmount;

    //备注
    private String comment;

    //付款单
    private String paymentOrder;

    //签约主体
    private String orgId;
    //支付方式
    private String paymentMethod;
    //票据类型
    private String billType;

    //币种
    private String currencyType;

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

    //是否生成凭证
    private String isGenerateVoucher;

    @ApiModelProperty("细分票据类型")
    private String segmentedBillType;

    @ApiModelProperty("业务日期操作付款日期")
    private String businessDate;

    @ApiModelProperty("票据号")
    private String billNumber;

    @ApiModelProperty("付款项目")
    private String paymentItem;

    @ApiModelProperty("付款类型")
    private String paymentType;

    // 付款批次号
    private String batchNumber;

    @ApiModelProperty("实际客户名称")
    private String actualClientName;
    @ApiModelProperty("实际客户code")
    private String actualClientCode;
}
