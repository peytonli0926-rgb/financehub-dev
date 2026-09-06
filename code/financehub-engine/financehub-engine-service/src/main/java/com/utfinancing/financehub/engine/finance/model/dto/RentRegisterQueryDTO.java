package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-07
 * @Description :   RentRegister查询from对象
 * @Modified :
 */
@ApiModel("RentRegister查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RentRegisterQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "房产租赁合同编号")
    private String contractCode;

    @ApiModelProperty(value = "资产编号")
    private String assetNumber;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "处理状态")
    private List<String> processStatusList;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "是否仅展示最新版本(0否1是)")
    private String isLatestVersion;
}
