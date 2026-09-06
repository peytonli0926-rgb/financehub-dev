package com.utfinancing.financehub.admin.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description :   SysInternalSubUser查询from对象
 * @Modified :
 */
@ApiModel("SysInternalSubUser查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysInternalSubUserQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "上级内部用户id")
    private Long parentUserId;

    @ApiModelProperty(value = "下级用户Id")
    private Long subUserId;

    @ApiModelProperty(value = "下级用户名称")
    private String subUserName;
}
