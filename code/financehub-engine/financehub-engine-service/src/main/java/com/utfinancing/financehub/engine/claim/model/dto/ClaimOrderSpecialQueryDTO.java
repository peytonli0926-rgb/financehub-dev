package com.utfinancing.financehub.engine.claim.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-08
 * @Description :   ClaimOrderSpecial查询from对象
 * @Modified :
 */
@ApiModel("ClaimOrderSpecial查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClaimOrderSpecialQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "报销单主表ID")
    private Long claimOrderId;

    @ApiModelProperty(value = "单据编号")
    private String orderNo;

    @ApiModelProperty(value = "序号")
    private String no;

    @ApiModelProperty(value = "组织")
    private String org;

    @ApiModelProperty(value = "系统")
    private String sourceSystem;

    @ApiModelProperty(value = "合同号")
    private String contractNum;

    @ApiModelProperty(value = "承租人")
    private String tenant;

    @ApiModelProperty(value = "账期(yyyy-MM)")
    private String accountDate;

    @ApiModelProperty(value = "单价")
    private String unitPrice;

    @ApiModelProperty(value = "设备款不含税额")
    private String equipNotaxAmount;

    @ApiModelProperty(value = "设备款进项税")
    private String equipInputTax;

    @ApiModelProperty(value = "安装费")
    private String installAmount;

    @ApiModelProperty(value = "安装费不含税额")
    private String installNotaxAmount;

    @ApiModelProperty(value = "安装费进项税")
    private String installInputTax;

    @ApiModelProperty(value = "服务费")
    private String serviceAmount;

    @ApiModelProperty(value = "服务费不含税额")
    private String serviceNotaxAmount;

    @ApiModelProperty(value = "服务费进项税")
    private String serviceInputTax;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "是否转天津")
    private String tianjiFlag;

    @ApiModelProperty(value = "付款金额")
    private String paymentAmount;

    @ApiModelProperty(value = "不含税金额")
    private String paymentNotaxAmount;

    @ApiModelProperty(value = "进项税")
    private String paymentInputTax;

    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    @ApiModelProperty(value = "凭证标识")
    private String voucherFlag;

    @ApiModelProperty(value = "item01")
    private String item01;

    @ApiModelProperty(value = "item01")
    private String item02;

    @ApiModelProperty(value = "item01")
    private String item03;

    @ApiModelProperty(value = "item01")
    private String item04;

    @ApiModelProperty(value = "item01")
    private String item05;

    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "是否已生成凭证(0:否，1：是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "签约主体Id")
    private String orgId;

    @ApiModelProperty(value = "费用类型集合")
    private List<String> expenseTypeList;
}
