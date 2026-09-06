package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-14
 * @Description :   KingdeeVoucher查询from对象
 * @Modified :
 */
@ApiModel("KingdeeVoucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeVoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "金蝶T_GL_VOUCHER表主键ID")
    private String easId;

    @ApiModelProperty(value = "凭证编号")
    private String voucherCode;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "凭证类型编码")
    private String voucherTypeCode;

    @ApiModelProperty(value = "凭证类型名称")
    private String voucherTypeName;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "本位币借方金额")
    private String baseDebitAmount;

    @ApiModelProperty(value = "本位币贷方金额")
    private String baseCreditAmount;

    @ApiModelProperty(value = "报告币借方金额")
    private String reportDebitAmount;

    @ApiModelProperty(value = "报告币贷方金额")
    private String reportCreditAmount;

    @ApiModelProperty(value = "状态")
    private String voucherStatus;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "签约主体编码")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherAbstract;

    @ApiModelProperty(value = "凭证说明")
    private String voucherDescription;
}
