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
 * 金蝶科目余额表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_kingdee_contract_balance")
public class KingdeeContractBalanceEntity extends Model<KingdeeContractBalanceEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //会计期间
    private Integer periodCode;

    //签约主体
    private String orgId;

    //合同编码
    private String contractCode;

    //客户编码
    private String clientCode;

    //借款合同编号
    private String billContractCode;

    //科目编码
    private String accountCode;

    //科目名称
    private String accountName;

    //科目余额
    private String accountBalance;

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
