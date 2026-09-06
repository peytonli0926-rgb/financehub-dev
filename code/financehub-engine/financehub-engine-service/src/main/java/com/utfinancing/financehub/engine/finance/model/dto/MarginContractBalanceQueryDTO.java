package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description :   MarginContractBalance查询from对象
 * @Modified :
 */
@ApiModel("MarginContractBalance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class MarginContractBalanceQueryDTO extends BaseQueryDTO {


    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    @ApiModelProperty(value = "查询日期")
    private Date balanceDate;
    private Date startBalanceDate;
    private List<String> idList;

    @ApiModelProperty(value = "公司")
    private String orgId;

    @ApiModelProperty(value = "公司")
    private List<String> orgIdList;

    @ApiModelProperty(value = "排除的公司")
    private List<String> orgIdListNotIn;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目编码List")
    private List<String> accountCodeList;

    @ApiModelProperty(value = "保证金类别")
    private String accountName;

    @ApiModelProperty(value = "保证金类别List")
    private List<String> accountNameList;

    @ApiModelProperty(value = "排除的合同号")
    private List<String> contractCodeListNotIn;

    @ApiModelProperty(value = "业务codeList")
    private List<String> businessCodeList;

    @ApiModelProperty(value = "保证金凭证类型")
    private String marginType;

    @ApiModelProperty(value = "批次id")
    private Long batchId;
    private List<Long> batchIdList;

    private int periodCode;

    @ApiModelProperty(value = "业务日期年月")
    private String businessDateMonth;

    @ApiModelProperty(value = "上个月的会计期间")
    private int lastPeriodCode;

}
