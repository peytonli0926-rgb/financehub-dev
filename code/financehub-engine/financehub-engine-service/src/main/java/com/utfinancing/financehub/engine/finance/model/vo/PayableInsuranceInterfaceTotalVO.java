package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-30
 * @Description : 投保事件累计金额VO对象
 * @Modified :
 */
@Data
public class PayableInsuranceInterfaceTotalVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "实际计划支付保险费")
    private String actualPayableInsuaranceAmount;

    @ApiModelProperty(value = "应付保险费")
    private String payableInsuranceAmount;

    @ApiModelProperty(value = "应付保险费余额")
    private String payableInsuranceBalance;

    @ApiModelProperty(value = "是否可用1:可用 0:不可用")
    private String enableFlag;

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

}
