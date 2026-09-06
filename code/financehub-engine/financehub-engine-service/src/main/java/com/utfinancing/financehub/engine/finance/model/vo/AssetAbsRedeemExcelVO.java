package com.utfinancing.financehub.engine.finance.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemExcelVO</li>
 * <li>CreateTime : 2024/03/15 14:35</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class AssetAbsRedeemExcelVO {

    @ApiModelProperty(value = "合同编号")
    @Excel(name = "合同编号",width = 20)
    private String contractCode;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态",width = 20)
    private String financialContractStatus;

    @ApiModelProperty(value = "赎回开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "赎回起算日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "实际赎回日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际赎回日",width = 20,dateFormat = "yyyy-MM-dd")
    private Date actualDate;

    @ApiModelProperty(value = "赎回价格")
    @Excel(name = "赎回价格",width = 20,cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal redeemPrice;

    //借款合同编号
    @ApiModelProperty(value = "借款合同编号")
    private String loanContractCode;

    //签约主体
    @ApiModelProperty(value = "签约主体")
    private String orgId;

    //出表期数
    @ApiModelProperty(value = "出表期数")
    private String periods;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;





}
