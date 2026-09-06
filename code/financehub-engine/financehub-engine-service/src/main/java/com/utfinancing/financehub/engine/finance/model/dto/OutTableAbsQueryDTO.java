package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description :   OutTableAbs查询from对象
 * @Modified :
 */
@ApiModel("OutTableAbs查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OutTableAbsQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date accountDate;


    @ApiModelProperty(value = "记账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date startAccountDate;


    @ApiModelProperty(value = "记账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date endAccountDate;

    @ApiModelProperty(value = "期数")
    private String periods;

    @ApiModelProperty(value = "期数数组")
    private List<String> periodsList;

    @ApiModelProperty(value = "管理人")
    private String administrator;

    @ApiModelProperty(value = "封包日")
    private LocalDateTime closeDate;

    @ApiModelProperty(value = "发行日")
    private LocalDateTime releaseDate;

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

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "借款合同编号数组")
    private List<String> loanContractCodeList;

    @ApiModelProperty(value = "合同编号集合")
    private List<String> contractCodeList;
}
