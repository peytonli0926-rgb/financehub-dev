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
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :   RecyclingEquipmentInDetail查询from对象
 * @Modified :
 */
@ApiModel("RecyclingEquipmentInDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RecyclingEquipmentInDetailQueryDTO extends BaseQueryDTO{

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

    @ApiModelProperty(value = "回收设备入库详细数据id集合")
    private List<Long> recyclingEquipmentInDetailIdList;

    @ApiModelProperty(value = "汇总表ID")
    private String id;
}
