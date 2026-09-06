package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ChargeOffDownLoadQueryDTO</li>
 * <li>CreateTime : 2024/03/07 09:42</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "Charge Off导出DTO")
@Data
public class ChargeOffDownLoadQueryDTO {

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private List<String> orgIdList;

    @ApiModelProperty(value = "核销状态")
    private String verificationStatus;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "记账时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date accountDate;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;


}
