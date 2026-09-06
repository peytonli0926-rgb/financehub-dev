package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : 线下合同DTO对象
 * @Modified :
 */
@Data
public class ContractHisExcel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "主合同编号")
    private String contractCodeM;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "合同名称", type = Excel.Type.EXPORT)
    private String contractName;

    @Excel(name = "客户编号", type = Excel.Type.EXPORT)
    private String clientCode;

    @Excel(name = "客户名称", type = Excel.Type.EXPORT)
    private String clientName;

    @Excel(name = "客户类型", type = Excel.Type.EXPORT)
    private String clientType;

    @Excel(name = "签约主体")
    private String orgId;

    @Excel(name = "新签约主体")
    private String newOrgId;

    @Excel(name = "起租日", cellType = Excel.ColumnType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateStart;

    @Excel(name = "到期日", cellType = Excel.ColumnType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateEnd;

    @Excel(name = "租赁类型",comment = "回租/直租")
    private String leaseType;

    @Excel(name = "税率", cellType = Excel.ColumnType.NUMERIC, type = Excel.Type.EXPORT, comment = "填入数值,如0.06")
    private BigDecimal taxRate;

    @Excel(name = "币种", type = Excel.Type.EXPORT, comment = "人民币/美元/港元/欧元/日元")
    private String currencyType;

    @Excel(name = "发票类型", type = Excel.Type.EXPORT,comment = "本金一次性开票(利息按年利率拆分)/随租金开票-按年利率拆分(本金收据,利息增普票)/每期租金方式开票/本金一次性开票")
    private String invoiceType;

    @Excel(name = "开票标识",comment = "开票/计提")
    private String invoicingFlag;

    @Excel(name = "收益计算", readConverterExp = "2=否,1=是", comment = "是/否")
    private String incomeCalculate;

    @Excel(name = "计提方式", comment = "XIRR分摊收益/实收/IRR分摊收益")
    private String incomeProvisionMethod;

    @Excel(name = "业务合同状态", type = Excel.Type.EXPORT)
    private String contractStatus;

    @Excel(name = "财务合同状态", type = Excel.Type.EXPORT)
    private String financialContractStatus;

    @Excel(name = "合同出单部门", type = Excel.Type.EXPORT)
    private String contractCreateDept;

    @Excel(name = "所属系统", type = Excel.Type.EXPORT)
    private String systemCode;

    @Excel(name = "设备金额", type = Excel.Type.EXPORT)
    private BigDecimal payableDeviceAmount;


}
