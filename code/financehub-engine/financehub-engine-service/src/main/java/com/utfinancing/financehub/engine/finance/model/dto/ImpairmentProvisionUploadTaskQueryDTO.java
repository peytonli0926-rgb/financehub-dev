package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-28
 * @Description :   ImpairmentProvisionUploadTask查询from对象
 * @Modified :
 */
@ApiModel("ImpairmentProvisionUploadTask查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ImpairmentProvisionUploadTaskQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "导入的excel类型")
    private String excelType;

    @ApiModelProperty(value = "用户名")
    private String userName;


}
