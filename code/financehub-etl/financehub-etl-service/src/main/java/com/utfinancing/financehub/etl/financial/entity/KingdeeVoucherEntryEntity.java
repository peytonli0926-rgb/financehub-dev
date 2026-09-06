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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 金蝶凭证分录表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_kingdee_voucher_entry")
public class KingdeeVoucherEntryEntity extends Model<KingdeeVoucherEntryEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //金蝶T_GL_VOUCHERENTRY表主键ID
    private String easId;

    //金蝶凭证头ID
    private String voucherEasId;

    //摘要
    private String voucherSummary;

    //签约主体
    private String orgId;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //银行编号
    private String bankNumber;

    //借款合同编号
    private String billContractCode;

    //会计期间
    private Integer periodCode;

    //借贷方向
    private String debitCreditType;

    //借方金额
    private BigDecimal debitAmount;

    //贷方金额
    private BigDecimal creditAmount;

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
    private String delFlag;


}
