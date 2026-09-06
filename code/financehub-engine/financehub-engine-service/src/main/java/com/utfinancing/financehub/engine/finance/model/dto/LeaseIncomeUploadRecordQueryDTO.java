package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : robjiang
 * @Date : Create in 2025-11-20
 * @Description :   LeaseIncomeUploadRecord查询from对象
 * @Modified :
 */
@ApiModel("LeaseIncomeUploadRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaseIncomeUploadRecordQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "计提月份")
    private Date businessDate;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "是否计提")
    private String accrued;

    @ApiModelProperty(value = "是否逾期")
    private String laborOverdueMark;

    @ApiModelProperty(value = "计提方式(XIRR分摊收益/实收/IRR分摊收益)")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "上期实收期间")
    private Date previousPaidPeriod;
}
