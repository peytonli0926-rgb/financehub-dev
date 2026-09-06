package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细导入模板 附件9：恒信应收蓬莱租赁款清单
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailExcel9 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("导入的excel类型")
    private String excelType;

    @ApiModelProperty(value = "合同编号(核算项目)")
    @Excel(name = "物料编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    @Excel(name = "物料名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "公司名称")
    private String clientName;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "五级分类")
    private String fiveClass;

    @ApiModelProperty(value = "三阶段")
    private String threeStep;

    @ApiModelProperty(value = "风险敞口")
    private BigDecimal riskExposure;

    @ApiModelProperty(value = "拨备合计")
    @Excel(name = "减值")
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


}
