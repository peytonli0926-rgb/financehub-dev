package com.utfinancing.financehub.engine.verification.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.verification.model.dto.VerificationPaybackQueryDTO</li>
 * <li>CreateTime : 2023/10/19 09:59</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "核销回款查询DTO")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class VerificationPaybackQueryDTO extends BaseQueryDTO {

    @ApiModelProperty(value = "回款月份")
    private String businessDate;

    @ApiModelProperty(value = "回款开始月份")
    private String startBusinessDate;

    @ApiModelProperty(value = "回款结束月份")
    private String endBusinessDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "是否异常，0：否 1：是")
    private String isAbnormal;

    @ApiModelProperty(value = "接口id")
    private Long interfaceDataId;

    @ApiModelProperty(value = "记账日期")
    private Integer periodCode;

    @ApiModelProperty(value = "上个月记账日期")
    private Integer lastPeriodCode;

    @ApiModelProperty(value = "开始会计期间")
    private Integer startPeriodCode;

    @ApiModelProperty(value = "结束会计期间")
    private Integer endPeriodCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;
}
