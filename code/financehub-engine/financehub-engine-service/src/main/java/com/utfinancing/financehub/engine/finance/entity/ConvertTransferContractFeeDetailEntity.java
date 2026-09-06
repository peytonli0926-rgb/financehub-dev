package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("eg_convert_transfer_contract_fee_detail")
public class ConvertTransferContractFeeDetailEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 合同转让费用汇总id
     */
    private Long transferId;

    /**
     * 业务日期
     */
    private LocalDate uploadDate;

    /**
     * 记账日期
     */
    private LocalDate accountDate;

    /**
     * 签约主体
     */
    private String orgId;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 费用类型
     */
    private String transferFeeType;

    /**
     * 金额
     */
    private BigDecimal transferFee;

    /**
     * 成本中心
     */
    private String costCenter;

    private String voucherId;

    /**
     * 是否删除（0-否，1-是）
     */
    @TableLogic
    private String delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
