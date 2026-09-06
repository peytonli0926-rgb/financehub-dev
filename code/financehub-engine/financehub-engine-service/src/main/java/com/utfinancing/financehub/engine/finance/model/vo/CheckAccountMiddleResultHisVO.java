package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description : 金蝶中间表与中台科目发生额对账历史表VO对象
 * @Modified :
 */
@Data
public class CheckAccountMiddleResultHisVO implements Serializable{
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

    @ApiModelProperty(value = "客户代码")
    private String clientCode;

    @ApiModelProperty(value = "合同代码")
    private String contractCode;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "批次号")
    private String billContractCode;

    @ApiModelProperty(value = "中台科目发生额")
    private String accountRemainBalance;

    @ApiModelProperty(value = "金蝶中间表科目发生额")
    private String kingdeeRemainBalance;

    @ApiModelProperty(value = "差异标志 0 无差异 1 有差异")
    private String diffFlag;

    @ApiModelProperty(value = "差异金额 金蝶中间表科目发生额-中台科目发生额")
    private String diffBalance;

    @ApiModelProperty(value = "删除标志")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "客户名称名称")
    private String clientName;

    @ApiModelProperty(value = "差异状态描述")
    private String diffFlagDesc;

    @ApiModelProperty(value = "科目名称")
    private String accountName;
}
