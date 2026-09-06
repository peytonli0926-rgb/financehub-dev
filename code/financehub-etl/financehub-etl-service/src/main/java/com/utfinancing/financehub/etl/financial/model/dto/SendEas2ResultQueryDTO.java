package com.utfinancing.financehub.etl.financial.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-10
 * @Description :   SendEas2Result查询from对象
 * @Modified :
 */
@ApiModel("SendEas2Result查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SendEas2ResultQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "fid")
    private String fid;

    @ApiModelProperty(value = "是否成功（sucs：成功，errs：失败）")
    private String isSuccess;

    @ApiModelProperty(value = "数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB")
    private String systemCode;
}
