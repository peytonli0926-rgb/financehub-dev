package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description : 业务系统对还款认领记录VO对象
 * @Modified :
 */
@Data
public class BusinessClaimRepaymentRecordVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "")
    private String systemCode;

    @ApiModelProperty(value = "业务系统网银编号")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "认领金额")
    private String claimAmount;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0:否，1：是）")
    private String delFlag;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "合同号")
    private String contractCode;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "业务系统交易流水号")
    private String orderId;

    @ApiModelProperty(value = "业务日期(yyyy-MM-dd HH:mm:ss)")
    private String businessDate;

}
