package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@Data
public class HthxOfflineOnlineBankTemplateExcel implements Serializable {

    @Excel(name = "业务系统网银编号-小网银", comment = "和[业务系统网银编号]列至少输入一个")
    private String ebankSerialNumber;

    @Excel(name = "业务系统网银编号", comment = "和[业务系统网银编号-小网银]列至少输入一个")
    private String businessEbankNumber;

    @Excel(name = "借方发生额", comment = "必填")
    private BigDecimal claimAmount;

    @Excel(name = "认领日期(yyyy-MM-dd)", comment = "必填")
    private String claimDate;

    @Excel(name = "网银归属主体", comment = "必填")
    private String orgName;

    /**
     * @description: 主体ID
     **/
    private String orgId;

}
