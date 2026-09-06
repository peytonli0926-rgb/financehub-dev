package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.engine.finance.model.dto.RepaymentPlanSaveDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description : 偿还计划测算表VO对象
 * @Modified :
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RepaymentPlanVO extends RepaymentPlanSaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

    @ApiModelProperty("网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty("结算方式")
    private String settlementWay;

    @ApiModelProperty("归还日期")
    private Date businessDate;

    @ApiModelProperty("归还利息")
    private BigDecimal recycleInterestAmount;

    @ApiModelProperty("归还本金")
    private BigDecimal recyclePrincipalAmount;

}
