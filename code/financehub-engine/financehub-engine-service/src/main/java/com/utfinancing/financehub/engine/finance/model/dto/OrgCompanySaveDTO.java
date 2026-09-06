package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-12
 * @Description : 签约主体表DTO对象
 * @Modified :
 */
@Data
public class OrgCompanySaveDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "金蝶主键FID")
    private String easFid;

    @ApiModelProperty(value = "组织机构编码")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "长编码")
    private String orgLongId;

    @ApiModelProperty(value = "长名称")
    private String orgLongName;

}
