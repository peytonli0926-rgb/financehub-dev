package com.utfinancing.financehub.engine.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.model.dto.CommonApproveDTO</li>
 * <li>CreateTime : 2024/01/09 14:59</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
public class CommonApproveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("单据id")
    private Long documentId;

    @ApiModelProperty("单据类型")
    private String documentType;

    @ApiModelProperty("单据状态")
    private String documentStatus;

    @ApiModelProperty("提交人工号")
    private String submitterNum;

    @ApiModelProperty("提交人姓名")
    private String submitterName;

    @ApiModelProperty("审批人工号")
    private String approverNum;

    @ApiModelProperty("审批人姓名")
    private String approverName;

    @ApiModelProperty("审批备注")
    private String remark;
}
