package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.engine.hthx.common.annotation.CheckField;
import com.utfinancing.financehub.engine.hthx.common.base.HthxBaseWarnDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class ClaimConfirmDTO extends HthxBaseWarnDTO implements Serializable {

    /**
     * @description: 针对标记为@CheckField注解的字段会在软提示时进行数据一致性校验
     * @author: zhangli.chen
     **/
    @CheckField
    @ApiModelProperty(value = "未确认收款ID", required = true)
    private Long id;

    @ApiModelProperty(value = "记账日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐(1:是, 0:否)", required = true)
    private String isRelateClientAuxiliaryAccount;

//    @ApiModelProperty(value = "是否修改入账日期")
//    private String isModifyIncomeDate;
//
//    @ApiModelProperty(value = "原入账年月（字段：是否修改入账日期 为是时，该字段必填）")
//    private String incomeYmOld;

    @ApiModelProperty(value = "认领数据明细")
    private List<ClaimQueryResultDetailDTO> claimQueryResultDetailList = new ArrayList<>();

    @ApiModelProperty(value = "认领凭证明细")
    private List<ClaimConfirmVoucherDTO> claimConfirmVoucherList = new ArrayList<>();

    /**
     * 生成数据一致性校验的hash值
     */
    @Override
    public int generateDataHash() {
        try {
            return Objects.hash(getCheckFields());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("软提示生成数据一致性校验的Hash值失败：", e);
        }
    }
}
