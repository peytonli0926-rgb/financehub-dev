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
 * 科目余额与明细余额对账结果实时表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_account_detail_result")
public class CheckAccountDetailResultEntity extends Model<CheckAccountDetailResultEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //对账记录ID
    private Long recordId;

    //会计期间
    private Integer periodCode;

    //币种
    private String currencyType;

    //签约主体
    private String orgId;

    //科目代码
    private String accountCode;

    //合同编号
    private String contractCode;

    //客户编号
    private String clientCode;

    //科目余额
    private BigDecimal accountRemainBalance;

    //明细余额
    private BigDecimal detailRemainBalance;

    //差异类型：0无差异，1有差异
    private String diffFlag;

    //差异金额：科目余额-明细余额
    private BigDecimal diffBalance;

    //删除标记
    @TableLogic
    private String delFlag;

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


}
