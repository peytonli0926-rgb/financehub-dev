package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :   Voucher查询from对象
 * @Modified :
 */
@ApiModel("Voucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "接口表ID")
    private Long interfaceDataId;

    @ApiModelProperty(value = "来源;refInterface")
    private String source;

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "来源系统名称")
    private String systemName;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "业务交易流水号")
    private String orderId;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    private String sceneName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherType;

    @ApiModelProperty(value = "公司")
    private String signCompany;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "凭证生成方式：auto:自动凭证, manual:手工凭证")
    private String voucherWay;

    @ApiModelProperty(value = "制单人工号")
    private String createUserNo;

    @ApiModelProperty(value = "制单人姓名")
    private String createUserName;

    @ApiModelProperty(value = "复核人工号")
    private String recheckUserNo;

    @ApiModelProperty(value = "复核人姓名")
    private String recheckUserName;

    @ApiModelProperty(value = "凭证状态")
    private String voucherStatus;

    @ApiModelProperty(value = "批次ID")
    private Long batchId;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;
}
