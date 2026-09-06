package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class QueryNonConfirmAccountCheckingInputDTO extends BaseQueryDTO implements Serializable {
    @ApiModelProperty(value = "对账月份(yyyy-MM)")
    private String checkingMonth;

    @ApiModelProperty(value = "到账主体编码")
    private List<String> collectionAccountsBankCode = new ArrayList<>();

    @ApiModelProperty(value = "账龄分类")
    private List<String> accountAgeClass = new ArrayList<>();

    @ApiModelProperty(value = "业务系统")
    private List<String> systemCode = new ArrayList<>();

    @ApiModelProperty(value = "财务部初分类")
    private List<String> financialPrimaryClassic = new ArrayList<>();

    @ApiModelProperty(value = "运营部确认款项性质")
    private List<String> confirmAccountProperty = new ArrayList<>();
}
