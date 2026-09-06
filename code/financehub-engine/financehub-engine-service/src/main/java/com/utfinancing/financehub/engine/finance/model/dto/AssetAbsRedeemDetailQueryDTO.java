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
 * @Date : Create in 2024-03-15
 * @Description :   AssetAbsRedeemDetail查询from对象
 * @Modified :
 */
@ApiModel("AssetAbsRedeemDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetAbsRedeemDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "赎回主表Id",required = true)
    private Long assetAbsRedeemId;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "赎回价格")
    private BigDecimal redeemPrice;

    @ApiModelProperty(value = "税率")
    private String rate;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherIds;

    @ApiModelProperty(value = "报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "出表期数")
    private String periods;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "赎回主表Id集合",required = true)
    private List<Long> assetAbsRedeemIdList;
}
