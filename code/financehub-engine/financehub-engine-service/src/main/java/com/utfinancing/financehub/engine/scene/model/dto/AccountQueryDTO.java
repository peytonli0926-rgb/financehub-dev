package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   Account查询from对象
 * @Modified :
 */
@ApiModel("Account查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "金额类型")
    private String fundType;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "科目编码list")
    private List<String> accountCodeList;

    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目名称")
    private List<String> accountNameList;

    @ApiModelProperty(value = "科目性质")
    private String accountCategory;

    @ApiModelProperty(value = "借贷方向")
    private String debitCreditType;

    @ApiModelProperty(value = "余额方向")
    private String settlementType;

    @ApiModelProperty(value = "凭证维度")
    private List<String> assistFlags;
}
