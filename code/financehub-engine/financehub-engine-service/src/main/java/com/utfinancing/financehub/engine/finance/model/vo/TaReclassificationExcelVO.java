package com.utfinancing.financehub.engine.finance.model.vo;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-24
 * @Description : ta重分类VO对象
 * @Modified :
 */
@Data
public class TaReclassificationExcelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "重分类月份")
    @Excel(name = "重分类月份", dateFormat = "yyyy-MM", cellType = Excel.ColumnType.DATE)
    private LocalDateTime reclassificationMonth;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "TA重分类金额")
    @Excel(name = "TA重分类金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal taReclassificationAmount;

    @ApiModelProperty(value = "处理状态")
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=已复核,4=已传至金蝶,5=已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "是否已生成凭证(0-否，1-是)")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

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

}
