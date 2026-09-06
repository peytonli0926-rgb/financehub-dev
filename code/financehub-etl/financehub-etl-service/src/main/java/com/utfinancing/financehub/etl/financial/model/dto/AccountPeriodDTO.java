package com.utfinancing.financehub.etl.financial.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : 会计期间DTO对象
 * @Modified :
 */
@Data
public class AccountPeriodDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "期间编码")
    private Integer periodCode;

    @ApiModelProperty(value = "会计年度")
    private Integer periodYear;

    @ApiModelProperty(value = "会计季度")
    private Integer periodQuarter;

    @ApiModelProperty(value = "期间")
    private Integer periodNumber;

    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "期间名称")
    private String periodName;

    @ApiModelProperty(value = "金蝶主键ID")
    private String easId;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
