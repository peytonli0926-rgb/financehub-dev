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
 * @Date : Create in 2024-03-11
 * @Description :   OutTableContractDetail查询from对象
 * @Modified :
 */
@ApiModel("OutTableContractDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OutTableContractDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "出表absId",required = true)
    private Long outTableAbsId;

    @ApiModelProperty(value = "借款合同编码")
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "封包日应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "封包日应收残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "封包日应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "封包日为实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "封包日承租人保证金")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractCodeList;

    @ApiModelProperty(value = "状态编号")
    private List<String> processStatusList;

    @ApiModelProperty(value = "出表期数")
    private String periods;


    @ApiModelProperty(value = "出表id数组")
    private List<Long> outTableAbsIdList;
}
