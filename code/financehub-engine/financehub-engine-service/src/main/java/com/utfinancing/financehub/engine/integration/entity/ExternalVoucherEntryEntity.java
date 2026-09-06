package com.utfinancing.financehub.engine.integration.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 外部业务系统凭证分录实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_external_voucher_entry")
public class ExternalVoucherEntryEntity extends Model<ExternalVoucherEntryEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //外部凭证ID
    private Long externalVoucherId;

    //分录行号
    private Integer entrySeq;

    //摘要
    private String voucherAbstract;

    //科目
    private String accountNumber;

    //币种
    private String currencyNumber;

    //利润中心编码
    private String profitCenterNumber;

    //汇率
    private String localRate;

    //方向
    private Integer entryDC;

    //原币金额
    private String originalAmount;

    //数量
    private String qty;

    //计量单位
    private String measurement;

    //单价
    private String price;

    //借方金额
    private String debitAmount;

    //贷方金额
    private String creditAmount;

    //辅助账行号
    private Integer asstSeq;

    //业务编号
    private String bizNumber;

    //结算方式
    private String settlementNumber;

    //结算号
    private String settlementType;

    //核销/挂账
    private Integer cussent;

    //核算项目1
    private String asstActType1;

    //核算对象编码1
    private String asstActNumber1;

    //核算对象名称1
    private String asstActName1;

    //核算项目2
    private String asstActType2;

    //核算对象编码2
    private String asstActNumber2;

    //核算对象名称2
    private String asstActName2;

    //核算项目3
    private String asstActType3;

    //核算对象编码3
    private String asstActNumber3;

    //核算对象名称3
    private String asstActName3;

    //核算项目4
    private String asstActType4;

    //核算对象编码4
    private String asstActNumber4;

    //核算对象名称4
    private String asstActName4;

    //核算项目5
    private String asstActType5;

    //核算对象编码5
    private String asstActNumber5;

    //核算对象名称5
    private String asstActName5;

    //核算项目6
    private String asstActType6;

    //核算对象编码6
    private String asstActNumber6;

    //核算对象名称6
    private String asstActName6;

    //核算项目7
    private String asstActType7;

    //核算对象编码7
    private String asstActNumber7;

    //核算对象名称7
    private String asstActName7;

    //核算项目8
    private String asstActType8;

    //核算对象编码8
    private String asstActNumber8;

    //核算对象名称8
    private String asstActName8;

    //现金流量标记
    private Integer itemflag;

    //对方科目分录号
    private Integer oppAccountSeq;

    //主表项目
    private String primaryItem;

    //附表项目
    private String supplyItem;

    //主表系数
    private Integer primaryCoef;

    //附表系数
    private Integer supplyCoef;

    //现金流量原币金额
    private BigDecimal cashflowAmountOriginal;

    //现金流量本位币金额
    private BigDecimal cashflowAmountLocal;

    //现金流量报告币金额
    private BigDecimal cashflowAmountRpt;

    //现金流量性质列
    private String type;

    //现金流量核算项目1
    private String cashAsstActType1;

    //现金流量核算对象编码1
    private String cashAsstActNumber1;

    //现金流量核算对象名称1
    private String cashAsstActName1;

    //现金流量核算项目2
    private String cashAsstActType2;

    //现金流量核算对象编码2
    private String cashAsstActNumber2;

    //现金流量核算对象名称2
    private String cashAsstActName2;

    //现金流量核算项目3
    private String cashAsstActType3;

    //现金流量核算对象编码3
    private String cashAsstActNumber3;

    //现金流量核算对象名称3
    private String cashAsstActName3;

    //现金流量核算项目4
    private String cashAsstActType4;

    //现金流量核算对象编码4
    private String cashAsstActNumber4;

    //现金流量核算对象名称4
    private String cashAsstActName4;

    //现金流量核算项目5
    private String cashAsstActType5;

    //现金流量核算对象编码5
    private String cashAsstActNumber5;

    //现金流量核算对象名称5
    private String cashAsstActName5;

    //现金流量核算项目6
    private String cashAsstActType6;

    //现金流量核算对象编码6
    private String cashAsstActNumber6;

    //现金流量核算对象名称6
    private String cashAsstActName6;

    //现金流量核算项目7
    private String cashAsstActType7;

    //现金流量核算对象编码7
    private String cashAsstActNumber7;

    //现金流量核算对象名称7
    private String cashAsstActName7;

    //现金流量核算项目8
    private String cashAsstActType8;

    //现金流量核算对象编码8
    private String cashAsstActNumber8;

    //现金流量核算对象名称8
    private String cashAsstActName8;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;


}
