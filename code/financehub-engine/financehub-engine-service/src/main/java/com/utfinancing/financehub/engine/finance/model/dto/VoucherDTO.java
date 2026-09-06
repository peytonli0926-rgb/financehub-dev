package com.utfinancing.financehub.engine.finance.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-01
 * @Description : 凭证表DTO对象
 * @Modified :
 */
@Data
public class VoucherDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "接口表ID")
    private Long interfaceDataId;

    @ApiModelProperty(value = "来源;refInterface")
    private String source;

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "来源系统名称")
    private String systemName;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "业务交易流水号")
    private String orderId;

    @ApiModelProperty(value = "业务场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "业务场景名称")
    private String sceneName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机编码")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "凭证类型;refDict")
    private String voucherType;

    @ApiModelProperty(value = "公司")
    private String signCompany;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

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

    @ApiModelProperty(value = "凭证号")
    private Long voucherNum;

    @ApiModelProperty(value = "凭证生成方式：auto:自动凭证, manual:手工凭证")
    private String voucherWay;

    @ApiModelProperty(value = "制单人工号")
    private String createUserNo;

    @ApiModelProperty(value = "制单人姓名")
    private String createUserName;

    @ApiModelProperty(value = "复核人工号")
    private String recheckUserNo;

    @ApiModelProperty(value = "复核人姓名")
    private String recheckUserName;

    @ApiModelProperty(value = "凭证状态")
    private String voucherStatus;

    @ApiModelProperty(value = "批次ID")
    private Long batchId;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    @ApiModelProperty(value = "细分场景")
    private String subSceneType;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "凭证行")
    private List<VoucherEntryDTO> entryList;

    @ApiModelProperty(value = "银行账号")
    private String bankNo;

    @ApiModelProperty(value = "资金系统交易类型")
    private String transactionType;

    @ApiModelProperty(value = "有效标识1:有效 2:凭证行为空 3:借贷金额未平")
    private String validFlag;

    @ApiModelProperty(value = "是否提交请求")
    private String isSubmit;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "是否是保存资金系统余额表0:否，1：是")
    private String isFundSystemBalance;

    @ApiModelProperty(value = "传送金蝶是否汇总金额，0：汇总，1：不汇总")
    private String isSummary;

    @ApiModelProperty(value = "借款合同编码")
    private String billContractCode;

    @ApiModelProperty(value = "金蝶凭证ID")
    private String easVoucherId;

    @ApiModelProperty(value = "金蝶凭证号")
    private String easVoucherNumber;

    @ApiModelProperty(value = "是否自动生成凭证true：是，false：否")
    private Boolean isAutoVoucherFlag=Boolean.TRUE;

    @ApiModelProperty(value = "是否冲销，0：未冲销，1：已冲销")
    private String isWriteOff;

    @ApiModelProperty(value = "手工凭证id")
    private Long manualId;

}
