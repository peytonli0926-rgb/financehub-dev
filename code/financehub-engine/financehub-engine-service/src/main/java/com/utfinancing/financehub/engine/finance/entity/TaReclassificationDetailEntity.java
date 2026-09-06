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
 * ta重分类明细表实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_ta_reclassification_detail")
public class TaReclassificationDetailEntity extends Model<TaReclassificationDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //重分类月份
    private LocalDateTime reclassificationMonth;

    //业务系统编码
    private String systemCode;

    //签约主体
    private String orgId;

    //合同编号
    private String contractCode;

    //租赁大类
    private String businessCode;

    //合同状态
    private String contractStatus;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //到账主体
    private String bankOrgId;

    //业务系统网银编号
    private String ebankSerialNumber;

    //业务系统批扣流水号
    private String ebankBatchNo;

    //网银付款人名称
    private String ebankClientName;

    //运营部备注
    private String operationRemark;

    //TA/溢存款余额
    private BigDecimal taExcessBalance;

    //租金校验结果
    private String rentCheck;

    //网银校验结果
    private String ebankCheck;

    //TA重分类金额
    private BigDecimal taReclassificationAmount;

    //合同结束日期
    private LocalDateTime leaseDateEnd;

    //应收租金余额
    private BigDecimal receivableRentBalance;

    //备注
    private String remark;

    //特殊合同状态
    private String specialContractStatus;

    //TA重分类科目编码
    private String taAccountCode;

    //TA重分类科目名称
    private String taAccountName;

    //异常类型
    private String exceptionType;

    //处理状态
    private String processStatus;

    //流程实例id
    private Long processInstanceId;

    //是否已生成凭证(0-否，1-是)
    private String isGenerateVoucher;

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

    //ta重分类id
    private Long taReclassificationId;

    @ApiModelProperty(value = "重分类科目编码")
    private String accountCode;
}
