package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description :   PostalStorageFeeDetails查询from对象
 * @Modified :
 */
@ApiModel("PostalStorageFeeDetails查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostalStorageFeeDetailsQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "邮储手续费表id,详情跳转参数")
    private Long postalStorageFeeId;

    @ApiModelProperty(value = "记账日期")
    private Date accountDate;

    @ApiModelProperty(value = "业务日期")
    private Date businessDate;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;
    private List<String> contractCodeList;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "邮储项目类型")
    private String postalStorageProjectType;

    @ApiModelProperty(value = "起租日")
    private Date leaseDateStart;

    @ApiModelProperty(value = "租赁期限")
    private Integer leaseTerm;

    @ApiModelProperty(value = "支付手续费金额")
    private BigDecimal payableProcedureCost;

    @ApiModelProperty(value = "当期分摊金额")
    private BigDecimal allocationAmount;

    @ApiModelProperty(value = "分摊余额")
    private BigDecimal allocationBalance;

    @ApiModelProperty(value = "已分摊期数")
    private Integer allocatedPeriods;

    @ApiModelProperty(value = "凭证id")
    private Long voucherId;
}
