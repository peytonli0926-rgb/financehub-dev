package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 应用模块名称: 应付保险费
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/5/26 19:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_payable_insurance_report_data")
public class PayableInsuranceReportDataEntity {

    /**
     * @description:  ID
     **/
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * @description:  合同编码
     **/
    private String contractCode;

    /**
     * @description:  签约主体
     **/
    private String orgId;

    /**
     * @description:  来源系统
     **/
    private String systemCode;

    /**
     * @description:  实际计划支付保险费
     **/
    private BigDecimal actualPayableInsuaranceAmount;

    /**
     * @description:  应付保险费
     **/
    private BigDecimal payableInsuranceAmount;

    /**
     * @description:  应付保险费余额
     **/
    private BigDecimal payableInsuranceBalance;

    /**
     * @description:  是否可用1:可用 0:不可用
     **/
    private String enableFlag;

    /**
     * @description:  创建人
     **/
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * @description:  创建时间
     **/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * @description:  更新人
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * @description:  更新时间
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * @description:  删除标识(0:未删除,1:已删除)
     **/
    @TableLogic
    private String delFlag;

}
