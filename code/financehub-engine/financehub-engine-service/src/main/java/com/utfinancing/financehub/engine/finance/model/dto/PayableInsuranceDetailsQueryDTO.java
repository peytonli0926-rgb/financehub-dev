package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-24
 * @Description :   PayableInsuranceDetails查询from对象
 * @Modified :
 */
@ApiModel("PayableInsuranceDetails查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayableInsuranceDetailsQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "应付保险费表id")
    private Long payableInsuranceId;

    @ApiModelProperty(value = "应付保险费表id")
    private List<Long> payableInsuranceIdList;

    @ApiModelProperty(value = "业务合同状态")
    private String contractStatus;
    private List<String> contractStatusList;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;
    private List<String> financialContractStatusList;

    private String contractCode;

}
