package com.utfinancing.financehub.engine.rule.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-16
 * @Description :   RawTransationData查询from对象
 * @Modified :
 */
@ApiModel("RawTransationData查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RawTransactionDataQueryDTO extends BaseQueryDTO{

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

    @ApiModelProperty(value = "消息状态")
    private String messageStatus;

    @ApiModelProperty(value = "异常信息")
    private String errorInfo;
}
