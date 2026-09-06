package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description : 出表ABSVO对象
 * @Modified :
 */
@Data
public class OutTableAbsVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    @ApiModelProperty(value = "业务日期")
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "期数")
    private String periods;

    @ApiModelProperty(value = "管理人")
    private String administrator;

    @ApiModelProperty(value = "封包日")
    private Date closeDate;

    @ApiModelProperty(value = "发行日")
    private Date releaseDate;

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "合同数量")
    private Integer contractNum;

    @ApiModelProperty(value = "计算周期")
    private String calculationPeriod;

    @ApiModelProperty(value = "转付周期")
    private String transferPeriod;

    @ApiModelProperty(value = "兑付周期")
    private String cashPeriod;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "审批报错信息")
    private String approveErrorInfo;

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

    @ApiModelProperty(value = "批量类型")
    private String batchType;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

}
