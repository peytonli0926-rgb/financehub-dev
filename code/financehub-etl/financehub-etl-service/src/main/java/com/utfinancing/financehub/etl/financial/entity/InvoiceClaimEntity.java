package com.utfinancing.financehub.etl.financial.entity;

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
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author bruyang
 * @since 2023-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_invoice_claim")
public class InvoiceClaimEntity extends Model<InvoiceClaimEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //单据编号
    private String documentNum;

    //企业编号
    private String enterpriseNum;

    //销方税号
    private String sellerTaxCode;

    //开票点代码
    private String invoicePointCode;

    //发票类型
    private String invoiceType;

    //付款人
    private String payer;

    //客户行业
    private String clientIndustry;

    //单据日期
    private LocalDateTime documentDate;

    //合同状态
    private String contractStatus;

    //价税合计（含税金额）
    private String taxAmount;

    //税率
    private String taxRate;

    //金额 (不含税)
    private BigDecimal noTaxAmount;

    //税额
    private BigDecimal taxValue;

    //是否先开开票(0:否，1：是)
    private String isFirstInvoice;

    //上期租金是否全额缴纳(0:否1：是)
    private String isRentPayFull;

    //是否暂不开票（0：否，1：是）
    private String isHoldInvoice;

    //购方识别号（税号）
    private String purchaserTaxCode;

    //购方名称
    private String purchaserName;

    //购方地址
    private String purchaserDress;

    //购方电话
    private String purchaserTel;

    //购方银行名称
    private String purchaserBankName;

    //购方银行账号
    private String purchaserBankNum;

    //实收日期/计划还款日期
    private LocalDateTime planRepayDate;

    //期数
    private String periodNum;

    //商品编码
    private String productCode;

    //商品名称
    private String productName;

    //单位
    private String unit;

    //数量
    private String quantity;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建日期
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //是否删除（0：否1：是）
    @TableLogic
    private String delFlag;

    //合同编号
    private String contractCode;

    //数据来源（0：纸质发票，1：电子发票，2：MQ）
    private String sourceFrom;

    //科目名称
    private String accountName;

    //是否自动生成（0：否，1：是）
    private String isAutoGeneration;

    //系统大类对应系统表字段：kaipxmm
    private String mainCategory;

    //凭证id
    private Long voucherId;

    //main_category映射大类
    private String mappingCategory;

    //同步系统发票主键id
    private Long systemInvoiceId;

    //销方名称
    private String sellerName;

    //业务来源(1：租赁 2：小微 3:商用车 4：乘用车 5：现代物流 6：运通宝 7:长江联合 8:供应链保理)
    private String businessSource;

    //备注
    private String comments;

    //back含税金额
    private String backTaxAmount;

    //back含税税额
    private String backTaxValue;

    //发票号码
    private String invoiceNumber;

    //生成凭证报错原因
    private String errorMessage;

    //签约主体
    private String orgId;

    //处理状态（0：未认领，1：已认领）
    private String processStatus;

    //手工凭证ID
    private Long manualId;
}
