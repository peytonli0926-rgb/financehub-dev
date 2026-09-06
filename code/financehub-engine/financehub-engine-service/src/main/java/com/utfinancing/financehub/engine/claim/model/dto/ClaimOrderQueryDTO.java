package com.utfinancing.financehub.engine.claim.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description :   ClaimOrder查询from对象
 * @Modified :
 */
@ApiModel("ClaimOrder查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClaimOrderQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "消息ID")
    private String messageId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "单据类型")
    private String orderType;

    @ApiModelProperty(value = "申请单号")
    private String applyNo;

    @ApiModelProperty(value = "报销人")
    private String claimName;

    @ApiModelProperty(value = "个人所属公司")
    private String belongCompany;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "本位币金额")
    private String baseAmount;

    @ApiModelProperty(value = "申请金额")
    private String applyAmount;

    @ApiModelProperty(value = "申请日期")
    private LocalDateTime applyDate;

    @ApiModelProperty(value = "单据状态")
    private String orderStatus;

    @ApiModelProperty(value = "报销金额")
    private String claimAmount;

    @ApiModelProperty(value = "凭证头编号")
    private String voucherHeaderNo;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "报销金额合计")
    private String claimTotalAmount;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "网约车发票张数")
    private Integer onlineCarInvoiceCount;

    @ApiModelProperty(value = "申请人")
    private String applyName;

    @ApiModelProperty(value = "出差类型")
    private String travelType;

    @ApiModelProperty(value = "目的")
    private String destination;

    @ApiModelProperty(value = "费用承担公司")
    private String costBearCompany;

    @ApiModelProperty(value = "费用承担部门")
    private String costBearDepartment;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "跨公司报销")
    private String crossCompanyClaim;

    @ApiModelProperty(value = "出差事由")
    private String travelReason;

    @ApiModelProperty(value = "事由")
    private String reason;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "提交日期")
    private LocalDateTime submitDate;

    @ApiModelProperty(value = "是否有QA申请")
    private String hasApplyOa;

    @ApiModelProperty(value = "客户")
    private String clientName;

    @ApiModelProperty(value = "进项税额")
    private String inputTaxAmount;

    @ApiModelProperty(value = "付款币别")
    private String paymentCurrencyType;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "是否明确事项")
    private String hasExplicitItem;

    @ApiModelProperty(value = "付款币种")
    private String paymentCurrencyCategory;

    @ApiModelProperty(value = "是否银行托收")
    private String hasBankMandate;

    @ApiModelProperty(value = "金额合计")
    private String totalAmount;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "费用类型")
    private List<String> expenseTypeList;

    @ApiModelProperty(value = "是否生成凭证")
    private String isGenerateVoucher;
}
