package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <ul>
 * <li>Project :  financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ChargeOffDetailQueryDTO</li>
 * <li>CreateTime : 2024/02/28 17:10</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("详情分页查询参数")
@Data
public class ChargeOffDetailQueryDTO extends BaseQueryDTO {

    @ApiModelProperty(value = "合同编号")
    @NotNull(message = "合同编号不可以为空")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    @NotNull(message = "签约主体不可以为空")
    private String orgId;

    @ApiModelProperty(value = "核销状态")
    @NotNull(message = "核销状态不可以为空")
    private String verificationStatus;

    @ApiModelProperty(value = "核销时间")
    @NotNull(message = "核销时间不可以为空")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty("上个月会计期间")
    private Integer lastPeriodCode;

}
