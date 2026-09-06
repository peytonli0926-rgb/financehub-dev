package com.utfinancing.financehub.etl.financial.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-30
 * @Description : VO对象
 * @Modified :
 */
@Data
public class CheckCommonDataVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "与中台sql关联字段组合成的json字段")
    private String joinField;

    @ApiModelProperty(value = "与中台sql查询的比较字段组合的json字段")
    private String queryField;

    @ApiModelProperty(value = "删除标志")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "对接系统db")
    private String dbCode;

}
