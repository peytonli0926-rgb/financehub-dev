package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-11
 * @Description :   VoucherEntry查询from对象
 * @Modified :
 */
@ApiModel("VoucherEntry查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherEntryQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;

}
