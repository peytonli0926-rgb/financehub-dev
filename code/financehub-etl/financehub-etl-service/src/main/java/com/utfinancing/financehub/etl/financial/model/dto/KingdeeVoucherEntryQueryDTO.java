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
 * @Date : Create in 2023-11-15
 * @Description :   KingdeeVoucherEntry查询from对象
 * @Modified :
 */
@ApiModel("KingdeeVoucherEntry查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeVoucherEntryQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "金蝶T_GL_VOUCHERENTRY表主键ID")
    private String easId;

    @ApiModelProperty(value = "金蝶凭证头ID")
    private String voucherEasId;

    @ApiModelProperty(value = "摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "借贷方向")
    private String debitCreditType;

    @ApiModelProperty(value = "借方金额")
    private String debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private String creditAmount;
}
