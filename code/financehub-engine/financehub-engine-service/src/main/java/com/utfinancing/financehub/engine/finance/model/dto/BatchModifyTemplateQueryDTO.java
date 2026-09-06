package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : robjiang
 * @Date : Create in 2025-06-23
 * @Description :   BatchModifyTemplate查询from对象
 * @Modified :
 */
@ApiModel("BatchModifyTemplate查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BatchModifyTemplateQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "对账月份")
    private String accountCheckingMonth;

    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统的网银编号-小网银")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "comments")
    private String remark;

    @ApiModelProperty(value = "财务初分类")
    private String financialPrimaryClassic;

    @ApiModelProperty(value = "运营部确认款项性质")
    private String confirmAccountProperty;

    @ApiModelProperty(value = "非租对账备注")
    private String nonLeaseAccountCheckingComments;

    @ApiModelProperty(value = "运营部历史备注")
    private String operateHistoryComments;
}
