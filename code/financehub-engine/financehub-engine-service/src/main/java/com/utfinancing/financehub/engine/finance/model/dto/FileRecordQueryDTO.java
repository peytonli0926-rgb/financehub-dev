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
 * @Author : jnc
 * @Date : Create in 2024-04-29
 * @Description :   FileRecord查询from对象
 * @Modified :
 */
@ApiModel("FileRecord查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class FileRecordQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "业务场景")
    private String businessScene;

    @ApiModelProperty(value = "文件地址")
    private String fileLocation;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "执行状态")
    private List<String> executeStatus;

    @ApiModelProperty(value = "文件上传完成时间")
    private LocalDateTime fileUploadTime;

    @ApiModelProperty(value = "文件上传人")
    private String fileUploadBy;
}
