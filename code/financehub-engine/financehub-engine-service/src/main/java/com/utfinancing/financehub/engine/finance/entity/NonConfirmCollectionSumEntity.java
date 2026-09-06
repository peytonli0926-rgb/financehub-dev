package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 未确认收款汇总表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_non_confirm_collection_sum")
public class NonConfirmCollectionSumEntity extends Model<NonConfirmCollectionSumEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //网银编号-资金系统
    @ApiModelProperty(value = "网银编号-资金系统")
    private String ebankNumber;

    //到账主体
    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    //到账银行账号
    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    //业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)
    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String ebankSerialNumber;

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

    //是否删除（0:否，1：是）
    private String delFlag;


    //到账主体编码
    @ApiModelProperty(value = "到账主体编码")
    private String collectionAccountsBankCode;

    //业务系统的网银编号
    @ApiModelProperty(value = "业务系统的网银编号")
    private String businessEbankNumber;

    //资金系统、业务系统网银编号映射表ID
    @ApiModelProperty(value = "资金系统、业务系统网银编号映射表ID")
    private Long ebankMappingId;

    // 网银到账金额
    @ApiModelProperty(value = "网银到账金额")
    private BigDecimal bankAmount;

    // 应批扣金额
    @ApiModelProperty(value = "应批扣金额")
    private BigDecimal accountsReceivable;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "网银到账日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "银行交易摘要")
    private String bankSummary;
    @ApiModelProperty(value = "银行交易备注")
    private String comment;
    @ApiModelProperty(value = "对方客户银行账号")
    private String clientAccountsBankNo;
}
