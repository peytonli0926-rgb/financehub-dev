package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description : 科目余额与明细余额对账记录表VO对象
 * @Modified :
 */
@Data
public class CheckAccountDetailRecordVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

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

    @ApiModelProperty(value = "删除标志")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "版本编号")
    private String version;

    @ApiModelProperty(value = "对账目标 明细余额 Detail 金蝶 Kingdee 金蝶中间表 Middle")
    private String checkTarget;
}
