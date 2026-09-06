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
 * @Date : Create in 2024-04-11
 * @Description :   LongReceivableRegister查询from对象
 * @Modified :
 */
@ApiModel("LongReceivableRegister查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class LongReceivableRegisterQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "长期应收款编号")
    private String longReceivableNumber;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "是否仅展示最新版本(0否1是)")
    private String isLatestVersion;
}
