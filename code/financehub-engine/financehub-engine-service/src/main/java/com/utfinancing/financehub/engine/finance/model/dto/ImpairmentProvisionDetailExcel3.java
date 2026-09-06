package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细导入模板3 附件3：其他应收款明细（诉讼保全费）；附件4：其他应收款明细（其他保证金）
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailExcel3 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("导入的excel类型")
    private String excelType;

    @ApiModelProperty(value = "合同编号(核算项目)")
    @Excel(name = "辅助账编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    @Excel(name = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客  户")
    private String clientName;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "业务类型")
    @Excel(name = "科目")
    private String businessType;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "所属账套")
    private String orgId;

    @ApiModelProperty(value = "五级分类")
    private String fiveClass;

    @ApiModelProperty(value = "三阶段")
    private String threeStep;

    @ApiModelProperty(value = "风险敞口")
    private BigDecimal riskExposure;

    @ApiModelProperty(value = "拨备合计")
    @Excel(name = "ECL")
    private BigDecimal provisionTotal;

    @ApiModelProperty(value = "上月余额")
    private BigDecimal lastMonthBalance;

    @ApiModelProperty(value = "本月计提")
    private BigDecimal thisMonthProvision;

    @ApiModelProperty(value = "是否核销")
    @Excel(name = "是否核销")
    private String isVerification;

    @ApiModelProperty(value = "应收租金余额")
    private BigDecimal rentReceivableBalance;


    @Excel(name = "币种")
    private String originCurrency;

    @Excel(name = "目标币种")
    private String targetCurrency;

}
