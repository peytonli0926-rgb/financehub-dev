package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-20
 * @Description :   CheckAccountDetailResultHis查询from对象
 * @Modified :
 */
@ApiModel("CheckAccountDetailResultHis查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckAccountDetailResultHisQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "对账记录ID")
    private Long recordId;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "科目代码")
    private String accountCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "科目余额")
    private BigDecimal accountRemainBalance;

    @ApiModelProperty(value = "明细余额")
    private BigDecimal detailRemainBalance;

    @ApiModelProperty(value = "差异类型：0无差异，1有差异")
    private String diffFlag;

    @ApiModelProperty(value = "差异金额：科目余额-明细余额")
    private BigDecimal diffBalance;

    @ApiModelProperty(value = "查询的科目余额字段")
    private String balanceField;

    @ApiModelProperty(value = "删除标记")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
