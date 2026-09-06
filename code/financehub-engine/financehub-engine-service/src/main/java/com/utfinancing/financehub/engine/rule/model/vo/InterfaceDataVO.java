package com.utfinancing.financehub.engine.rule.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-14
 * @Description : 接口数据表VO对象
 * @Modified :
 */
@Data
public class InterfaceDataVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "来源系统名称")
    private String systemName;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "交易流水号")
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

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "财务日期")
    private LocalDateTime financeDate;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "接口数据")
    private String interfaceData;

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

    @ApiModelProperty(value = "批次ID")
    private Long batchId;

    @ApiModelProperty(value = "批次类型")
    private String batchType;

    //银行账号签约主体
    @ApiModelProperty("银行账号签约主体)")
    private String bankOrgId;

    @ApiModelProperty("跨合同抵扣保证金标识(0:应收,1:保证金)")
    private String crossContractFlag;

    //网银编号
    private String ebankNumber;

    //网银流水号
    private String ebankSerialNumber;

    @ApiModelProperty("业务付款单号")
    private String payableNumber;

    @ApiModelProperty("资金付款单号")
    private String paymentOrder;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(value = "外部接口Id")
    private String interfaceId;

    @ApiModelProperty(value = "银行流水批次号")
    private String ebankBatchNo;
}
