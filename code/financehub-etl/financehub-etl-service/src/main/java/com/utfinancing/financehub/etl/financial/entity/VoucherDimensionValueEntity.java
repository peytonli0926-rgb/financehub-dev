package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证维度和值对应表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-02-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_voucher_dimension_value")
public class VoucherDimensionValueEntity extends Model<VoucherDimensionValueEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //组织机编码
    private String orgId;

    //科目编码
    private String accountCode;

    //维度:用|分隔
    private String dim;

    //维度值:用|分隔
    private String dimValue;

    //来源
    private String sourceSystem;


}
