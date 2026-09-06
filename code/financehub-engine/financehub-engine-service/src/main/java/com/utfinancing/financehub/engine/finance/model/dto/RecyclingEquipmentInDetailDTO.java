package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class RecyclingEquipmentInDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "入库日期")
    private String inboundDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "应收期末残值")
    private BigDecimal receivableResidualValueBalance;

    @ApiModelProperty(value = "应收销项税")
    private BigDecimal receivableOuttaxBalance;

    @ApiModelProperty(value = "未实现收益")
    private BigDecimal unrealizedRevenueBalance;

    @ApiModelProperty(value = "承租人保证金")
    private BigDecimal lesseeMarginBalance;

    @ApiModelProperty(value = "财务敞口")
    private BigDecimal financialExposure;

    @ApiModelProperty(value = "回收设备成本")
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "入库时计提减值")
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "生成凭证id 逗号隔开")
    private String voucherId;

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

    @ApiModelProperty(value = "客户代码")
    private String clientCode;
}
