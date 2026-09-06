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
 * 减值计提上传任务实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_impairment_provision_upload_task")
public class ImpairmentProvisionUploadTaskEntity extends Model<ImpairmentProvisionUploadTaskEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //导入的excel类型
    private String excelType;

    //开始时间
    private LocalDateTime startTime;

    //结束时间
    private LocalDateTime endTime;

    //导入文件名称
    private String fileName;

    //状态
    private String status;

    //上传用户id
    private Long userId;

    //用户名
    private String userName;

    //错误信息
    private String errorInfo;

    //是否删除（0-否，1-是）
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

    //任务类型
    private String taskType;

    //任务处理数据总条数
    private Integer dataSize;

    //成功数据条数
    private Integer dataSuccessSize;

    //失败数据条数
    private Integer dataFailedSize;

    //单据id
    private Long docId;

    /**
     * @description: 进入到凭证生成逻辑的数据总条数
     **/
    private Integer dataTotalSize;

    /**
     * @description: 例外记录条数
     **/
    private int dataExceptionSize;

}
