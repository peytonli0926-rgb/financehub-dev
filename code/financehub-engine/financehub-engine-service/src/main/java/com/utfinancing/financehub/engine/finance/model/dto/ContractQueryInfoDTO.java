package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.model.dto.ContractQueryInfoDTO</li>
 * <li>CreateTime : 2024/01/02 14:28</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "合同详情查询DTO")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ContractQueryInfoDTO extends BaseQueryDTO {

    @ApiModelProperty(value = "合同id")
    @NotNull(message = "合同ID不可以为空")
    private Long id;

    @ApiModelProperty(value = "合同编码")
    @JsonIgnore
    private String contractCode;

    @ApiModelProperty(value = "版本日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date versionDate;
}
