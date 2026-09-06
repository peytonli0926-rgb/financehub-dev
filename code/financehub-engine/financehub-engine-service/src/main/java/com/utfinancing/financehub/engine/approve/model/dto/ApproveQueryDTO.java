package com.utfinancing.financehub.engine.approve.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-08
 * @Description :   Approve查询from对象
 * @Modified :
 */
@ApiModel("Approve查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ApproveQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "单据类型")
    private String documentType;

    @ApiModelProperty(value = "单据状态")
    private String documentStatus;

    @ApiModelProperty(value = "提交人工号")
    private String submitterNum;

    @ApiModelProperty(value = "提交人姓名")
    private String submitterName;

    @ApiModelProperty(value = "审批人工号")
    private String approverNum;

    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitDate;

    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approverDate;

    @ApiModelProperty(value = "提交人工号集合")
    private List<String> submitterNumList;

    @ApiModelProperty(value = "单据状态集合")
    private List<String> documentStatusList;

    @ApiModelProperty(value = "审批人工号集合")
    private List<String> approverNumList;
}
