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
 * 批量查询上传记录表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_batch_query_upload_record")
public class BatchQueryUploadRecordEntity extends Model<BatchQueryUploadRecordEntity> {

    private static final long serialVersionUID = 1L;

    //id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //上传人
    private String uploadBy;

    //上传时间
    private LocalDateTime uploadTime;

    //合同编号集合，逗号隔开
    private String contractCodeList;

    //科目编码集合，逗号隔开
    private String accountCodeList;

    //删除标志
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
