package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description :   AccountAssistBalance查询from对象
 * @Modified :
 */
@Data
public class AccountAssistBalanceGetOneQueryDTO{

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "业务类型")
    private String businessCode;

    @ApiModelProperty(value = "借款合同编号")
    private String billContractCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;


}
