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
 * @Date : Create in 2024-03-21
 * @Description :   BusinessClaimRepaymentRecord查询from对象
 * @Modified :
 */
@ApiModel("BusinessClaimRepaymentRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessClaimRepaymentRecordQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "")
    private String systemCode;

    @ApiModelProperty(value = "业务系统网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "认领金额")
    private String claimAmount;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "合同号")
    private String contractCode;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "业务系统交易流水号")
    private String orderId;

    @ApiModelProperty(value = "业务日期(yyyy-MM-dd HH:mm:ss)")
    private String businessDate;
}
