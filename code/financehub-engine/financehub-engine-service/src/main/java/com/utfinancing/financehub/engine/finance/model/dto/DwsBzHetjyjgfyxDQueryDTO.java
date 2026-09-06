package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : robjiang
 * @Date : Create in 2024-05-08
 * @Description :   DwsBzHetjyjgfyxD查询from对象
 * @Modified :
 */
@ApiModel("DwsBzHetjyjgfyxD查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class DwsBzHetjyjgfyxDQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "数据抽取日期")
    private String pt;

    @ApiModelProperty(value = "业务系统编号")
    private String vcYewxtid;

    @ApiModelProperty(value = "合同编号")
    private String vcHetbh;

    @ApiModelProperty(value = "费用类型")
    private String vcFeiylx;

    @ApiModelProperty(value = "费用名称")
    private String vcFeiymc;

    @ApiModelProperty(value = "价税合计")
    private String decJiashj;

    @ApiModelProperty(value = "金额")
    private String decJine;

    @ApiModelProperty(value = "税额")
    private String decShuie;

    @ApiModelProperty(value = "税率")
    private String decShuil;

    @ApiModelProperty(value = "币种")
    private String vcBiz;

    @ApiModelProperty(value = "费用比率")
    private String decFeiybl;

    @ApiModelProperty(value = "结算方式")
    private String vcJiesfs;

    @ApiModelProperty(value = "收支方向(收款/付款)")
    private String vcShouzfx;

    @ApiModelProperty(value = "创建时间")
    private String dtChuangjsj;
}
