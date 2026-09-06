package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : ta重分类明细表DTO对象
 * @Modified :
 */
@Data
public class TaReclassificationDetailDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "重分类月份")
    private LocalDateTime reclassificationMonth;

    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "租赁大类")
    private String businessCode;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "到账主体")
    private String bankOrgId;

    @ApiModelProperty(value = "业务系统网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankBatchNo;

    @ApiModelProperty(value = "网银付款人名称")
    private String ebankClientName;

    @ApiModelProperty(value = "运营部备注")
    private String operationRemark;

    @ApiModelProperty(value = "TA/溢存款余额")
    private BigDecimal taExcessBalance;

    @ApiModelProperty(value = "租金校验结果")
    private String rentCheck;

    @ApiModelProperty(value = "网银校验结果")
    private String ebankCheck;

    @ApiModelProperty(value = "TA重分类金额")
    private BigDecimal taReclassificationAmount;

    @ApiModelProperty(value = "合同结束日期")
    private LocalDateTime leaseDateEnd;

    @ApiModelProperty(value = "应收租金余额")
    private BigDecimal receivableRentBalance;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "特殊合同状态")
    private String specialContractStatus;

    @ApiModelProperty(value = "TA重分类科目编码")
    private String taAccountCode;

    @ApiModelProperty(value = "TA重分类科目名称")
    private String taAccountName;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "处理状态")
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

    @ApiModelProperty(value = "ta重分类id")
    private Long taReclassificationId;

    @ApiModelProperty(value = "重分类科目编码")
    private String accountCode;
}
