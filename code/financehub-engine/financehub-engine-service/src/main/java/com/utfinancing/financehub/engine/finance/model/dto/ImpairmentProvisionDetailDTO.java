package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细DTO对象
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号(核算项目)")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
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
    private BigDecimal provisionTotal;

    @ApiModelProperty(value = "上月余额")
    private BigDecimal lastMonthBalance;

    @ApiModelProperty(value = "本月计提")
    private BigDecimal thisMonthProvision;

    @ApiModelProperty(value = "凭证id(多个逗号分隔)")
    private String voucherId;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否核销")
    private String isVerification;

    @ApiModelProperty(value = "应收租金余额")
    private BigDecimal rentReceivableBalance;

    @ApiModelProperty(value = "中台应收租金余额")
    private BigDecimal hubRentReceivableBalance;

    @ApiModelProperty("导入的excel类型")
    private String excelType;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(name = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(name = "减值计提id")
    private Long impairmentProvisionId;

    @ApiModelProperty(value = "目标币种本月计提金额")
    private BigDecimal targetAmount;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(name = "原币币种")
    private String originCurrency;

    @ApiModelProperty(name = "目标币种")
    private String targetCurrency;

}
