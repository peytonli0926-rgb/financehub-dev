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
 * @Description :   RecyclingEquipmentOutDetail查询from对象
 * @Modified :
 */
@ApiModel("RecyclingEquipmentOutDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RecyclingEquipmentOutDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "出库日期")
    private String outboundDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "回收设备成本")
    private BigDecimal recyclingEquipmentCost;

    @ApiModelProperty(value = "回收设备减值")
    private BigDecimal provisionForImpairment;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;

    @ApiModelProperty(value = "生成凭证id 逗号隔开")
    private String voucherId;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "回收设备出库数据id集合")
    private List<Long> recyclingEquipmentOutIdList;

    @ApiModelProperty(value = "签约主体id集合")
    private List<String> orgIds;

    @ApiModelProperty(value = "出库日期 开始")
    private String outboundDateStart;

    @ApiModelProperty(value = "出库日期 结束")
    private String outboundDateEnd;
}
