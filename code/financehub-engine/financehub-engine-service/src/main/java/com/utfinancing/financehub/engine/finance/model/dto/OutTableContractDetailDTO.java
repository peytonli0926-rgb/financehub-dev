package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-11
 * @Description : 出表ABS合同详情DTO对象
 * @Modified :
 */
@Data
public class OutTableContractDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "出表absId")
    private Long outTableAbsId;

    @ApiModelProperty(value = "借款合同编码")
    private String loanContractCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "封包日应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "封包日应收残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "封包日应收销项税")
    private BigDecimal receivableOuttax;

    @ApiModelProperty(value = "封包日为实现收益")
    private BigDecimal unrealizedRevenue;

    @ApiModelProperty(value = "封包日承租人保证金")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty("会计期间")
    private Integer periodCode;

}
