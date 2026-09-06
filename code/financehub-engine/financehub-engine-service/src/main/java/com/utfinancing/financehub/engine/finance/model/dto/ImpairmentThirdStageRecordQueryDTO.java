package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : robjiang
 * @Date : Create in 2025-12-26
 * @Description :   ImpairmentThirdStageRecord查询from对象
 * @Modified :
 */
@ApiModel("ImpairmentThirdStageRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ImpairmentThirdStageRecordQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "上传账期")
    private String uploadPeriods;
}
