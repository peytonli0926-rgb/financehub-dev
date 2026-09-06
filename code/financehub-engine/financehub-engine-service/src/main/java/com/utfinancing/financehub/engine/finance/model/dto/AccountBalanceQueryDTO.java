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
 * @Author : lixin
 * @Date : Create in 2023-09-11
 * @Description :   AccountBalance查询from对象
 * @Modified :
 */
@ApiModel("AccountBalance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountBalanceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    private String sceneName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "机构名称(签约主体)")
    private String signCompany;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "财务日期")
    private Date financeDate;

    @ApiModelProperty(value = "财务账期(yyyyMM)")
    private Integer accountPeriod;

    @ApiModelProperty(value = "借方金额")
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private BigDecimal creditAmount;

    @ApiModelProperty(value = "公司")
    private List<String> orgIdList;

    @ApiModelProperty(value = "排除的公司")
    private List<String> orgIdListNotIn;
}
