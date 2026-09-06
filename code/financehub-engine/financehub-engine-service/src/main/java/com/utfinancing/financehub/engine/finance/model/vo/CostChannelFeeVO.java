package com.utfinancing.financehub.engine.finance.model.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-13
 * @Description : 成本类支付-经销商服务费、外部渠道费，海通渠道费VO对象
 * @Modified :
 */
@Data
public class CostChannelFeeVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "渠道类型（1：经销商服务费,2：外部渠道费,3：海通渠道费,4：收车费,5：抵押费,6：解抵押费,7：安装费，8：服务费,9:设备款）")
    private String channelType;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "渠道方编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道方名称")
    private String channelName;

    @ApiModelProperty(value = "主机厂")
    private String hostFactory;

    @ApiModelProperty(value = "实付金额（含税）")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "对应交易结构金额（不含税）")
    private BigDecimal noTaxTransactionAmount;

    @ApiModelProperty(value = "交易结构调整类型")
    private String structureType;

    @ApiModelProperty(value = "凭证id,多个以逗号分隔")
    private String voucherIds;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0：否，1：是）")
    private String delFlag;

    //金额（不含税）
    @ApiModelProperty(value = "金额（不含税）")
    private BigDecimal noTaxAmount;

    //税额
    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "业务日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate businessDate;

    @ApiModelProperty("费用大类（费用大类(1:GPS,2:手环设备款,3:经销商服务费、外部渠道费、海通渠道费,4:收车费、抵押费、解抵押费)）")
    private String expenseMainCategoryType;

    @ApiModelProperty("批量类型")
    private String batchType;

    @ApiModelProperty("是否自动生成0：否，1：是")
    private String isAutoGenerate;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同状态")
    private String contractStatus;

    @ApiModelProperty("财务合同状态")
    private String financialContractStatus;

    @ApiModelProperty(value = "是否区分合同状态")
    private String isContractStatus;

    @ApiModelProperty(value = "处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)")
    private String processStatus;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime financialDate;

    @ApiModelProperty("流程实例id")
    private Long processInstanceId;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体")
    @Excel(name = "签约主体",width = 20)
    private String orgIdName;



}
