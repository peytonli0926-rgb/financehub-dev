package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description : 线下合同交易数据DTO对象
 * @Modified :
 */
@Data
public class OfflineContractStructureDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    private String contractCodeM;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "设备款")
    private BigDecimal payableDevice;

    @ApiModelProperty(value = "首付款")
    private BigDecimal receivableDownpayment;

    @ApiModelProperty(value = "出租人保险费")
    private BigDecimal lessorInsurance;

    @ApiModelProperty(value = "承租人履约保证金")
    private BigDecimal lesseeMargin;

    @ApiModelProperty(value = "渠道费用")
    private BigDecimal channelFee;

    @ApiModelProperty(value = "手续费收入(含增值税)")
    private BigDecimal receivableCommission;

    @ApiModelProperty(value = "出租人其它成本")
    private BigDecimal lessorOtherincome;

    @ApiModelProperty(value = "厂商返利")
    private BigDecimal receivableRebate;

    @ApiModelProperty(value = "承租人保险费")
    private BigDecimal receivableInsurance;

    @ApiModelProperty(value = "期末残值")
    private BigDecimal receivableResidualValue;

    @ApiModelProperty(value = "其他收入 (含增值税)")
    private BigDecimal receivableOtherincome;

    @ApiModelProperty(value = "咨询服务收入(含增值税)")
    private BigDecimal receivableService;

    @ApiModelProperty(value = "供应商履约保证金")
    private BigDecimal supplierMargin;

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
