package com.utfinancing.financehub.engine.finance.entity;

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
 * 网银收付款数据表实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("hthx_offline_online_bank_data")
public class HthxOfflineOnlineBankDataEntity extends Model<HthxOfflineOnlineBankDataEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * @description: ID
     **/
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * @description: 交易类型(必填,收款：collection,付款：payment)
     **/
    private String transactionType;

    /**
     * @description: 业务日期(yyyy-MM-dd HH:mm:ss)
     **/
    private String businessDate;

    /**
     * @description: 操作日期(yyyy-MM-dd HH:mm:ss)
     **/
    private String operationDate;

    /**
     * @description: 业务事件
     **/
    private String businessOperation;

    /**
     * @description: 网银编号
     **/
    private String ebankNumber;

    /**
     * @description: 收款账号（虚拟户）
     **/
    private String collectionAccountsBankNo;

    /**
     * @description: 收款开户行
     **/
    private String collectionAccountsBank;

    /**
     * @description: 对方合同号
     **/
    private String contractCode;

    /**
     * @description: 客户编号
     **/
    private String clientCode;

    /**
     * @description: 客户名称
     **/
    private String clientName;

    /**
     * @description: 网银金额
     **/
    private BigDecimal bankAmount;

    /**
     * @description: 备注
     **/
    private String comment;

    /**
     * @description: 对方客户开户行
     **/
    private String clientAccountsBank;

    /**
     * @description: 对方客户银行账号
     **/
    private String clientAccountsBankNo;

    /**
     * @description: 签约主体
     **/
    private String orgId;

    /**
     * @description: 币种
     **/
    private String currencyType;

    /**
     * @description: 银行交易摘要
     **/
    private String bankSummary;

    /**
     * @description: 收款类型
     **/
    private String collectionType;

    /**
     * @description: 删除标识(0:未删除,1:已删除)
     **/
    @TableLogic
    private String delFlag;

    /**
     * @description: 创建人
     **/
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * @description: 创建时间
     **/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * @description: 更新人
     **/
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * @description: 更新时间
     **/
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
