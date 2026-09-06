package com.utfinancing.financehub.engine.scene.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :   SceneVoucherEntry查询from对象
 * @Modified :
 */
@ApiModel("SceneVoucherEntry查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class SceneVoucherEntryQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "场景凭证配置ID")
    private Long sceneVoucherId;

    @ApiModelProperty(value = "金额类型;refDict")
    private String fundType;

    @ApiModelProperty(value = "是否银行账号相关(0:否 1是)")
    private String relateBankFlag;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "现金流属性")
    private String cashAttribute;

    @ApiModelProperty(value = "凭证摘要")
    private String voucherSummary;

    @ApiModelProperty(value = "凭证维度-客户(0:否 1是)")
    private String clientFlag;

    @ApiModelProperty(value = "凭证维度-合同(0:否 1是)")
    private String contractFlag;
}
