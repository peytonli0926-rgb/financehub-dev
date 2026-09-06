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
 * @Date : Create in 2024-03-15
 * @Description :   AssetAbsRedeem查询from对象
 * @Modified :
 */
@ApiModel("AssetAbsRedeem查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetAbsRedeemQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "出表期数")
    private String periods;

    @ApiModelProperty(value = "赎回开始日期")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "实际赎回日")
    private LocalDateTime actualDate;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "审批报错信息")
    private String approveErrorInfo;

    @ApiModelProperty(value = "记账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date startAccountDate;

    @ApiModelProperty(value = "记账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date endAccountDate;

    @ApiModelProperty(value = "期数数组")
    private List<String> periodsList;

    @ApiModelProperty(value = "借款合同编号数组")
    private List<String> loanContractCodeList;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "id集合")
    private List<Long> idList;
}
