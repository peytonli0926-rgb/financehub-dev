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
 * @Date : Create in 2024-03-22
 * @Description :   NonConfirmCollectionSecondDetail查询from对象
 * @Modified :
 */
@ApiModel("NonConfirmCollectionSecondDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class NonConfirmCollectionSecondDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "未确认收款汇总表id")
    private Long sumId;

    @ApiModelProperty(value = "业务系统")
    private String systemCode;

    @ApiModelProperty(value = "业务系统名称")
    private String systemName;

    @ApiModelProperty(value = "网银到账日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "网银确认日期")
    private LocalDateTime businessHappenDate;

    @ApiModelProperty(value = "币种")
    private String currencyType;

    @ApiModelProperty(value = "网银到账金额")
    private String bankAmount;

    @ApiModelProperty(value = "剩余未确认金额")
    private String remainNonConfirmAmount;

    @ApiModelProperty(value = "已确认金额")
    private String confirmAmount;
}
