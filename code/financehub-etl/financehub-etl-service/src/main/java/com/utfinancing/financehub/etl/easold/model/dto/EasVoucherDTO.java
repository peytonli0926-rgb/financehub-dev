package com.utfinancing.financehub.etl.easold.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : lixin
 * @Date : Create in 30/10/2023
 */
@Data
public class EasVoucherDTO {

    @ApiModelProperty(value = "主键")
    private String fid;

    @ApiModelProperty(value = "公司编码")
    private String companyNumber;

    @ApiModelProperty(value = "记账日期")
    private String bookedDate;

    @ApiModelProperty(value = "业务日期")
    private String bizDate;

    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    @ApiModelProperty(value = "会计期间-年")
    private Integer periodYear;

    @ApiModelProperty(value = "会计期间-编码")
    private Integer periodNumber;

    @ApiModelProperty(value = "凭证字（凭证类型）")
    private String voucherType;

    @ApiModelProperty(value = "附件数量")
    private Integer attaches;

    @ApiModelProperty(value = "参考信息")
    private String description;

    @ApiModelProperty(value = "凭证号")
    private String voucherNumber;

    @ApiModelProperty(value = "分录行号")
    private Integer entrySeq;

    @ApiModelProperty(value = "摘要")
    private String voucherAbstract;

    @ApiModelProperty(value = "科目")
    private String accountNumber;

    @ApiModelProperty(value = "币种")
    private String currencyNumber;

    @ApiModelProperty(value = "利润中心编码")
    private String profitCenterNumber;

    @ApiModelProperty(value = "汇率")
    private BigDecimal localRate;

    @ApiModelProperty(value = "方向")
    private Integer entryDC;

