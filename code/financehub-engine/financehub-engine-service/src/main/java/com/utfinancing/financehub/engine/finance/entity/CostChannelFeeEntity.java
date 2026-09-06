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
 * 成本类支付-经销商服务费、外部渠道费，海通渠道费实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_cost_channel_fee")
public class CostChannelFeeEntity extends Model<CostChannelFeeEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //渠道类型（"1=经销商服务费,2=外部渠道费,3=海通渠道费,4=收车费,5=抵押费,6=解抵押费,7=安装费，8=服务费,9=设备款"）
    private String channelType;

    //合同编号
    private String contractCode;

    //渠道方编码
    private String channelCode;

    //渠道方名称
    private String channelName;

    //主机厂
    private String hostFactory;

    //实付金额（含税）
    private BigDecimal actualAmount;

    //对应交易结构金额（不含税）
    private BigDecimal noTaxTransactionAmount;

    //交易结构调整类型
    private String structureType;

    //凭证id,多个以逗号分隔
    private String voucherIds;

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

    //是否删除（0：否，1：是）
    @TableLogic
    private String delFlag;


    //金额（不含税）
    private BigDecimal noTaxAmount;

    //税额
    private BigDecimal taxAmount;

    //业务日期
    private LocalDateTime businessDate;

    @ApiModelProperty("费用大类（费用大类(1:GPS,2:手环设备款,3:经销商服务费、外部渠道费、海通渠道费,4:收车费、抵押费、解抵押费)）")
    private String expenseMainCategoryType;

    @ApiModelProperty("是否自动生成0：否，1：是")
    private String isAutoGenerate;

    @ApiModelProperty(value = "是否区分合同状态")
    private String isContractStatus;

    @ApiModelProperty(value = "处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)")
    private String processStatus;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime financialDate;

    @ApiModelProperty("流程实例id")
    private Long processInstanceId;

    @ApiModelProperty("签约主体")
    private String orgId;


}
