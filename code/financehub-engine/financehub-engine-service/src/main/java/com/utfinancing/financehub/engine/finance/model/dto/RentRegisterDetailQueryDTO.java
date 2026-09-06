package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-08
 * @Description :   RentRegisterDetail查询from对象
 * @Modified :
 */
@ApiModel("RentRegisterDetail查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class RentRegisterDetailQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "出租登记idList")
    private List<Long> rentRegisterIdList;



}
