package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-10
 * @Description :   Manual查询from对象
 * @Modified :
 */
@ApiModel("Manual查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ManualQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "业务开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startBusinessDate;

    @ApiModelProperty(value = "业务结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endBusinessDate;

    @ApiModelProperty(value = "记账开始日期（财务日期）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startVoucherDate;

    @ApiModelProperty(value = "记账结束日期（财务日期）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endVoucherDate;

    @ApiModelProperty(value = "币种编码")
    private String currencyCode;

    @ApiModelProperty(value = "凭证类型")
    private String voucherType;

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "处理状态(1:已录入，2：已提交，3：已复核4：已传至金蝶)")
    private String processStatus;

    @ApiModelProperty(value = "手工凭证Id")
    private Long id;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "手工凭证Id集合")
    private List<Long> idList;
}
