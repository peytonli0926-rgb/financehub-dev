package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 手工表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_manual")
public class ManualEntity extends Model<ManualEntity> {

    private static final long serialVersionUID = 1L;
    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //签约主体
    private String orgId;

    //会计期间
    private Integer periodCode;

    //业务日期
    private LocalDateTime businessDate;

    //记账日期（财务日期）
    private LocalDateTime voucherDate;

    //币种编码
    private String currencyCode;

    //处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

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
    private String delFlag = "0";

    //凭证类型
    private String voucherType;

    //凭证号
    private Long voucherNum;

    //摘要内容
    private String voucherSummary;

    //场景编码
    private String sceneCode;

    //场景名称
    private String sceneName;

    //细分场景
    private String subSceneType;

    //汇率
    private BigDecimal rate;

    //业务编码
    private String businessCode;

    //业务名称
    private String businessName;

    //审批报错信息
    private String errorInfo;

    @ApiModelProperty("是否冲销（0：否，1：是）")
    private String isWriteOff;

    @ApiModelProperty("数据来源:KJFP：开票认领")
    private String sourceFrom;

    //复核人工号
    private String recheckUserNo;

    //复核人姓名
    private String recheckUserName;

    //创建人姓名
    private String createUserName;

    @ApiModelProperty("数据来源Id")
    private Long sourceId;
}
