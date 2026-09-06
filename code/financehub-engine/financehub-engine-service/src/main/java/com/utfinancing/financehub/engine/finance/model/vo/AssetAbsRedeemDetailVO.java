package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-15
 * @Description : 资产转让ABS-赎回详情表VO对象
 * @Modified :
 */
@Data
public class AssetAbsRedeemDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "赎回主表Id")
    private Long assetAbsRedeemId;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "赎回价格")
    private BigDecimal redeemPrice;

    @ApiModelProperty(value = "税率")
    private String rate;

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

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgIdName;

    @ApiModelProperty(value = "出表期数")
    private String periods;

    @ApiModelProperty(value = "赎回开始日期")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "实际赎回日")
    private LocalDateTime actualDate;

    @ApiModelProperty(value = "赎回起算日租金余额")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "赎回起算日本金余额")
    private BigDecimal principalBalance;

    @ApiModelProperty(value = "赎回起算日利息余额")
    private BigDecimal interestBalance;

    @ApiModelProperty(value = "赎回起算日留购价余额")
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "赎回起算日保证金余额")
    private BigDecimal lesseeMarginBalance;

    //赎回起算日应收销项税余额，区分客户、合同（余额表凭证日期<=赎回起算日，创建日期为最新时，receivable_outtax_balance的金额）
    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttax;

    //赎回起算日未实现收益余额，区分合同（余额表凭证日期<=赎回起算日，创建日期为最新时，unrealized_revenue_balance的金额）
    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenue;

}
