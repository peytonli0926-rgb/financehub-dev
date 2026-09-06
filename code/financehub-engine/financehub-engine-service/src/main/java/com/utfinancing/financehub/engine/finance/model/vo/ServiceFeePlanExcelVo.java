package com.utfinancing.financehub.engine.finance.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 服务费计划表实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceFeePlanExcelVo extends Model<ServiceFeePlanExcelVo> {

    private static final long serialVersionUID = 1L;
    //合同编号
    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "服务费协议编号")
    private String serviceFeeNo;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "合同主体")
    private String orgName;

    @Excel(name = "服务费主体")
    private String serviceOrgName;

    @Excel(name = "分摊方式")
    private String allocationMethod;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @Excel(name = "业务类型")
    private String businessName;

    @Excel(name = "会计起租日",width = 20,cellType = Excel.ColumnType.DATE)
    private Date leaseDateStart;

    @Excel(name = "合同约定到期日",width = 20,cellType = Excel.ColumnType.DATE)
    private Date leaseDateEnd;

    @Excel(name = "服务费实收（税前）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualReceiveServiceFee;

    @Excel(name = "服务费实收（税后）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualReceiveNoTax;

    @Excel(name = "应分摊的服务费收入（税前）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal agreedApportionAmount;

    @Excel(name = "应分摊的服务费收入（税后）",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal agreedApportionAmountNoTax;

    @Excel(name = "分摊期间",width = 20,cellType = Excel.ColumnType.DATE)
    private Date planDate;

    @Excel(name = "本期计划金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal planAmount;

    @Excel(name = "本期实际金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal actualAccruedAmount;

    @Excel(name = "本期调整金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal adjustAmount;

    @Excel(name = "计提凭证状态")
    private String voucherStatus;

    @Excel(name = "分摊完结标记")
    private String endSharingServiceFeeFlagStr;

    @Excel(name = "是否分摊标记")
    private String sharingServiceFeeFlagStr;

    @Excel(name = "是否特殊状态调整")
    private String specialStatusAdjustmentFlagStr;

}
