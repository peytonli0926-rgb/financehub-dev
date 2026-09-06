package com.utfinancing.financehub.engine.verification.model.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : bruyang
 * @Date : Create in 2023-10-23
 * @Description : 诉讼费转费用详情表VO对象
 * @Modified :
 */
@Data
public class CourtCostDetailsVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "诉讼费转费用id")
    private Long courtCostId;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "签约主体名称")
    private String orgName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "记账日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

    @ApiModelProperty(value = "转费用金额")
    private BigDecimal transgerCostAmount;

    @ApiModelProperty(value = "成本中心")
    private String costCenter;

    @ApiModelProperty(value = "是否删除（0-否，1-是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "凭证id")
    private String voucherId;

}
