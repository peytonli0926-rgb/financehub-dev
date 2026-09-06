package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-09
 * @Description : 合同接口表累计金额DTO对象
 * @Modified :
 */
@Data
public class ContractInterfaceTotalSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "回收本金累计金额")
    private BigDecimal recyclePrincipalAmount;

    @ApiModelProperty(value = "回收利息累计金额")
    private BigDecimal recycleInterestAmount;

    @ApiModelProperty(value = "回收罚息累计金额")
    private BigDecimal recycleDefaultInterestAmount;

    @ApiModelProperty(value = "收取首付款累计金额")
    private BigDecimal receiveFirstAmount;

    @ApiModelProperty(value = "收取手续费累计金额")
    private BigDecimal receiveProcedureAmount;

    @ApiModelProperty(value = "收取保险费累计金额")
    private BigDecimal receiveInsuranceAmount;

    @ApiModelProperty(value = "收取服务费累计金额")
    private BigDecimal receiveServiceAmount;

    @ApiModelProperty(value = "收取履约保证金累计金额")
    private BigDecimal receiveMarginAmount;

    @ApiModelProperty(value = "收取留购价累计金额")
    private BigDecimal receiveRetainedPrice;

    @ApiModelProperty(value = "收取其他收入累计金额")
    private BigDecimal receiveOtherRevenues;

    @ApiModelProperty(value = "收取厂商返利累计金额")
    private BigDecimal receiveFirmRebate;

    @ApiModelProperty(value = "收取合同解约及更改手续费累计金额")
    private BigDecimal receiveTerminateProcedureAmount;

    @ApiModelProperty(value = "收取违约金累计金额")
    private BigDecimal receivePenal;

    @ApiModelProperty(value = "收到GPS累计金额")
    private BigDecimal receiveGPS;

    @ApiModelProperty(value = "收到保险费差额累计金额")
    private BigDecimal receiveInsuranceDifferAmount;

    @ApiModelProperty(value = "收到收车款累计金额")
    private BigDecimal receiveRecycleCarAmount;

    @ApiModelProperty(value = "TA收款")
    private BigDecimal receiveTA;

    @ApiModelProperty(value = "是否可用1:可用 0:不可用")
    private String enableFlag;

}
