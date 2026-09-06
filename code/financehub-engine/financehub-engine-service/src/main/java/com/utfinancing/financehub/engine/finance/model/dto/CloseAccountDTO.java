package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-16
 * @Description : 系统关账期间表DTO对象
 * @Modified :
 */
@Data
public class CloseAccountDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "系统来源（统一平台：TYPT,商用车系统：SYCXT,小微系统：XWXT,乘用车系统:CYCXT）	")
    private String systemCode;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "关账日期")
    private String closeDate;

    @ApiModelProperty(value = "关账操作日期")
    private String operateDate;

    @ApiModelProperty(value = "关账人")
    private String operateUser;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除0：否，1：是")
    private String delFlag;

}
