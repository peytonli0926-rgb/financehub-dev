package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 未确认收款凭证记录实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_non_confirm_collection_voucher_record")
public class NonConfirmCollectionVoucherRecordEntity extends Model<NonConfirmCollectionVoucherRecordEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //未确认收款明细表Id
    private Long detailId;

    //做账主体
    private String orgId;

    //借贷方向
    private String crOrDt;

    //科目编码
    private String accountNumber;

    //凭证摘要
    private String voucherComments;

    //金额
    private BigDecimal amount;

    //客户编码
    private String clientCode;

    //合同编码
    private String contractCode;

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

    //是否删除（0:否，1：是）
    @TableLogic
    private String delFlag;

    private String orgName;

    private String accountName;

    //借款合同编号
    private String loansContractCode;

    //银行账号
    private String bankNo;
}
