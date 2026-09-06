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
 * @Date : Create in 2023-12-13
 * @Description :   CostChannelFeeTaxApportion查询from对象
 * @Modified :
 */
@ApiModel("CostChannelFeeTaxApportion查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CostChannelFeeTaxApportionQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "渠道费类型")
    private String channelType;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "金额（不含税）")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "凭证id,多个以逗号分隔")
    private String voucherIds;
}
