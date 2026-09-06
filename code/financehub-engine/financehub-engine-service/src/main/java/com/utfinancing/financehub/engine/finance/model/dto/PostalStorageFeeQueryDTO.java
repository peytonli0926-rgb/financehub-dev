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
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description :   PostalStorageFee查询from对象
 * @Modified :
 */
@ApiModel("PostalStorageFee查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostalStorageFeeQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "记账月份")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

    @ApiModelProperty(value = "业务月份")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date businessDate;

    @ApiModelProperty(value = "签约主体（组织机构编码）")
    private String orgId;
    private List<String> orgIdList;

    @ApiModelProperty(value = "支付手续费金额")
    private BigDecimal payableProcedureCost;

    @ApiModelProperty(value = "当期分摊金额")
    private BigDecimal allocationAmount;

    @ApiModelProperty(value = "分摊余额")
    private BigDecimal allocationBalance;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "提交人")
    private String submitBy;
}
