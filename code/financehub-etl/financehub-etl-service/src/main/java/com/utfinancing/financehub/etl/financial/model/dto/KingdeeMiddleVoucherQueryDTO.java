package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-03
 * @Description :   KingdeeMiddleVoucher查询from对象
 * @Modified :
 */
@ApiModel("KingdeeMiddleVoucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeMiddleVoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "凭证编号")
    private String voucherNumber;

    @ApiModelProperty(value = "easFlag")
    private String easFlag;

    @ApiModelProperty(value = "easbzCode")
    private String easbzCode;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "币种")
    private String currencyNumber;

    @ApiModelProperty(value = "借方发生额")
    private String debitAmount;

    @ApiModelProperty(value = "贷方发生额")
    private String creditAmount;

    @ApiModelProperty(value = "摘要")
    private String voucherAbstract;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "银行账号")
    private String ebankNo;

    @ApiModelProperty(value = "相同凭证分组id")
    private Long batchId;

    @ApiModelProperty(value = "(未执行：NOT_EXECUTE，进行中：RUNNING，成功：SUCCESS，失败：FAILED)")
    private String messageStatus;

    @ApiModelProperty(value = "报错信息")
    private String messageError;
}
