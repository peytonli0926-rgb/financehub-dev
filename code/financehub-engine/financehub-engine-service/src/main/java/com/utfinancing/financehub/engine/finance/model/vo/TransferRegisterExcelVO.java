package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description : 转入登记VO excel对象
 * @Modified :
 */
@Data
public class TransferRegisterExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "资产编号")
    private String assetNumber;

    // @Excel(name = "签约主体")
    private String orgId;

    @Excel(name = "签约主体")
    private String orgName;

    @Excel(name = "入账时间",dateFormat = "yyyy-MM-dd")
    private LocalDateTime accountDate;

    @Excel(name = "原合同号")
    private String contractCode;

    @Excel(name = "房产地址")
    private String propertyAddress;

    @Excel(name = "抵债资产入账价值", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal debtAssetValue;

    @Excel(name = "处理状态",readConverterExp="1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;


}
