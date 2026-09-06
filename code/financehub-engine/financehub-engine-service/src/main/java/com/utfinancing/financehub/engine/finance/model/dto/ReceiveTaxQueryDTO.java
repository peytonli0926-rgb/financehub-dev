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
 * @Description :   ReceiveTax查询from对象
 * @Modified :
 */
@ApiModel("ReceiveTax查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiveTaxQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "签约主体")
    private String sellerName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "是否开票(0-否，1-是)")
    private String isInvoiced;

    @ApiModelProperty(value = "开票/计提名称")
    private String productName;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    @ApiModelProperty(value = "税额")
    private String taxValue;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "开票明细税额")
    private String invoiceDetailTax;

    @ApiModelProperty(value = "差异情况")
    private String differenceSituation;
}
