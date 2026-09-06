package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-12
 * @Description :   OrgCompany查询from对象
 * @Modified :
 */
@ApiModel("OrgCompany查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgCompanyQueryDTO extends BaseQueryDTO{

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
