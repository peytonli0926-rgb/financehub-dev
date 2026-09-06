package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description : 出售登记VO execl对象
 * @Modified :
 */
@Data
public class SellRegisterExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "资产编号")
    private String assetNumber;

    @Excel(name = "转出时间",dateFormat = "yyyy-MM-dd")
    private LocalDateTime transferOutDate;

    @Excel(name = "买售人")
    private String buyOrSellPerson;

    @Excel(name = "售价")
    private BigDecimal sellPrice;

    @Excel(name = "原合同号")
    private String contractCode;

    @Excel(name = "房产地址")
    private String propertyAddress;

    @Excel(name = "处理状态",readConverterExp="1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;


}
