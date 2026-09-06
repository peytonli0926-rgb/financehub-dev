package com.utfinancing.financehub.engine.claim.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo</li>
 * <li>CreateTime : 2023/11/28 09:55</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class ClaimOrderCostVo {

    //日期,签约主体,合同,客户,系统来源,期数

    @ApiModelProperty("签约主体")
    private String orgId;

    @ApiModelProperty("合同编码")
    private String contractNum;

    @ApiModelProperty("系统来源")
    private String systemSource;

    @ApiModelProperty("业务日期(yyyy-MM)")
    private String accountDate;

    @ApiModelProperty("金额")
    private BigDecimal amount;

}
