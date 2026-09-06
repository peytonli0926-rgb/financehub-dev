package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description :   ImpairmentProvisionDetail查询from对象
 * @Modified :
 */
@ApiModel("ImpairmentProvisionDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ImpairmentProvisionDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(name = "减值计提id")
    private Long impairmentProvisionId;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "减值类型")
    private String impairmentType;

    @ApiModelProperty(value = "减值计提汇总头创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "DTM+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime impairmentCreateTime;

    @ApiModelProperty(value = "合同编号(核算项目)")
    private String contractCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "五级分类")
    private String fiveClass;

    @ApiModelProperty(value = "三阶段")
    private String threeStep;

    @ApiModelProperty(value = "财务合同状态")
    private List<String> financialContractStatus;

    @ApiModelProperty(value = "合同状态")
    private String contractStatus;

    @ApiModelProperty("导入的excel类型")
    private String excelType;

}
