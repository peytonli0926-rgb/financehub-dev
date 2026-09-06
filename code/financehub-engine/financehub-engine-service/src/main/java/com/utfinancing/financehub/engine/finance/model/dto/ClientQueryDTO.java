package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description :   Client查询from对象
 * @Modified :
 */
@ApiModel("Client查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "省市")
    private String provinceCity;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "客户类型")
    private String clientType;

    @ApiModelProperty(value = "客户属性")
    private String clientAttribute;

    @ApiModelProperty(value = "客户编号,名称")
    private String clientCodeOrName;

    @ApiModelProperty(value = "客户编号,名称")
    private String searchKey;
}
