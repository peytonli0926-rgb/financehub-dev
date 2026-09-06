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
 * @Description :   RecyclingEquipmentIn查询from对象
 * @Modified :
 */
@ApiModel("RecyclingEquipmentIn查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RecyclingEquipmentInQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "入库日期")
    private String inboundDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "财务敞口")
    private BigDecimal financialExposure;

    @ApiModelProperty(value = "回收设备成本")
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "入库时计提减值")
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "回收设备入库数据id集合")
    private List<Long> recyclingEquipmentInIdList;

    @ApiModelProperty(value = "签约主体id集合")
    private List<String> orgIds;

    @ApiModelProperty(value = "入库日期 开始")
    private String inboundDateStart;

    @ApiModelProperty(value = "入库日期 结束")
    private String inboundDateEnd;
}
