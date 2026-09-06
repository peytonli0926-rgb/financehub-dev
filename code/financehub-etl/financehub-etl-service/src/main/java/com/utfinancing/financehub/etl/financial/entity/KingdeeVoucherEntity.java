package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 金蝶凭证表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_kingdee_voucher")
public class KingdeeVoucherEntity extends Model<KingdeeVoucherEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //金蝶T_GL_VOUCHER表主键ID
    private String easId;

    //凭证编号
    private String voucherCode;

    //会计期间
    private Integer periodCode;

    //凭证类型编码
    private String voucherTypeCode;

    //凭证类型名称
    private String voucherTypeName;

    //业务日期
    private LocalDateTime businessDate;

    //记账日期
    private LocalDateTime voucherDate;

    //本位币借方金额
    private String baseDebitAmount;

    //本位币贷方金额
    private String baseCreditAmount;

    //报告币借方金额
    private String reportDebitAmount;

    //报告币贷方金额
    private String reportCreditAmount;

    //状态
    private String voucherStatus;

    //币种
    private String currencyCode;

    //签约主体编码
    private String orgId;

    //签约主体名称
    private String orgName;

    //凭证摘要
    private String voucherAbstract;

    //凭证说明
    private String voucherDescription;

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
