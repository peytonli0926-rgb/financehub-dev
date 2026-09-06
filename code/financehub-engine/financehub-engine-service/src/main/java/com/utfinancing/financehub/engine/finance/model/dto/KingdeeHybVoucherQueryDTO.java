package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-02
 * @Description :   KingdeeHybVoucher查询from对象
 * @Modified :
 */
@ApiModel("KingdeeHybVoucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeHybVoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "金蝶凭证id")
    private String fid;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "fassgrpId")
    private String fassgrpId;

    @ApiModelProperty(value = "币种")
    private String currencyName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "借方金额")
    private String debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private String creditAmount;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "(未执行：NOT_EXECUTE，进行中：RUNNING，成功：SUCCESS，失败：FAILED)")
    private String messageStatus;

    @ApiModelProperty(value = "报错信息")
    private String messageError;

    @ApiModelProperty(value = "会计期间")
    private String periodCode;

    @ApiModelProperty(value = "摘要")
    private String abstractContent;
}
