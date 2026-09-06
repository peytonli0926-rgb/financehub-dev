package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-19
 * @Description :   KingdeeContractBalance查询from对象
 * @Modified :
 */
@ApiModel("KingdeeContractBalance查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeContractBalanceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目余额")
    private String accountBalance;
}
