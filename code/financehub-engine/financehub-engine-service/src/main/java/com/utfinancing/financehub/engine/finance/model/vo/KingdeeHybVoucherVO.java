package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-02
 * @Description : VO对象
 * @Modified :
 */
@Data
public class KingdeeHybVoucherVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "金蝶凭证id")
    private String fid;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "合同编码")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编码")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "fassgrpId")
    private String fassgrpId;

    @ApiModelProperty(value = "币种")
    private String currencyName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "借方金额")
    private String debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private String creditAmount;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "(未执行：NOT_EXECUTE，进行中：RUNNING，成功：SUCCESS，失败：FAILED)")
    private String messageStatus;

    @ApiModelProperty(value = "报错信息")
    private String messageError;

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

    @ApiModelProperty(value = "会计期间")
    private String periodCode;

    @ApiModelProperty(value = "摘要")
    private String abstractContent;

}
