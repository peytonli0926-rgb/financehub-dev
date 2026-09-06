package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-02-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_voucher_to_eas_record")
public class VoucherToEasRecordEntity extends Model<VoucherToEasRecordEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //公司编码
    private String companyNumber;

    //记账日期
    private String bookedDate;

    //业务日期
    private String bizDate;

    //会计期间-年
    private Integer periodYear;

    //会计期间-编码
    private Integer periodNumber;

    //凭证字（凭证类型）
    private String voucherType;

    //附件数量
    private Integer attaches;

    //参考信息
    private String description;

    //凭证号
    private String voucherNumber;

    //制单人
    private String creator;

    //过账人
    private String poster;

    //审核人
    private String auditor;

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

    //分录行方向：1 借方 -1贷方
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

    //传送时间
    private LocalDateTime transactionDate;

    private String jsonParam;

    // 出纳人
    private String cashier;
    // fid
    private String fid;
    //数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB
    private String systemCode;

    private String importKey;

    private String sourceBillId;

    private String sourceSysNo;

    private String sourceSysBillUrl;

    private String isCheck;

    private int lineNo;

    private String assistAbstract;

    private String assistBizDate;

    private String assistEndDate;

    private String ticketNumber;

    private String invoiceNumber;
    private String sourceType;

    private String batchUuid;
    private String metaParams;

    @ApiModelProperty(value = "发送数据状态类型：暂存，提交，审核，过账")
    private String dataStatus;

    @ApiModelProperty(value = "原始系统id")
    private String primaryKey;
}
