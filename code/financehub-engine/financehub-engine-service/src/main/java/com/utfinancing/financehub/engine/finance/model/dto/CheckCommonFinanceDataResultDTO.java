package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description : 中台对账数据结果表DTO对象
 * @Modified :
 */
@Data
public class CheckCommonFinanceDataResultDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "记录ID")
    private Long recordId;

    @ApiModelProperty(value = "执行日期， 分区字段")
    private LocalDateTime executeDate;

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "关联字段")
    private String joinField;

    @ApiModelProperty(value = "查询字段")
    private String queryField;

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

    @ApiModelProperty(value = "对接业务系统代码")
    private String dbCode;

    @ApiModelProperty(value = "展示字段")
    private String showField;

    @ApiModelProperty(value = "金额对比结果字段")
    private String compareResultField;

    @ApiModelProperty(value = "金额一致结果字段")
    private String compareFlagField;

}
