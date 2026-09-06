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
 * @Date : Create in 2024-03-05
 * @Description :   TailDifferenceAdjustment查询from对象
 * @Modified :
 */
@ApiModel("TailDifferenceAdjustment查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TailDifferenceAdjustmentQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    //对应凭证日期
    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8" )
    private Date businessDate;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8" )
    private Date accountDate;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "科目编码集合")
    private List<String> accountCodeList;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "id集合")
    private List<Long> idList;

    @ApiModelProperty(value = "最小科目余额")
    private BigDecimal minAccountBalance;

    @ApiModelProperty(value = "最大科目余额")
    private BigDecimal maxAccountBalance;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "金额类型余额")
    private String fundTypeBalance;
    private String fundTypeAmount;

    @ApiModelProperty(value = "业务日期")
    private String businessDateString;
    private Integer periodCode;
    private Integer lastPeriodCode;
}
