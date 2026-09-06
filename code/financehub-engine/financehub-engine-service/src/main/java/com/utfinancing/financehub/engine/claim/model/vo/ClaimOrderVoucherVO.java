package com.utfinancing.financehub.engine.claim.model.vo;

import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderVoucherVO</li>
 * <li>CreateTime : 2023/11/23 10:23</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "GPS生成凭证VO")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ClaimOrderVoucherVO extends ExecuteCommonDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal actuallyPaymentAmount;

    @ApiModelProperty(value = "支付不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "结转金额")
    private BigDecimal settlementAmount;

    @ApiModelProperty(value = "是否生成收票凭证 1：是")
    private int isReceiveInvoiceVoucher = 0;

    @ApiModelProperty(value = "诉讼费支付")
    private BigDecimal litigationExpensePayment;

    @ApiModelProperty(value = "诉讼费支付-应付未付款")
    private BigDecimal litigationExpensePayable;

    @ApiModelProperty(value = "成本中心")
    private String costBearDepartment;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "内容摘要")
    private String content;

    @ApiModelProperty(value = "中台费用类型")
    private String litigationExpenseType;

    @ApiModelProperty(value = "交易结构调整类型")
    private String transactionStructureAdjustType;

    @ApiModelProperty(value = "是否区分合同状态")
    private String differContractStatus;

    @ApiModelProperty(value = "会计期间YYYY.MM")
    private String period;

    @ApiModelProperty(value = "内容摘要")
    private String contentAbstract;

}
