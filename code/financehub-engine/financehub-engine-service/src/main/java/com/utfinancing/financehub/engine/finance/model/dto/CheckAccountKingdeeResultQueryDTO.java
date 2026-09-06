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
 * @Date : Create in 2024-03-27
 * @Description :   CheckAccountKingdeeResult查询from对象
 * @Modified :
 */
@ApiModel("CheckAccountKingdeeResult查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class CheckAccountKingdeeResultQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "对账记录ID")
    private Long recordId;

    @ApiModelProperty(value = "期间")
    private Integer periodCode;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "科目代码")
    private String accountCode;

    @ApiModelProperty(value = "中台科目余额")
    private String accountRemainBalance;

    @ApiModelProperty(value = "金蝶科目余额")
    private String kingdeeRemainBalance;

    @ApiModelProperty(value = "差异类型 0 无差异 1 有差异")
    private String diffFlag;

    @ApiModelProperty(value = "差异金额 金蝶科目余额-中台科目余额")
    private String diffBalance;
}
