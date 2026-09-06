package com.utfinancing.financehub.engine.scene.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : 场景凭证分录配置;VO对象
 * @Modified :
 */
@Data
public class SceneVoucherEntryVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

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

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标识(0:未删除,1:已删除)")
    private String delFlag;

}
