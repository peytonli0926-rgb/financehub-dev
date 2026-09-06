package com.utfinancing.financehub.engine.scene.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description : 业务配置VO对象
 * @Modified :
 */
@Data
public class BusinessVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "业务编码")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "科目余额表编码")
    private String accountBalanceCode;

    @ApiModelProperty(value = "关联的场景总数")
    private Integer sceneCount;

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
