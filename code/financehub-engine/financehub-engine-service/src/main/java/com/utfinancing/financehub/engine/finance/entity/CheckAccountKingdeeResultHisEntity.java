package com.utfinancing.financehub.engine.finance.entity;

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
 * 金蝶科目余额与中台科目余额对账历史表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_account_kingdee_result_his")
public class CheckAccountKingdeeResultHisEntity extends Model<CheckAccountKingdeeResultHisEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //对账记录ID
    private Long recordId;

    //期间
    private Integer periodCode;

    //币种
    private String currencyType;

    //签约主体
    private String orgId;

    //科目代码
    private String accountCode;

    //中台科目余额
    private String accountRemainBalance;

    //金蝶科目余额
    private String kingdeeRemainBalance;

    //差异类型 0 无差异 1 有差异
    private String diffFlag;

    //差异金额 金蝶科目余额-中台科目余额
    private String diffBalance;

    //删除标志
    @TableLogic
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
