package com.utfinancing.financehub.etl.financial.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : 金蝶科目表DTO对象
 * @Modified :
 */
@Data
public class KingdeeAccountDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

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

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
