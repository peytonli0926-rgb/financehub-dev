package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-02-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_voucher_to_eas_result")
public class VoucherToEasResultEntity extends Model<VoucherToEasResultEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //返回结果json
    private String backResult;

    //凭证号
    private String voucherNumber;

    //收到消息时间
    private LocalDateTime receiveTime;

    // 是否成功标识
    private String successFlag;

    private String batchUuid;

}
