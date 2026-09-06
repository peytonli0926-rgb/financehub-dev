package com.utfinancing.financehub.engine.dw.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : DTO对象
 * @Modified :
 */
@Data
public class DwDictDataDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer nuId;

    @ApiModelProperty(value = "名称")
    private String vcMingc;

    @ApiModelProperty(value = "值")
    private String vcZhi;

    @ApiModelProperty(value = "父节点ID")
    private String vcFujdid;

    @ApiModelProperty(value = "是否有子级")
    private String vcShifyzj;

    @ApiModelProperty(value = "备注")
    private String vcBeiz;

    @ApiModelProperty(value = "有效标识")
    private String chYouxbs;

    @ApiModelProperty(value = "创建人")
    private String vcChuangjr;

    @ApiModelProperty(value = "创建时间")
    private String dtChuangjsj;

    @ApiModelProperty(value = "更新人")
    private String vcGengxr;

    @ApiModelProperty(value = "更新时间")
    private String dtGengxsj;

}
