package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description :   AccountAssistBalance查询from对象
 * @Modified :
 */
@ApiModel("核算项目余额表查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountAssistBalanceQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;
    private Integer lastPeriodCode;

//    @ApiModelProperty(value = "币种")
//    private String currencyCode;

//    @ApiModelProperty(value = "签约主体")
//    private String orgId;

//    @ApiModelProperty(value = "排除范围")
//    private List<String> excludes;

//    @ApiModelProperty(value = "取值范围")
//    private List<String> status;

//    @ApiModelProperty(value = "合同编号")
//    private String contractCode;

//    @ApiModelProperty(value = "客户编号")
//    private String clientCode;

//    @ApiModelProperty(value = "业务类型")
//    private String businessCode;

//    @ApiModelProperty(value = "借款合同编号")
//    private String billContractCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;
//
//    @ApiModelProperty(value = "科目名称")
//    private String accountName;

//    @ApiModelProperty(value = "客户名称")
//    private String clientName;

    @ApiModelProperty(value = "科目编码范围开始")
    private String accountCodeStart;

    @ApiModelProperty(value = "科目编码范围结束")
    private String accountCodeEnd;

    @ApiModelProperty(value = "会计期间开始")
    private Integer periodCodeStart;

    @ApiModelProperty(value = "会计期间结束")
    private Integer periodCodeEnd;

    @ApiModelProperty(value = "审批状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "排除范围集合")
    //排除范围 无发生不展示/余额为零且无发生不显示/余额为零不显示/本年无发生额不显示/余额为零且本年无发生不显示
    private List<String> exceptList;

    @ApiModelProperty(value = "合同编号集合")
    private List<String> contractCodes;

    private String queryType;

    @ApiModelProperty(value = "前端赋值 查询传 query 导出传 export")
    private String searchType;
    private Integer limit;
    private Integer offset;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "科目名称集合")
    private List<String> accountNameList;

    @ApiModelProperty(value = "币种集合")
    private List<String> currencyCodeList;

    @ApiModelProperty(value = "借款合同编号集合")
    private List<String> billContractCodeList;

    @ApiModelProperty(value = "会计结束期间的年初期间")
    private Integer periodYearStart;

    private Integer assistPeriodCodeStart;

    private Integer assistPeriodCodeEnd;

    private Integer otherPeriodCodeStart;

    private Integer otherPeriodCodeEnd;
}
