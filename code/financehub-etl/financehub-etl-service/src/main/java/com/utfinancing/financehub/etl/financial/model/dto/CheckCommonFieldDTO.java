package com.utfinancing.financehub.etl.financial.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-30
 * @Description : 对账对接其他系统查询sql里的字段顺序DTO对象
 * @Modified :
 */
@Data
public class CheckCommonFieldDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "对接系统db")
    private String dbCode;

    @ApiModelProperty(value = "业务场景")
    private String businessType;

    @ApiModelProperty(value = "查询字段")
    private String commonField;

    @ApiModelProperty(value = "字段类型 1 连接字段， 2 查询字段")
    private String fieldType;

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

    @ApiModelProperty(value = "sql里查询的字段顺序")
    private Integer fieldOrder;

}