    @ApiModelProperty(value = "原币金额")
    private BigDecimal originalAmount;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "计量单位")
    private String measurement;

    @ApiModelProperty(value = "核销/挂账")
    private Integer cussent;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "借方金额")
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private BigDecimal creditAmount;

    @ApiModelProperty(value = "制单人")
    private String creator;

    @ApiModelProperty(value = "过账人")
    private String poster;

    @ApiModelProperty(value = "审核人")
    private String auditor;

    @ApiModelProperty(value = "出纳人")
    private String cashier;

    @ApiModelProperty(value = "辅助账行号")
    private Integer asstSeq;

    @ApiModelProperty(value = "辅助账摘要")
    private String assistAbstract;

    @ApiModelProperty(value = "业务日期")
    private String assistBizDate;

    @ApiModelProperty(value = "到期日")
    private String assistEndDate;

    @ApiModelProperty(value = "结算方式")
    private String settlementType;

    @ApiModelProperty(value = "结算号")
    private String settlementNumber;

    @ApiModelProperty(value = "业务编号")
    private String bizNumber;

    @ApiModelProperty(value = "票证号码")
    private String ticketNumber;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    @ApiModelProperty(value = "核算项目1")
    private String asstActType1;

    @ApiModelProperty(value = "核算对象编码1")
    private String asstActNumber1;

    @ApiModelProperty(value = "核算对象名称1")
    private String asstActName1;

    @ApiModelProperty(value = "核算项目2")
    private String asstActType2;

    @ApiModelProperty(value = "核算对象编码2")
    private String asstActNumber2;

    @ApiModelProperty(value = "核算对象名称2")
    private String asstActName2;

    @ApiModelProperty(value = "核算项目3")
    private String asstActType3;

    @ApiModelProperty(value = "核算对象编码3")
    private String asstActNumber3;

    @ApiModelProperty(value = "核算对象名称3")
    private String asstActName3;

    @ApiModelProperty(value = "核算项目4")
    private String asstActType4;

    @ApiModelProperty(value = "核算对象编码4")
    private String asstActNumber4;

    @ApiModelProperty(value = "核算对象名称4")
    private String asstActName4;

    @ApiModelProperty(value = "核算项目5")
    private String asstActType5;

    @ApiModelProperty(value = "核算对象编码5")
    private String asstActNumber5;

    @ApiModelProperty(value = "核算对象名称5")
    private String asstActName5;

    @ApiModelProperty(value = "核算项目6")
    private String asstActType6;

    @ApiModelProperty(value = "核算对象编码6")
    private String asstActNumber6;

    @ApiModelProperty(value = "核算对象名称6")
    private String asstActName6;

    @ApiModelProperty(value = "核算项目7")
    private String asstActType7;

    @ApiModelProperty(value = "核算对象编码7")
    private String asstActNumber7;

    @ApiModelProperty(value = "核算对象名称7")
    private String asstActName7;

    @ApiModelProperty(value = "核算项目8")
    private String asstActType8;

    @ApiModelProperty(value = "核算对象编码8")
    private String asstActNumber8;

    @ApiModelProperty(value = "核算对象名称8")
    private String asstActName8;


    @ApiModelProperty(value = "核算项目9")
    private String asstActType9;

    @ApiModelProperty(value = "核算对象编码9")
    private String asstActNumber9;

    @ApiModelProperty(value = "核算对象名称9")
    private String asstActName9;

    @ApiModelProperty(value = "核算项目10")
    private String asstActType10;

    @ApiModelProperty(value = "核算对象编码10")
    private String asstActNumber10;

    @ApiModelProperty(value = "核算对象名称10")
    private String asstActName10;

    @ApiModelProperty(value = "核算项目11")
    private String asstActType11;

    @ApiModelProperty(value = "核算对象编码11")
    private String asstActNumber11;

    @ApiModelProperty(value = "核算对象名称11")
    private String asstActName11;

    @ApiModelProperty(value = "核算项目12")
    private String asstActType12;

    @ApiModelProperty(value = "核算对象编码12")
    private String asstActNumber12;

    @ApiModelProperty(value = "核算对象名称12")
    private String asstActName12;

    @ApiModelProperty(value = "核算项目13")
    private String asstActType13;

    @ApiModelProperty(value = "核算对象编码13")
    private String asstActNumber13;

    @ApiModelProperty(value = "核算对象名称13")
    private String asstActName13;

    @ApiModelProperty(value = "核算项目14")
    private String asstActType14;

    @ApiModelProperty(value = "核算对象编码14")
    private String asstActNumber14;

    @ApiModelProperty(value = "核算对象名称14")
    private String asstActName14;

    @ApiModelProperty(value = "现金流量标记")
    private Integer itemflag;

    @ApiModelProperty(value = "对方科目分录号")
    private Integer oppAccountSeq;

    @ApiModelProperty(value = "主表项目")
    private String primaryItem;

    @ApiModelProperty(value = "附表项目")
    private String supplyItem;

    @ApiModelProperty(value = "主表系数")
    private Integer primaryCoef;

    @ApiModelProperty(value = "附表系数")
    private Integer supplyCoef;

    @ApiModelProperty(value = "现金流量原币金额")
    private BigDecimal cashflowAmountOriginal;

    @ApiModelProperty(value = "现金流量本位币金额")
    private BigDecimal cashflowAmountLocal;

    @ApiModelProperty(value = "现金流量报告币金额")
    private BigDecimal cashflowAmountRpt;

    @ApiModelProperty(value = "现金流量性质列")
    private String type;

    @ApiModelProperty(value = "现金流量核算项目1")
    private String cashAsstActType1;

    @ApiModelProperty(value = "现金流量核算对象编码1")
    private String cashAsstActNumber1;

    @ApiModelProperty(value = "现金流量核算对象名称1")
    private String cashAsstActName1;

    @ApiModelProperty(value = "现金流量核算项目2")
    private String cashAsstActType2;

    @ApiModelProperty(value = "现金流量核算对象编码2")
    private String cashAsstActNumber2;

    @ApiModelProperty(value = "现金流量核算对象名称2")
    private String cashAsstActName2;

    @ApiModelProperty(value = "现金流量核算项目3")
    private String cashAsstActType3;

    @ApiModelProperty(value = "现金流量核算对象编码3")
    private String cashAsstActNumber3;

    @ApiModelProperty(value = "现金流量核算对象名称3")
    private String cashAsstActName3;

    @ApiModelProperty(value = "现金流量核算项目4")
    private String cashAsstActType4;

    @ApiModelProperty(value = "现金流量核算对象编码4")
    private String cashAsstActNumber4;

    @ApiModelProperty(value = "现金流量核算对象名称4")
    private String cashAsstActName4;

    @ApiModelProperty(value = "现金流量核算项目5")
    private String cashAsstActType5;

    @ApiModelProperty(value = "现金流量核算对象编码5")
    private String cashAsstActNumber5;

    @ApiModelProperty(value = "现金流量核算对象名称5")
    private String cashAsstActName5;

    @ApiModelProperty(value = "现金流量核算项目6")
    private String cashAsstActType6;

    @ApiModelProperty(value = "现金流量核算对象编码6")
    private String cashAsstActNumber6;

    @ApiModelProperty(value = "现金流量核算对象名称6")
    private String cashAsstActName6;

    @ApiModelProperty(value = "现金流量核算项目7")
    private String cashAsstActType7;

    @ApiModelProperty(value = "现金流量核算对象编码7")
    private String cashAsstActNumber7;

    @ApiModelProperty(value = "现金流量核算对象名称7")
    private String cashAsstActName7;

    @ApiModelProperty(value = "现金流量核算项目8")
    private String cashAsstActType8;

    @ApiModelProperty(value = "现金流量核算对象编码8")
    private String cashAsstActNumber8;

    @ApiModelProperty(value = "现金流量核算对象名称8")
    private String cashAsstActName8;

    @ApiModelProperty(value = "报文")
    private String jsonParam;

    @ApiModelProperty(value = "系统来源")
    private String systemCode;

    @ApiModelProperty(value = "是否暂存状态：0：否，1：是")
    private String isStage;

    @ApiModelProperty(value = "索引号")
    private String indexNo;

    @ApiModelProperty(value = "是否校验")
    private String isCheck;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "全局标识")
    private String importKey;

    @ApiModelProperty(value = "来源单ID")
    private String sourceBillId;

    @ApiModelProperty(value = "来源系统唯一号")
    private String sourceSysNo;

    @ApiModelProperty(value = "来源系统单据详情url")
    private String sourceSysBillUrl;

    private int lineNo;

    @ApiModelProperty(value = "来源系统唯一号")
    private String sourceSys;

    @ApiModelProperty(value = "数据来源id,多个逗号分隔")
    private String primaryKey;

    @ApiModelProperty(value = "发送数据状态类型：暂存，提交，审核，过账")
    private String dataStatus;

    @ApiModelProperty(value = "细分场景编码")
    private String subSceneType;

    @ApiModelProperty(value = "外部接口ID")
    private Long interfaceId;

    @ApiModelProperty(value = "临时参数")
    private String uuid;

    @ApiModelProperty(value = "entryId")
    private String entryId;

    @ApiModelProperty(value = "主凭证表摘要")
    private String evVoucherSummary;

    @ApiModelProperty(value = "资金付款批次号")
    private String zjBatchNo;
    @ApiModelProperty(value = "资金付款类型")
    private String businessOperation;
}
