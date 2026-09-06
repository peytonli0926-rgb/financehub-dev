package com.utfinancing.financehub.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-common</li>
 * <li>ClassName : com.utfinancing.financehub.common.core.dto.DropDownDTO</li>
 * <li>CreateTime : 2024/02/01 13:57</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@ApiModel(value = "下拉框")
@Data
public class DropDownDTO {

    @ApiModelProperty(value = "下拉列表内容")
    private List<String> dataList;

    @ApiModelProperty(value = "起始列")
    private Integer firstRow;

    @ApiModelProperty(value = "终止列")
    private Integer lastRow;



}
