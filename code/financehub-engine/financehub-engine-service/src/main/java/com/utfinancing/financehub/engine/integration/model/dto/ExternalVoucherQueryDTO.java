package com.utfinancing.financehub.engine.integration.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description :   ExternalVoucher查询from对象
 * @Modified :
 */
@ApiModel("ExternalVoucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExternalVoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "来源系统")
    private String systemCode;

    @ApiModelProperty(value = "公司编码")
    private String companyNumber;

    @ApiModelProperty(value = "记账日期")
    private String bookedDate;

    @ApiModelProperty(value = "业务日期")
    private String bizDate;

    @ApiModelProperty(value = "会计期间-年")
    private Integer periodYear;

    @ApiModelProperty(value = "会计期间-编码")
    private Integer periodNumber;

    @ApiModelProperty(value = "凭证字（凭证类型）")
    private String voucherType;

    @ApiModelProperty(value = "附件数量")
    private Integer attaches;

    @ApiModelProperty(value = "参考信息")
    private String description;

    @ApiModelProperty(value = "凭证号")
    private String voucherNumber;

    @ApiModelProperty(value = "制单人")
    private String creator;

    @ApiModelProperty(value = "过账人")
    private String poster;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "同步到金蝶状态")
    private String easStatus;

    @ApiModelProperty(value = "金蝶返回系统状态标识，成功0000")
    private String easFlag;

    @ApiModelProperty(value = "金蝶返回期间年")
    private Integer easPeriodYear;

    @ApiModelProperty(value = "金蝶返回期间月")
    private Integer easPeriodMonth;

    @ApiModelProperty(value = "金蝶返回凭证类型")
    private String easVoucherType;

    @ApiModelProperty(value = "金蝶返回日志")
    private String easLog;

    @ApiModelProperty(value = "金蝶生成凭证后的凭证编码")
    private String easVoucherNumber;

    @ApiModelProperty(value = "金蝶对应凭证唯一编码")
    private String easVoucherId;
}
