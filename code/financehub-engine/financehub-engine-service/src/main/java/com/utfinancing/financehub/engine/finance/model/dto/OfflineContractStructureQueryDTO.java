package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :   OfflineContractStructure查询from对象
 * @Modified :
 */
@ApiModel("OfflineContractStructure查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OfflineContractStructureQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractCodeList;

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
}
