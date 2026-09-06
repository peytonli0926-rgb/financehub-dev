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
 * @Date : Create in 2024-06-25
 * @Description :   BatchTask查询from对象
 * @Modified :
 */
@ApiModel("BatchTask查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BatchTaskQueryDTO extends BaseQueryDTO{
}
