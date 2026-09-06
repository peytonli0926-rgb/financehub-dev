package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@ApiModel("已勾稽分页列表查询表单")
public class SystemBankMappingReconciliationQueryDTO extends BaseQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 改成之选一个，默认当前会计期间
     * 按当前时间计算 如 2023-12-22 -> 202312
     */
    @ApiModelProperty(value = "会计期间 默认当前会计期间 按当前时间计算 如 2023-12-22 -> 202312",example = "202312", required = true)
    private Integer periodCode;


    /**
     * 差额是否为0
     * 非0为异常
     */
    @ApiModelProperty(value = "是否异常，N：否 Y：是", example = "")
    private String  abnormal;

    public Integer getPeriodCode() {
        if (periodCode == null) {
            LocalDate now = LocalDate.now();
            periodCode = now.getYear() * 100 + now.getMonthValue();
        }
        return periodCode;
    }

    public Integer abnormal() {
        if (StringUtils.isBlank(abnormal)) {
            return null;
        }
        return abnormal.trim().equals("Y") ? 1 : 0;
    }
}
