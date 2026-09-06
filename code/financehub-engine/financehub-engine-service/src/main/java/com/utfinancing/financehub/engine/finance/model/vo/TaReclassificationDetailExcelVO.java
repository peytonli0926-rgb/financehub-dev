package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : ta重分类明细表VO对象
 * @Modified :
 */
@Data
public class TaReclassificationDetailExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "重分类月份")
    @Excel(name = "重分类月份", dateFormat = "yyyy-MM", cellType = Excel.ColumnType.DATE)
    private LocalDateTime reclassificationMonth;

    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统编码")
    @Excel(name = "业务系统")
    private String systemCodeName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    @Excel(name = "签约主体")
    private String orgName;

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "租赁大类,字典：contract_business_type")
    private String businessCode;

    @ApiModelProperty(value = "租赁大类,字典：contract_business_type")
    @Excel(name = "租赁大类")
    private String businessCodeName;

    @ApiModelProperty(value = "合同状态")
    @Excel(name = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "到账主体")
    private String bankOrgId;

    @ApiModelProperty(value = "到账主体名称")
    @Excel(name = "网银到账主体")
    private String bankOrgName;

    @ApiModelProperty(value = "业务系统网银编号")
    @Excel(name = "业务系统网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "业务系统网银编号-小网银")
    @Excel(name = "业务系统网银编号-小网银")
    private String ebankBatchNo;

    @ApiModelProperty(value = "网银付款人名称")
    @Excel(name = "网银付款人名称")
    private String ebankClientName;

    @ApiModelProperty(value = "运营部备注")
    @Excel(name = "运营部备注")
    private String operationRemark;

    @ApiModelProperty(value = "TA/溢存款余额")
    @Excel(name = "TA/溢存款余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taExcessBalance;

    @ApiModelProperty(value = "租金校验结果")
    @Excel(name = "租金校验结果")
    private String rentCheck;

    @ApiModelProperty(value = "网银校验结果")
    @Excel(name = "网银校验结果")
    private String ebankCheck;

    @ApiModelProperty(value = "TA重分类金额")
    @Excel(name = "TA重分类金额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taReclassificationAmount;

    @ApiModelProperty(value = "合同结束日期")
    @Excel(name = "合同结束日期", dateFormat = "yyyy-MM-dd", cellType = Excel.ColumnType.DATE)
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "应收租金余额")
    @Excel(name = "应收租金余额",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty(value = "特殊合同状态")
    @Excel(name = "特殊合同状态")
    private String specialContractStatus;

    @ApiModelProperty(value = "TA重分类科目编码")
    @Excel(name = "TA重分类科目编码")
    private String taAccountCode;

    @ApiModelProperty(value = "TA重分类科目名称")
    private String taAccountName;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "异常类型")
    @Excel(name = "异常类型")
    private String exceptionTypeName;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "记账日期")
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

    @ApiModelProperty(value = "ta重分类id")
    private Long taReclassificationId;

    @ApiModelProperty(value = "重分类科目编码")
    private String accountCode;
}
