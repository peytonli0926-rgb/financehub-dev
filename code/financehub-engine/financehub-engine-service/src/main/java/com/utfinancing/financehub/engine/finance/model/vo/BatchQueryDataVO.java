package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : VO对象
 * @Modified :
 */
@Data
public class BatchQueryDataVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "TA余额")
    private BigDecimal taAmount;

    @ApiModelProperty(value = "已收款未开票利息")
    private BigDecimal nonInvoicedProfit;

    @ApiModelProperty(value = "系统未收本金")
    private BigDecimal nonReceivedPrincipal;

    @ApiModelProperty(value = "系统未收利息")
    private BigDecimal nonReceivedProfit;

    @ApiModelProperty(value = "科目余额信息集合")
    List<BatchQueryDataAssistVO> assistList;
}
