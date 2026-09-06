package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description : 未确认收款明细表(第二层明细)VO对象
 * @Modified :
 */
@Data
public class NonConfirmCollectionSecondDetailVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

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
