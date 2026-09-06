package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description :   Voucher查询from对象
 * @Modified :
 */
@ApiModel("Voucher查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherQueryDTO extends BaseQueryDTO{

    @ApiModelProperty("凭证ID")
    private List<Long> voucherIdList;

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;
    private Integer lastPeriodCode;
    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "业务日期-开始")
    private LocalDate businessDateStart;

    @ApiModelProperty(value = "业务日期-结束")
    private LocalDate businessDateEnd;

    @ApiModelProperty(value = "记账日期-开始")
    private LocalDate voucherDateStart;

    @ApiModelProperty(value = "记账日期-结束")
    private LocalDate voucherDateEnd;

    @ApiModelProperty(value = "业务场景")
    private String sceneCode;

    @ApiModelProperty(value = "科目代码")
    private String accountCode;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "凭证头摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证行摘要")
    private String voucherEntrySummary;

    @ApiModelProperty(value = "制单人姓名")
    private String createUserName;

    @ApiModelProperty(value = "复核人姓名")
    private String recheckUserName;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "批次ID")
    private Long batchId;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "辅助帐科目余额表ID")
    private Long accountAssistBalanceId;

    @ApiModelProperty(value = "凭证号集合")
    private List<Long> voucherNumList;

    @ApiModelProperty(value = "凭证类型集合")
    private List<String> voucherTypeList;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    //细分场景
    @ApiModelProperty(value = "细分场景集合")
    private List<String> subSceneTypeList;

    @ApiModelProperty(value = "业务场景集合")
    private List<String> sceneCodeList;

    @ApiModelProperty(value = "科目代码集合")
    private List<String> accountCodeList;

    @ApiModelProperty(value = "科目名称集合")
    private List<String> accountNameList;

    //币种
    @ApiModelProperty(value = "币种")
    private List<String> currencyList;

    @ApiModelProperty(value = "凭证号查询key")
    private String searchKey;

    @ApiModelProperty(value = "制单人姓名集合")
    private List<String> createUserNameList;

    @ApiModelProperty(value = "复核人姓名集合")
    private List<String> recheckUserNameList;

    @ApiModelProperty(value = "客户名称集合")
    private List<String> clientNameList;

    @ApiModelProperty(value = "合同编码集合")
    private List<String> contractCodeList;

    //借款合同编号
    @ApiModelProperty(value = "借款合同编号集合")
    private List<String> billContractCodeList;

    @ApiModelProperty(value = "成本中心集合")
    private List<String> costCentreList;

    @ApiModelProperty(value = "客户编码集合")
    private List<String> clientCodeList;

    @ApiModelProperty("接口id")
    private Long interfaceDataId;

    @ApiModelProperty(value = "是否涉及其他客户及辅助帐（0：否，1：是）")
    private String isRelatedOtherCustomer;

    @ApiModelProperty(value = "是否按照科目编码、科目名称查询")
    private String isAccountCodeFlag;

    @ApiModelProperty(value = "金蝶凭证ID")
    private String easVoucherId;

    @ApiModelProperty(value = "金蝶凭证号")
    private String easVoucherNumber;

}
