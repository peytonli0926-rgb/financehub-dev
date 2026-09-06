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
 * @Description :   DwsBzHetkpxxD查询from对象
 * @Modified :
 */
@ApiModel("DwsBzHetkpxxD查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class DwsBzHetkpxxDQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String vcHetbh;

    @ApiModelProperty(value = "客户编号")
    private String vcKehbh;

    @ApiModelProperty(value = "纳税人种类")
    private String vcNasrzl;

    @ApiModelProperty(value = "开票对象")
    private String vcKaipdx;

    @ApiModelProperty(value = "是否开票")
    private String vcShifkp;

    @ApiModelProperty(value = "是否先开发票")
    private String vcShifxkfp;

    @ApiModelProperty(value = "先开发票天数")
    private String nuXiankfpts;

    @ApiModelProperty(value = "租金发票类型")
    private String vcZujfplx;

    @ApiModelProperty(value = "租金发票本利金是否拆分")
    private String vcZujfpbljsfcf;

    @ApiModelProperty(value = "发票为放款先决条件")
    private String vcFapwfkxjtj;

    @ApiModelProperty(value = "本金是否开收据")
    private String vcBenjsfksj;

    @ApiModelProperty(value = "合同开票主体")
    private String vcHetkpzt;

    @ApiModelProperty(value = "税号（增值税发票）")
    private String vcShuih;

    @ApiModelProperty(value = "开票地址（增值税发票）")
    private String vcKaipdz;

    @ApiModelProperty(value = "开票电话（增值税发票）")
    private String vcKaipdh;

    @ApiModelProperty(value = "开票开户行（增值税发票）")
    private String vcKaipkhx;

    @ApiModelProperty(value = "开票银行账号（增值税发票）")
    private String vcKaipyxzh;

    @ApiModelProperty(value = "暂不开票")
    private String vcZanbkp;

    @ApiModelProperty(value = "咨询服务费开票主体")
    private String vcZixfwfkpzt;

    @ApiModelProperty(value = "创建时间")
    private String dtChuangjsj;

    @ApiModelProperty(value = "税率")
    private String vcShuil;

    @ApiModelProperty(value = "咨询服务费开票对象")
    private String vcZixfwfkpdx;

    @ApiModelProperty(value = "")
    private String pt;

    @ApiModelProperty(value = "")
    private String vcYewxtid;
}
