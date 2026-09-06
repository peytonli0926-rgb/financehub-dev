package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceCheckQueryDTO</li>
 * <li>CreateTime : 2024/04/01 09:25</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Data
@ApiModel("余额表checkDTO")
public class ContractBalanceCheckQueryDTO extends BaseQueryDTO {

    @NotNull(message = "合同编码不可以为空")
    @ApiModelProperty(value = "合同编码集合")
    private List<String> contractCodeList;

    @ApiModelProperty(value = "签约主体集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "场景编码-临时表")
    private String sceneCodeForTemp;

    @ApiModelProperty(value = "客户编码集合")
    private List<String> clientCodeList;
}
