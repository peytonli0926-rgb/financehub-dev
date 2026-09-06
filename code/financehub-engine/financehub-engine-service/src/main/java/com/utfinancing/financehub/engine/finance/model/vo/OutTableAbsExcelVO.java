package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.OutTableAbsExcelVO</li>
 * <li>CreateTime : 2024/03/11 16:39</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel("出表导入AbsVo")
@Data
public class OutTableAbsExcelVO {

    @ApiModelProperty(value = "借款合同编号")
    @Excel(name = "借款合同编号",width = 20)
    private String loanContractCode;

    @ApiModelProperty(value = "期数")
    @Excel(name = "出表期数",width = 20)
    private String periods;

    @ApiModelProperty(value = "管理人")
    @Excel(name = "管理人",width = 20)
    private String administrator;

    @ApiModelProperty(value = "封包日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "封包日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date closeDate;

    @ApiModelProperty(value = "发行日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发行日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date releaseDate;

    @ApiModelProperty(value = "转让价格")
    @Excel(name = "转让价格",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "合同数量")
    @Excel(name = "合同数量",width = 20)
    private Integer contractNum;

    @ApiModelProperty(value = "计算周期")
    @Excel(name = "计算周期",width = 20)
    private String calculationPeriod;

    @ApiModelProperty(value = "转付周期")
    @Excel(name = "转付周期",width = 20)
    private String transferPeriod;

    @ApiModelProperty(value = "兑付周期")
    @Excel(name = "兑付周期",width = 20)
    private String cashPeriod;

}
