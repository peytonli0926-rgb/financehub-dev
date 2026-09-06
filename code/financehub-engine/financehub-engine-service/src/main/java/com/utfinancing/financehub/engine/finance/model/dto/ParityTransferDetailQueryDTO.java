package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description :   ParityTransferDetail查询from对象
 * @Modified :
 */
@ApiModel("ParityTransferDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ParityTransferDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "平价转让id",required = true)
    private Long parityTransferId;


}
