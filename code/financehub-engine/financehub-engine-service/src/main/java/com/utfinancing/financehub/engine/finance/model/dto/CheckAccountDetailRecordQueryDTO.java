package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :   CheckAccountDetailRecord查询from对象
 * @Modified :
 */
@ApiModel("CheckAccountDetailRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckAccountDetailRecordQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "对账提交人")
    private String submitBy;

    @ApiModelProperty(value = "对账类型：JOB:定时任务执行,MANAUL:界面手动执行")
    private String checkType;

    @ApiModelProperty(value = "执行状态：In-Progress,Finish,Error")
    private String executeStatus;

    @ApiModelProperty(value = "对账开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "对账结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "对账目标 明细余额 Detail 金蝶 Kingdee 金蝶中间表 Middle")
    private String checkTarget;
}
