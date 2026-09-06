package com.utfinancing.financehub.engine.verification.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO</li>
 * <li>CreateTime : 2023/11/06 16:47</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "诉讼费报表VO")
@Data
public class CourtCostReportFormVO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编码",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    @Excel(name = "合同名称",width = 20)
    private String contractName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体名称",width = 20)
    private String orgName;

    @ApiModelProperty(value = "签约时间")
    private Date accountDate;

    @ApiModelProperty(value = "应收诉讼费科目-期初余额")
    @Excel(name = "应收诉讼费科目-期初余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal litigationExpensesBalance;

    @ApiModelProperty(value = "应收诉讼费科目-诉讼费支付")
    @Excel(name = "应收诉讼费科目-诉讼费支付",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentPay;

    @ApiModelProperty(value = "应收诉讼费科目-诉讼费收回")
    @Excel(name = "应收诉讼费科目-诉讼费收回",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentRecover;

    @ApiModelProperty(value = "应收诉讼费科目-诉讼费转费用")
    @Excel(name = "应收诉讼费科目-诉讼费转费用",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal transgerCostAmount;

    //（应收诉讼费科目-期初余额+（应收诉讼费科目-诉讼费支付）-（应收诉讼费科目-诉讼费收回）-应收诉讼费科目-诉讼费转费用
    @ApiModelProperty(value = "应收诉讼费科目-期末余额")
    @Excel(name = "应收诉讼费科目-期末余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal litigationExpensesEndBalance;

    //诉讼费发生额
    @ApiModelProperty(value = "管理费用-诉讼费科目-支付金额")
    @Excel(name = "管理费用-诉讼费科目-支付金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal litigationExpensesPayTotal;

    @ApiModelProperty(value = "管理费用-诉讼费科目-收回金额")
    @Excel(name = "管理费用-诉讼费科目-收回金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal litigationExpensesRecoverTotal;

    @ApiModelProperty(value = "代收款项科目-支付金额")
    @Excel(name = "代收款项科目-支付金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentPayTotal;

    @ApiModelProperty(value = "代收款项科目-收回金额")
    @Excel(name = "代收款项科目-收回金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentRecoverTotal;

    @ApiModelProperty("应付未付款")
    private BigDecimal payableUnpaid;

    @ApiModelProperty(value = "订单id用于区分是汇总数据还是明细数据，有-是汇总数据")
    private String orderId;

    @ApiModelProperty("凭证id")
    private Long voucherId;

    @ApiModelProperty("凭证摘要")
    private String voucherSummary;

}
