package com.utfinancing.financehub.engine.integration.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description : 外部业务系统凭证分录VO对象
 * @Modified :
 */
@Data
public class ExternalVoucherEntryVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "外部凭证ID")
    private Long externalVoucherId;

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
    private String localRate;

    @ApiModelProperty(value = "方向")
    private Integer entryDC;

    @ApiModelProperty(value = "原币金额")
    private String originalAmount;

    @ApiModelProperty(value = "数量")
    private String qty;

    @ApiModelProperty(value = "计量单位")
    private String measurement;

    @ApiModelProperty(value = "单价")
    private String price;

    @ApiModelProperty(value = "借方金额")
    private String debitAmount;

    @ApiModelProperty(value = "贷方金额")
    private String creditAmount;

    @ApiModelProperty(value = "辅助账行号")
    private Integer asstSeq;

    @ApiModelProperty(value = "业务编号")
    private String bizNumber;

    @ApiModelProperty(value = "结算方式")
    private String settlementNumber;

    @ApiModelProperty(value = "结算号")
    private String settlementType;

    @ApiModelProperty(value = "核销/挂账")
    private Integer cussent;

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

}
