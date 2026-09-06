package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同状态记录表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_status_record")
public class ContractStatusRecordEntity extends Model<ContractStatusRecordEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //公司
    private String orgId;

    //系统合同状态
    private String contractStatus;

    //财务合同状态更新时间
    private Date financialContractStatusUpdateTime;

    //财务合同状态
    private String financialContractStatus;

    //转入公司
    private String transferOrgId;

    //转入合同号
    private String transferContractCode;

    //转入合同系统合同状态
    private String transferContractStatus;

    //操作人
    private String operator;

    //状态(1: 已录入,2: 已提交,3: 复核通过,4: 复核失败)
    private String recordStatus;

    //提交人
    private String submitBy;

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

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

    @ApiModelProperty(value = "模块id")
    private Long sourceFromId;

    @ApiModelProperty(value = "模块类型")
    private String sourceFromType;

}
