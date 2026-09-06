package com.utfinancing.financehub.engine.finance.model.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-02-27
 * @Description : Charge Off手工上传表VO对象
 * @Modified :
 */
@Data
public class ChargeOffVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "核销状态")
    private String verificationStatus;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "核销时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty(value = "财务核销敞口")
    private BigDecimal financialExpenseAmount;

    @ApiModelProperty(value = "拨备转回金额")
    private BigDecimal provisionReversalAmount;

    @ApiModelProperty(value = "拨备转回年份")
    private String provisionReversalYear;

    @ApiModelProperty(value = "税务核销日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date taxVerificationDate;

    @ApiModelProperty(value = "税务核销金额")
    private BigDecimal taxVerificationAmount;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    //坏账核销余额=财务核销敞口-累计拨备转回
    @ApiModelProperty(value = "坏账核销余额")
    private BigDecimal badDebtWriteOffBalance;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "签约主体")
    private String orgName;


}
