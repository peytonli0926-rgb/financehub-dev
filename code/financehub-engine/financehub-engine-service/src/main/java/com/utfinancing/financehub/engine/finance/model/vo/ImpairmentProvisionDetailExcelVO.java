package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : 减值计提明细VO对象
 * @Modified :
 */
@Data
public class ImpairmentProvisionDetailExcelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "核算项目")
    private String contractCode;

    @Excel(name = "合同名称")
    private String contractName;

    @Excel(name = "客户编号")
    private String clientCode;

    @Excel(name = "客户名称")
    private String clientName;

    @Excel(name = "减值类型")
    private String impairmentType;

    @Excel(name = "业务类型")
    private String businessType;

    @Excel(name = "签约主体")
    private String orgName;

    @Excel(name = "五级分类")
    private String fiveClass;

    @Excel(name = "三阶段")
    private String threeStep;

    @Excel(name = "风险敞口", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal riskExposure;

    @Excel(name = "拨备合计", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal provisionTotal;

    @Excel(name = "上月余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal lastMonthBalance;

    @Excel(name = "本月计提", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal thisMonthProvision;

    @Excel(name = "财务合同状态")
    private String financialContractStatus;

    @Excel(name = "合同状态")
    private String contractStatus;

    @Excel(name = "应收租金余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal rentReceivableBalance;

    @Excel(name = "中台应收租金余额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal hubRentReceivableBalance;



}
