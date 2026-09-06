package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : 折价转让-汇总VO对象
 * @Modified :
 */
@Data
public class ConvertTransferVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "批次")
    private String batch;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime financeDate;

    @ApiModelProperty(value = "转让方")
    private String transferParty;

    @ApiModelProperty(value = "受让方")
    private String transfereeParty;

    @ApiModelProperty(value = "基准日")
    private LocalDateTime referenceDate;

    @ApiModelProperty(value = "交易日")
    private LocalDateTime tradeDate;

    @ApiModelProperty(value = "转让价格")
    private BigDecimal transferPrice;

    @ApiModelProperty(value = "合同数量")
    private Integer contractNum;

    @ApiModelProperty(value = "转让后是否开发票（0：否，1：是）")
    private String isInvoiceFlag;

    @ApiModelProperty(value = "流程id")
    private Long processInstanceId;

    @ApiModelProperty(value = "处理状态,1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "是否已生成凭证（0：未生成1：已生成）默认0")
    private String isGenerateVoucher;

    @ApiModelProperty(value = "凭证id,多个按照逗号分隔")
    private String voucherId;

    @ApiModelProperty(value = "生成凭证报错信息")
    private String errorInfo;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "是否删除（0：未删除1：删除）默认0")
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
