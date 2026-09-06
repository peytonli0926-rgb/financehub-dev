package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-10
 * @Description : 手工表VO对象
 * @Modified :
 */
@Data
public class ManualVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期（财务日期）")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "币种编码")
    private String currencyCode;

    @ApiModelProperty(value = "处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)")
    private String processStatus;

    @ApiModelProperty(value = "流程实例id")
    private Long processInstanceId;

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

    @ApiModelProperty(value = "凭证类型")
    private String voucherType;

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "摘要内容")
    private String voucherSummary;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "汇率")
    private BigDecimal rate;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty("是否冲销（0：否，1：是）")
    private String isWriteOff;

    @ApiModelProperty("数据来源:KJFP：开票认领")
    private String sourceFrom;

    //复核人工号
    private String recheckUserNo;

    //复核人姓名
    private String recheckUserName;

    @ApiModelProperty("数据来源Id")
    private String sourceId;

}
