package com.utfinancing.financehub.engine.dw.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :   DwDictData查询from对象
 * @Modified :
 */
@ApiModel("DwDictData查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class DwDictDataQueryDTO extends BaseQueryDTO{

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
