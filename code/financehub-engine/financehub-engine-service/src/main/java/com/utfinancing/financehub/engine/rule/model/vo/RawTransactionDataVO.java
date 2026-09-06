package com.utfinancing.financehub.engine.rule.model.vo;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-16
 * @Description : 业务系统原始交易数据VO对象
 * @Modified :
 */
@Data
public class RawTransactionDataVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "来源系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务交易ID")
    private String orderId;

    @ApiModelProperty(value = "消息队列messageId")
    private String messageId;

    @ApiModelProperty(value = "业务类型标签")
    private String businessCode;

    @ApiModelProperty(value = "签约主体编码")
    private String orgId;

    @ApiModelProperty(value = "场景编码")
    private String sceneCode;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty(value = "消息内容")
    private JSONObject messageContent;

    @ApiModelProperty(value = "消息状态")
    private String messageStatus;

    @ApiModelProperty(value = "异常信息")
    private String errorInfo;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
