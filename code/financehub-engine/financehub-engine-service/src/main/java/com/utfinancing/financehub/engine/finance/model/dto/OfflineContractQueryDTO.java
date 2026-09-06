package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   OfflineContract查询from对象
 * @Modified :
 */
@ApiModel("OfflineContract查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OfflineContractQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;
    private List<String> orgIdList;

    @ApiModelProperty(value = "起租日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "收益计算")
    private String incomeCalculate;

    @ApiModelProperty(value = "收益计提方式")
    private String incomeProvisionMethod;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;
    private List<String> processStatusList;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    private List<Long> idList;
    private Long  id;
}
