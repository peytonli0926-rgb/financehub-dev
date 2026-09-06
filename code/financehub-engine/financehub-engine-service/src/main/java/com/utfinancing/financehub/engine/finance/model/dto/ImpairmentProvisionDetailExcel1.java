package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细导入模板1 附件1：拨备减值明细_法人_to 财务；附件2：拨备减值明细_自然人_to 财务
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailExcel1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("导入的excel类型")
    private String excelType;

    @ApiModelProperty(value = "合同编号(核算项目)")
    @Excel(name = "合同号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "承租人")
    private String clientName;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "业务类型")
    //@Excel(name = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "签约主体")
    //@Excel(name = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "五级分类")
    @Excel(name = "五级分类")
    private String fiveClass;

    @ApiModelProperty(value = "三阶段")
    @Excel(name = "风险阶段划分")
    private String threeStep;

    @ApiModelProperty(value = "风险敞口")
    @Excel(name = "剩余风险敞口")
    private BigDecimal riskExposure;

    @ApiModelProperty(value = "拨备合计")
    @Excel(name = "Round(ECL4)")
    private BigDecimal provisionTotal;

    @ApiModelProperty(value = "上月余额")
    private BigDecimal lastMonthBalance;

    @ApiModelProperty(value = "本月计提")
    private BigDecimal thisMonthProvision;

    @ApiModelProperty(value = "是否核销")
    @Excel(name = "是否核销")
    private String isVerification;

    @ApiModelProperty(value = "应收租金余额")
    @Excel(name = "租金余额")
    private BigDecimal rentReceivableBalance;


}
