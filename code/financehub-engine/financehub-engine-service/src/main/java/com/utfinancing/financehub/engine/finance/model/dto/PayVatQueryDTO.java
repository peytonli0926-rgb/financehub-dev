package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description :   PayVat查询from对象
 * @Modified :
 */
@ApiModel("PayVat查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayVatQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "ID")
    private List<Long> idList;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同类型")
    private String contractType;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "开票标识")
    private String invoicingFlag;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "开票主体")
    private String orgId;

    @ApiModelProperty(value = "租赁合同编号")
    private String contractCodeM;

    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "数据范围")
    private List<String> exclusionScopeTaxList;

}
