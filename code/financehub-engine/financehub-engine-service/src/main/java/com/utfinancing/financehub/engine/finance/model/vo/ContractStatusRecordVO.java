package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 合同状态记录表VO对象
 * @Modified :
 */
@Data
public class ContractStatusRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "公司")
    @Excel(name = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "财务合同状态更新时间")
    @Excel(name = "财务合同状态")
    private Date financialContractStatusUpdateTime;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "业务合同状态")
    @Excel(name = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "转入公司")
    @Excel(name = "转入公司(针对内部转让)")
    private String transferOrgId;

    @ApiModelProperty(value = "转入合同号")
    @Excel(name = "转入合同号(针对内部转让)")
    private String transferContractCode;

    @ApiModelProperty(value = "转入合同系统合同状态")
    @Excel(name = "转入合同系统合同状态(针对内部转让)")
    private String transferContractStatus;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "处理状态(1: 已录入,2: 已提交,3: 复核通过,4: 复核失败)")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=复核通过,4=复核失败")
    private String recordStatus;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
