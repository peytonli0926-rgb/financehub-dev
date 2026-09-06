package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description : 金蝶科目余额与中台科目余额对账实时表VO对象
 * @Modified :
 */
@Data
public class CheckAccountKingdeeResultVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

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

    @ApiModelProperty(value = "删除标志")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改日期")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "差异状态描述")
    private String diffFlagDesc;

    @ApiModelProperty(value = "科目名称")
    private String accountName;
}
