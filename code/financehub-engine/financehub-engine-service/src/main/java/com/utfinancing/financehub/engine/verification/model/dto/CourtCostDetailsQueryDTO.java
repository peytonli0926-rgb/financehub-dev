package com.utfinancing.financehub.engine.verification.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description :   CourtCostDetails查询from对象
 * @Modified :
 */
@ApiModel("CourtCostDetails查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CourtCostDetailsQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "诉讼费转费用id")
    private Long courtCostId;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "记账日期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAccountDate;

    @ApiModelProperty(value = "记账日期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endAccountDate;

    @ApiModelProperty(value = "转费用金额")
    private BigDecimal transgerCostAmount;

    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

    @ApiModelProperty(value = "诉讼费转费用id集合")
    private List<Long> courtCostIdList;
}
