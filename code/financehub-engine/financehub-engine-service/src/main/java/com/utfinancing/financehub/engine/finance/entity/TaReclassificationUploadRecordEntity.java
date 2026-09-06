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
 * ta重分类上传文件记录实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_ta_reclassification_upload_record")
public class TaReclassificationUploadRecordEntity extends Model<TaReclassificationUploadRecordEntity> {

    private static final long serialVersionUID = 1L;

    //id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //批次id
    private String batchId;

    //重分类月份
    private LocalDateTime reclassificationMonth;

    //合同编号
    private String contractCode;

    //业务系统批扣流水号
    private String ebankBatchNo;

    //TA重分类金额
    private BigDecimal taReclassificationAmount;

    //TA重分类科目
    private String accountCode;

    //备注
    private String remark;

    //上传人
    private String uploadBy;

    //上传状态
    private String uploadStatus;

    //删除标记
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
