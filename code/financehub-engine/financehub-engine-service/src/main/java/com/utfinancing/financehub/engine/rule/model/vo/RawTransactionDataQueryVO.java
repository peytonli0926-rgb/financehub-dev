package com.utfinancing.financehub.engine.rule.model.vo;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 业务系统原始交易数据实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "eg_raw_transaction_data", autoResultMap = true)
public class RawTransactionDataQueryVO extends Model<RawTransactionDataQueryVO> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //来源系统编码
    private String systemCode;

    //业务交易ID
    private String orderId;

    //消息队列messageId
    private String messageId;

    //业务类型标签
    private String businessCode;

    //签约主体编码
    private String orgId;

    //场景编码
    private String sceneCode;

    //合同编号
    private String contractCode;

    //合同状态
    private String contractStatus;

    //消息内容
    private String messageContent;

    //消息状态
    private String messageStatus;

    //异常信息
    private String errorInfo;

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

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;

    //业务日期
    private LocalDateTime businessDate;
}
