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
 * 文件记录表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_file_record")
public class FileRecordEntity extends Model<FileRecordEntity> {

    private static final long serialVersionUID = 1L;

    //id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //模块名称
    private String moduleName;

    //业务场景
    private String businessScene;

    //文件地址
    private String fileLocation;

    //文件名称
    private String fileName;

    //执行状态
    private String executeStatus;

    //文件上传完成时间
    private LocalDateTime fileUploadTime;

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

    //文件上传人
    private String fileUploadBy;


}
