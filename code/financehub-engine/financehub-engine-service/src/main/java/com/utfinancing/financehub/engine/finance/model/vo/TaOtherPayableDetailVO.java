package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description : ta其他应付款明细VO对象
 * @Modified :
 */
@Data
public class TaOtherPayableDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "重分类月份")
    private LocalDateTime reclassificationMonth;

    @ApiModelProperty(value = "网银到账主体")
    private String bankOrgId;

    @ApiModelProperty(value = "业务系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统名称")
    private String systemName;

    @ApiModelProperty(value = "业务系统网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankBatchNo;

    @ApiModelProperty(value = "财务初分类")
    private String financialPrimaryClassic;

    @ApiModelProperty(value = "运营部确认款项性质")
    private String confirmAccountProperty;

    @ApiModelProperty(value = "重分类金额")
    private BigDecimal reclassificationAmount;

    @ApiModelProperty(value = "重分类科目编码")
    private String accountCode;

    @ApiModelProperty(value = "重分类科目名称")
    private String accountName;

    @ApiModelProperty(value = "入账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "账龄")
    private Integer accountAge;

    @ApiModelProperty(value = "账龄分类")
    private String accountAgeClass;

    @ApiModelProperty(value = "付款客户")
    private String payClientName;

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

    @ApiModelProperty(value = "ta其他应付款汇总id")
    private Long taOtherPayableId;

    @ApiModelProperty(value = "重分类月份展示")
    private String reclassificationMonthStr;

    @ApiModelProperty(value = "网银到账主体名称")
    private String bankOrgName;
}
