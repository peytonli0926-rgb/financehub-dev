package com.utfinancing.financehub.engine.verification.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description :   CourtCost查询from对象
 * @Modified :
 */
@ApiModel("CourtCost查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CourtCostQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate accountDate;

    @ApiModelProperty(value = "记账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAccountDate;

    @ApiModelProperty(value = "记账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAccountDate;

    @ApiModelProperty(value = "转费用金额")
    private BigDecimal transgerCostAmount;

    @ApiModelProperty(value = "处理状态(1-已录入，2-已提交，3-已复核，4-已传至金蝶)")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "诉讼费id集合")
    private List<Long> idList;

    @ApiModelProperty(value = "处理状态(1-已录入，2-已提交，3-已复核，4-已传至金蝶)集合")
    private List<String> processStatusList;
}
