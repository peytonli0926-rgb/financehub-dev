package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description : 合同VO对象
 * @Modified :
 */
@Data
public class ContractVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "利率")
    private BigDecimal leaseInterestRateYear;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "起租日")
    private LocalDateTime leaseDateStart;

    @ApiModelProperty(value = "到期日")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "租赁类型")
    private String leaseType;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "利率浮动类型")
    private String interestRateType;

    @ApiModelProperty(value = "五级分类")
    private String classificationFive;

    @ApiModelProperty(value = "拨备类型")
    private String provisionType;

    @ApiModelProperty(value = "还租方式")
    private String returnType;

    @ApiModelProperty(value = "国产进口")
    private String domesticEntrance;

    @ApiModelProperty(value = "项目区分")
    private String projectDifferentiate;

    @ApiModelProperty(value = "业务板块")
    private String businessPlate;

    @ApiModelProperty(value = "省市")
    private String provinceCity;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "合同金额")
    private BigDecimal contractAmount;

    @ApiModelProperty(value = "特殊标识")
    private String specialFlag;

    @ApiModelProperty(value = "财务合同状态更新时间")
    private Date financialContractStatusUpdateTime;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "业务类型标签")
    private String businessTypeLabel;

    @ApiModelProperty(value = "转入公司")
    private String transferOrgId;

    @ApiModelProperty(value = "转入合同号")
    private String transferContractCode;

    @ApiModelProperty(value = "转入合同系统合同状态")
    private String transferContractStatus;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "收益计算")
    private String incomeCalculate;
    @ApiModelProperty(value = "收益计提方式")
    private String incomeProvisionMethod;
    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    @ApiModelProperty("收车费汇总金额")
    private BigDecimal payableRecycleCarAmount;

    //省
    @ApiModelProperty(value = "省")
    private String province;

    //市
    @ApiModelProperty(value = "市")
    private String city;

    //业务日期
    private LocalDate businessDate;

    @ApiModelProperty(value = "系统名称")
    private String systemCodeName;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "签约主体名称")
    private String orgIdName;

    @ApiModelProperty(value = "是否抵债资产，0否，1是")
    private String isDzzc;

    @ApiModelProperty(value = "模块id")
    private Long sourceFromId;

    @ApiModelProperty(value = "模块类型")
    private String sourceFromType;

    @ApiModelProperty(value = "实收服务费")
    private BigDecimal actualServiceAmount;

}
