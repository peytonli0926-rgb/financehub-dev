package com.utfinancing.financehub.engine.finance.entity;

import java.math.BigDecimal;
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
 * 科目余额表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_account_balance")
public class AccountBalanceEntity extends Model<AccountBalanceEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //业务场景编码
    private String sceneCode;

    //业务场景名称
    private String sceneName;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //机构名称(签约主体)
    private String signCompany;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //合同编码
    private String contractCode;

    //合同名称
    private String contractName;

    //财务日期
    private LocalDateTime financeDate;

    //财务账期(yyyyMM)
    private Integer accountPeriod;

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
    @TableLogic
    private String delFlag;


}
