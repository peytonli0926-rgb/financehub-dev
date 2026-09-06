package com.utfinancing.financehub.etl.financial.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-etl</li>
 * <li>ClassName : com.utfinancing.financehub.etl.financial.model.dto.ContractQueryInfoDTO</li>
 * <li>CreateTime : 2024/01/02 15:01</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "合同详情查询开票认领分页接口")
@Data
public class ContractQueryInfoDTO extends BaseQueryDTO {
    @ApiModelProperty(value = "合同id")
//    @NotNull(message = "合同ID不可以为空")
    private Long id;

    @ApiModelProperty(value = "合同编码")
    @JsonIgnore
    private String contractCode;
}
