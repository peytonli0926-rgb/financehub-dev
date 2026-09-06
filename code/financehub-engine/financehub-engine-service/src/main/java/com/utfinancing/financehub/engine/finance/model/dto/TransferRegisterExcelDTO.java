package com.utfinancing.financehub.engine.finance.model.dto;

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
 * @Description : 转入登记DTO 导入对象
 * @Modified :
 */
@Data
public class TransferRegisterExcelDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资产编号")
    @Excel(name = "资产编号")
    private String assetNumber;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "入账时间")
    @Excel(name = "入账时间",dateFormat = "yyyy-MM-dd")
    private Date accountDate;

    @ApiModelProperty(value = "原合同号")
    @Excel(name = "原合同号")
    private String contractCode;

    @ApiModelProperty(value = "房产地址")
    @Excel(name = "房产地址")
    private String propertyAddress;

    @ApiModelProperty(value = "抵债资产入账价值")
    @Excel(name = "抵债资产入账价值")
    private BigDecimal debtAssetValue;

}
