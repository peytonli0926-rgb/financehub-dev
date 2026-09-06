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
 * 金蝶中间表凭证信息表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_kingdee_middle_voucher")
public class KingdeeMiddleVoucherEntity extends Model<KingdeeMiddleVoucherEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //签约主体
    private String orgId;

    //合同编码
    private String contractCode;

    //业务日期
    private LocalDateTime businessDate;

    //凭证日期
    private LocalDateTime voucherDate;

    //凭证编号
    private String voucherNumber;

    //easFlag
    private String easFlag;

    //easbzCode
    private String easbzCode;

    //系统编码
    private String systemCode;

    //场景名称
    private String sceneName;

    //科目编码
    private String accountCode;

    //币种
    private String currencyNumber;

    //借方发生额
    private BigDecimal debitAmount;

    //贷方发生额
    private BigDecimal creditAmount;

    //摘要
    private String voucherAbstract;

    //客户编码
    private String clientCode;

    //银行账号
    private String ebankNo;

    //相同凭证分组id
    private Long batchId;

    //(未执行：NOT_EXECUTE，进行中：RUNNING，成功：SUCCESS，失败：FAILED)
    private String messageStatus;

    //报错信息
    private String messageError;

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
