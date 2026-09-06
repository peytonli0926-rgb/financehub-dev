package com.utfinancing.financehub.engine.integration.entity;

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
 * 外部业务系统凭证表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_external_voucher")
public class ExternalVoucherEntity extends Model<ExternalVoucherEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //来源系统
    private String systemCode;

    //公司编码
    private String companyNumber;

    //记账日期
    private String bookedDate;

    //业务日期
    private String bizDate;

    //会计期间-年
    private Integer periodYear;

    //会计期间-编码
    private Integer periodNumber;

    //凭证字（凭证类型）
    private String voucherType;

    //附件数量
    private Integer attaches;

    //参考信息
    private String description;

    //凭证号
    private String voucherNumber;

    //制单人
    private String creator;

    //过账人
    private String poster;

    //审核人
    private String auditor;

    //同步到金蝶状态
    private String easStatus;

    //金蝶返回系统状态标识，成功0000
    private String easFlag;

    //金蝶返回期间年
    private Integer easPeriodYear;

    //金蝶返回期间月
    private Integer easPeriodMonth;

    //金蝶返回凭证类型
    private String easVoucherType;

    //金蝶返回日志
    private String easLog;

    //金蝶生成凭证后的凭证编码
    private String easVoucherNumber;

    //金蝶对应凭证唯一编码
    private String easVoucherId;

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
