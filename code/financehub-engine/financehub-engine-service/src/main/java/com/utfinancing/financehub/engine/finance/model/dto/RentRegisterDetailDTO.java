package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-08
 * @Description : 出租登记-租金计划DTO对象
 * @Modified :
 */
@Data
public class RentRegisterDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "应付日期")
    private LocalDate planDate;

    @ApiModelProperty(value = "所属期")
    private String period;

    @ApiModelProperty(value = "应收租金")
    private BigDecimal receivableRent;

    @ApiModelProperty(value = "当月应收租金")
    private BigDecimal thisMonthReceivableRent;

    @ApiModelProperty(value = "当月计提税金")
    private BigDecimal thisMonthTax;

    @ApiModelProperty(value = "当月租金收入")
    private BigDecimal thisMonthRentIncome;

    @ApiModelProperty(value = "凭证id(多个逗号分隔)")
    private String voucherId;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "出租登记id")
    private Long rentRegisterId;

}
