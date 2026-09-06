package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description :   MarginContractBalance查询from对象
 * @Modified :
 */
@ApiModel("MarginContractBalanceQueryBizCodeDTO查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class MarginContractBalanceQueryBizCodeDTO extends BaseQueryDTO {


    @ApiModelProperty(value = "业务code")
    private String businessCode;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

}
