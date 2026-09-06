package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 合同状态记录表DTO对象
 * @Modified :
 */
@Data
public class ContractStatusRecordDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "公司")
    private String orgId;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态更新时间")
    private Date financialContractStatusUpdateTime;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "转入公司")
    private String transferOrgId;

    @ApiModelProperty(value = "转入合同号")
    private String transferContractCode;

    @ApiModelProperty(value = "转入合同系统合同状态")
    private String transferContractStatus;

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
