package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-05
 * @Description :   TailDifferenceAdjustmentDetail查询from对象
 * @Modified :
 */
@ApiModel("TailDifferenceAdjustmentDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class TailDifferenceAdjustmentDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "尾差调整Id",required = true)
    private Long tailDifferenceAdjustmentId;

    @ApiModelProperty(value = "签约主体Id集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "最小科目余额")
    private BigDecimal minAccountBalance;

    @ApiModelProperty(value = "最大科目余额")
    private BigDecimal maxAccountBalance;

    @ApiModelProperty(value = "尾差id集合")
    private List<Long> tailDifferenceIdList;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "合同状态集合")
    private String contractStatusList;

}
