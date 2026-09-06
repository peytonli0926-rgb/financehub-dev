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

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-03
 * @Description :   ManualVoucher查询from对象
 * @Modified :
 */
@ApiModel("ManualVoucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ManualVoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "会计日期")
    private int periodCode;

    @ApiModelProperty(value = "业务开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startBusinessDate;

    @ApiModelProperty(value = "业务结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endBusinessDate;

    @ApiModelProperty(value = "记账开始日期(财务日期)")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startVoucherDate;

    @ApiModelProperty(value = "记账结束日期(财务日期)")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endVoucherDate;

    @ApiModelProperty(value = "凭证类型")
    private String voucherType;

    @ApiModelProperty(value = "摘要内容")
    private String voucherSummary;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "币种编码")
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    private String rate;

    @ApiModelProperty(value = "方向")
    private String direction;

    @ApiModelProperty(value = "原币金额")
    private String originalCurrencyAmount;

    @ApiModelProperty(value = "是否有现金流量（0：否，1：是）")
    private String isCashFlow;

    @ApiModelProperty(value = "现金流量标记")
    private String cashFlowMarker;

    @ApiModelProperty(value = "辅助帐摘要")
    private String subsidiaryAccount;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "手工Id")
    private Long manualId;
}
