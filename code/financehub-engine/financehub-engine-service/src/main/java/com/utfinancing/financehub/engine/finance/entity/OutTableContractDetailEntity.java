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
 * 出表ABS合同详情实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_out_table_contract_detail")
public class OutTableContractDetailEntity extends Model<OutTableContractDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //出表absId
    private Long outTableAbsId;

    //借款合同编码
    private String loanContractCode;

    //合同编号
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //签约主体
    private String orgId;

    //财务合同状态
    private String financialContractStatus;

    //封包日应收租金
    private BigDecimal receivableRent;

    //封包日应收残值
    private BigDecimal receivableResidualValue;

    //封包日应收销项税
    private BigDecimal receivableOuttax;

    //封包日为实现收益
    private BigDecimal unrealizedRevenue;

    //封包日承租人保证金
    private BigDecimal lesseeMargin;

    //凭证id,多个按照逗号分隔
    private String voucherId;

    //生成凭证报错信息
    private String errorInfo;

    //是否删除（0：未删除1：删除）默认0
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

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty("会计期间")
    private Integer periodCode;


}
