package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_non_confirm_collection_file_upload_record")
public class NonConfirmCollectionFileUploadRecordEntity extends Model<NonConfirmCollectionFileUploadRecordEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //上传事件
    private LocalDateTime uploadTime;

    //上传人
    private String uploader;

    //上传文件名
    private String fileName;

    //上传记录总数
    private Integer totalRecords;

    //错误文件URL
    private String errorFileUrl;

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

    //是否删除（0:否，1：是）
    private String delFlag;

    // 上传的数据按照认领主体分组(分组id)
    private String orgBatchIds;

}
