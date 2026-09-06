package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-11
 * @Description : 币别VO对象
 * @Modified :
 */
@Data
public class CurrencyVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "编码")
    private String currencyCode;

    @ApiModelProperty(value = "名称")
    private String currenctName;

    @ApiModelProperty(value = "金蝶主键ID")
    private String easId;

    @ApiModelProperty(value = "金蝶编码")
    private String easCode;

    @ApiModelProperty(value = "金蝶编码状态：普通=1,作废=2")
    private String easStatus;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
