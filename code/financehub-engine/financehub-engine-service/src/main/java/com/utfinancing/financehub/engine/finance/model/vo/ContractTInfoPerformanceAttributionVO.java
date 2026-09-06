package com.utfinancing.financehub.engine.finance.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.ContractTInfoPerformanceAttributionVO</li>
 * <li>CreateTime : 2024/01/04 14:42</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "交叉销售分成")
@Data
public class ContractTInfoPerformanceAttributionVO {

    @ApiModelProperty("公司")
    private String orgId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("员工名称")
    private String staffName;

    @ApiModelProperty("占比")
    private BigDecimal salesPercentage;

    @ApiModelProperty("金额")
    private BigDecimal amount;

}
