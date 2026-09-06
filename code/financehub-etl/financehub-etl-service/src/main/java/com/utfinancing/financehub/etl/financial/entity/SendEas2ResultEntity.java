package com.utfinancing.financehub.etl.financial.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 记录发送EAS2数据是否成功表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_send_eas2_result")
public class SendEas2ResultEntity extends Model<SendEas2ResultEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //fid
    private String fid;

    //是否成功（sucs：成功，errs：失败）
    private String isSuccess;

    //数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB
    private String systemCode;

    //返回值Key
    private String resultKey;

    private String batchUuid;

    @ApiModelProperty(value = "数据来源id,多个逗号分隔")
    private String primaryKey;



}
