package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2024-05-08
 * @Description : VO对象
 * @Modified :
 */
@Data
public class DwsBzHetjyjgfyxDVO implements Serializable{
    private static final long serialVersionUID = 1L;

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
