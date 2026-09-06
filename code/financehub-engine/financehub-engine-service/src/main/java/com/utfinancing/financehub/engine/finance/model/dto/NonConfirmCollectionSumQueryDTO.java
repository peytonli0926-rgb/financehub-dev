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
 * @Date : Create in 2024-03-22
 * @Description :   NonConfirmCollectionSum查询from对象
 * @Modified :
 */
@ApiModel("NonConfirmCollectionSum查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class NonConfirmCollectionSumQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "网银编号-资金系统")
    private String ebankNumber;

    @ApiModelProperty(value = "认领主体")
    private String orgId;

    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    @ApiModelProperty(value = "业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "认领主体中文名称")
    private String orgName;

    @ApiModelProperty(value = "到账主体中文名称")
    private String collectionAccountsBankName;

    @ApiModelProperty(value = "业务系统的网银编号")
    private String businessEbankNumber;

    @ApiModelProperty(value = "资金系统、业务系统网银编号映射表ID")
    private Long ebankMappingId;

    @ApiModelProperty(value = "网银余额非零, 1:非零 0:零")
    private String notZero;
}
