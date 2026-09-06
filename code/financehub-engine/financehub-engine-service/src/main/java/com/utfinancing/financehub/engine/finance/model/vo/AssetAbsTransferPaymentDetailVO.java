package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : 资产转付详情表VO对象
 * @Modified :
 */
@Data
public class AssetAbsTransferPaymentDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "转付主表Id")
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

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    //借款合同编号
    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    //出表期数
    @ApiModelProperty(value = "出表期数")
    private String periods;

    @ApiModelProperty("签约主体")
    private String orgId;

}
