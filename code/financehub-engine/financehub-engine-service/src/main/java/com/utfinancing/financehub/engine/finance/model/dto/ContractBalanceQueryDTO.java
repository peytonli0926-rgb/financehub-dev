package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-06
 * @Description :   ContractBalance查询from对象
 * @Modified :
 */
@ApiModel("ContractBalance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBalanceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务日期开始")
    private LocalDate businessDateStart;

    @ApiModelProperty(value = "业务日期结束")
    private LocalDate businessDateEnd;

    @ApiModelProperty(value = "凭证日期开始")
    private LocalDate voucherDateStart;

    @ApiModelProperty(value = "凭证日期结束")
    private LocalDate voucherDateEnd;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "凭证日期结束")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "返回的字段")
    private String querySql;

    @ApiModelProperty(value = "合同编码集合")
    private List<String> contractCodeList;

    @ApiModelProperty(value = "客户编码集合")
    private List<String> clientCodeList;

    @ApiModelProperty(value = "科目编码")
    private List<String> accountCodeList;

}
