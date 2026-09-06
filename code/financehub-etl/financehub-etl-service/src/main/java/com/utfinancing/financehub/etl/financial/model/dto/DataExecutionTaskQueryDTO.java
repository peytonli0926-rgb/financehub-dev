package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-07
 * @Description :   DataExecutionTask查询from对象
 * @Modified :
 */
@ApiModel("DataExecutionTask查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class DataExecutionTaskQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "任务开始时间")
    private LocalDateTime taskStartTime;

    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime taskEndTime;

    @ApiModelProperty(value = "业务日期开始时间")
    private LocalDateTime businessDateStart;

    @ApiModelProperty(value = "业务日期结束时间")
    private LocalDateTime businessDateEnd;

    @ApiModelProperty(value = "任务处理数据总条数")
    private Integer dataSize;

    @ApiModelProperty(value = "成功数据条数")
    private Integer dataSuccessSize;

    @ApiModelProperty(value = "失败数据条数")
    private Integer dataFailedSize;

    @ApiModelProperty(value = "任务异常消息")
    private String errorMessage;
}
