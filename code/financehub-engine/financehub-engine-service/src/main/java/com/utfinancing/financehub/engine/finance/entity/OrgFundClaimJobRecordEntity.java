package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/7/22 17:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_fund_claim_job_record")
public class OrgFundClaimJobRecordEntity extends Model<OrgFundClaimJobRecordEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * @description: ID
     **/
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * @description: 开始日期（查询参数）
     **/
    private String startDate;

    /**
     * @description: 结束日期（查询参数）
     **/
    private String endDate;

    /**
     * @description: 最后查询日期
     **/
    private String lastQueryDate;

    /**
     * @description: 删除标识 1：已删除 0：未删除
     **/
    private String delFlag;

    /**
     * @description: 创建人
     **/
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * @description: 创建时间
     **/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * @description: 更新人
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * @description: 更新时间
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
