package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :   KingdeeAccount查询from对象
 * @Modified :
 */
@ApiModel("KingdeeAccount查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class KingdeeAccountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "金蝶T_BD_ACCOUNTVIEW主键ID")
    private String easId;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "签约主体编码")
    private String orgId;

    @ApiModelProperty(value = "凭证类型编码")
    private String accountTypeCode;

    @ApiModelProperty(value = "凭证类型名称")
    private String accountTypeName;

    @ApiModelProperty(value = "借贷方向")
    private String drcrType;

    @ApiModelProperty(value = "是否叶子节点 1:是 0:否")
    private String leafFlag;

    @ApiModelProperty(value = "科目层级")
    private Integer accountLevel;

    @ApiModelProperty(value = "科目长名称")
    private String accountFullName;

    @ApiModelProperty(value = "科目长编码")
    private String accountFullCode;
}
