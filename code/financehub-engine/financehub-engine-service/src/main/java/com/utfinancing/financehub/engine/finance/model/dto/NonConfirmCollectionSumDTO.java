package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description : 未确认收款汇总表DTO对象
 * @Modified :
 */
@Data
public class NonConfirmCollectionSumDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "网银编号-资金系统")
    private String ebankNumber;

    @ApiModelProperty(value = "认领主体")
    private String orgId;

    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    @ApiModelProperty(value = "业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)")
    private String ebankSerialNumber;

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

    @ApiModelProperty(value = "认领主体中文名称")
    private String orgName;

    @ApiModelProperty(value = "到账主体中文名称")
    private String collectionAccountsBankName;

    @ApiModelProperty(value = "业务系统的网银编号")
    private String businessEbankNumber;

    @ApiModelProperty(value = "资金系统、业务系统网银编号映射表ID")
    private Long ebankMappingId;

}
