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
 * @Date : Create in 2025-11-21
 * @Description :   SpecialContractOverdueRecord查询from对象
 * @Modified :
 */
@ApiModel("SpecialContractOverdueRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialContractOverdueRecordQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "计提月份")
    private Date businessDate;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "上传账期")
    private String uploadPeriods;

    @ApiModelProperty(value = "是否逾期")
    private String laborOverdueMark;

    @ApiModelProperty(value = "上期实收期间")
    private Date previousPaidPeriod;
}
