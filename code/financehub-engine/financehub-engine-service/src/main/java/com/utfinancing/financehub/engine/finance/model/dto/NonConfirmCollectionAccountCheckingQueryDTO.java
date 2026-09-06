package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : robjiang
 * @Date : Create in 2024-04-22
 * @Description :   NonConfirmCollectionAccountChecking查询from对象
 * @Modified :
 */
@ApiModel("NonConfirmCollectionAccountChecking查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class NonConfirmCollectionAccountCheckingQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "对账月份")
    private String accountCheckingMonth;

    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统的网银编号")
    private String businessEbankNumber;

    @ApiModelProperty(value = "业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "入账日期")
    private LocalDateTime businessHappenDate;

    @ApiModelProperty(value = "账龄分类")
    private String accountAgeClass;

    @ApiModelProperty(value = "月初余额")
    private String monthInitBalance;

    @ApiModelProperty(value = "本月贷方发生额")
    private String curMonthCreditAmount;

    @ApiModelProperty(value = "本月余额")
    private String curMonthBalance;

    @ApiModelProperty(value = "系统金额")
    private String systemAmount;

    @ApiModelProperty(value = "差额")
    private String diffAmount;

    @ApiModelProperty(value = "财务对账备注")
    private String accountCheckingComments;

    @ApiModelProperty(value = "财务初分类")
    private String financialPrimaryClassic;

    @ApiModelProperty(value = "运营部确认款项性质")
    private String confirmAccountProperty;

    @ApiModelProperty(value = "是否已经确认")
    private String isConfirmed;
}
