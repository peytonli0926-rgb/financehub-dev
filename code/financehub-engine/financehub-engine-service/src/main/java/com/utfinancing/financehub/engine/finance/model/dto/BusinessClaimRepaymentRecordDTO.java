package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-21
 * @Description : 业务系统对还款认领记录DTO对象
 * @Modified :
 */
@Data
public class BusinessClaimRepaymentRecordDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统网银编号")
    private String ebankSerialNumber;


    @ApiModelProperty(value = "认领金额")
    private BigDecimal claimAmount;

    @ApiModelProperty(value = "调整金额")
    private BigDecimal adjustAmount;

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

    @ApiModelProperty(value = "合同号-逗号分隔")
    private String contractCode;

    @ApiModelProperty(value = "合同号列表")
    private List<String> contractCodeList = new ArrayList<>();

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "业务系统交易流水号")
    private String orderId;

    @ApiModelProperty(value = "业务日期(yyyy-MM-dd HH:mm:ss)")
    private String businessDate;

    @ApiModelProperty(value = "网银确认日期")
    private Date businessHappenDate;

    @ApiModelProperty(value = "网银到账金额")
    private BigDecimal bankAmount;

    @ApiModelProperty(value = "做账主体编码-逗号分隔")
    private String orgId;

    @ApiModelProperty(value = "做账主体名称-逗号分隔")
    private String orgName;

    @ApiModelProperty(value = "新业务系统批扣流水号")
    private String newEbankSerialNumber;

    @ApiModelProperty(value = "原入账年月")
    private String incomeYmOld;
}
