package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :   AssetAbsTransferPaymentDetail查询from对象
 * @Modified :
 */
@ApiModel("AssetAbsTransferPaymentDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetAbsTransferPaymentDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "转付主表Id",required = true)
    private Long assetAbsTransferPaymentId;

    @ApiModelProperty(value = "税率")
    private String rate;

    @ApiModelProperty(value = "实付本金")
    private BigDecimal actualPrincipalAmount;

    @ApiModelProperty(value = "实付利息")
    private BigDecimal actualInterestAmount;

    @ApiModelProperty(value = "实付留够价")
    private BigDecimal actualRetentionPurchaseAmount;

    @ApiModelProperty(value = "实付罚息及手续费")
    private BigDecimal actualPenaltyInterestAmount;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherIds;

    @ApiModelProperty(value = "报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "转付主表Id集合")
    private List<Long> assetAbsTransferPaymentIdList;
}
