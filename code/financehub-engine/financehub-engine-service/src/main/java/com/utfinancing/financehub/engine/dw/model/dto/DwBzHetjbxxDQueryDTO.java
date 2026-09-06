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
 * @Description :   DwBzHetjbxxD查询from对象
 * @Modified :
 */
@ApiModel("DwBzHetjbxxD查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class DwBzHetjbxxDQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String vcHetbh;

    @ApiModelProperty(value = "客户编号")
    private String vcKehbh;

    @ApiModelProperty(value = "项目编号")
    private String vcXiangmbh;

    @ApiModelProperty(value = "纳税人种类")
    private String vcNasrzl;

    @ApiModelProperty(value = "客户行业类型")
    private String vcKehhylx;

    @ApiModelProperty(value = "合同类型")
    private String vcHetlx;

    @ApiModelProperty(value = "[合同性质]COA/发票前/发货前")
    private String vcHetxz;

    @ApiModelProperty(value = "起租类型")
    private String vcQizlx;

    @ApiModelProperty(value = "内贸外贸(进口出口内贸外贸)")
    private String vcNeimwm;

    @ApiModelProperty(value = "业务大类")
    private String vcYewdl;

    @ApiModelProperty(value = "业务类型")
    private String vcYewlx;

    @ApiModelProperty(value = "业务子类")
    private String vcYewzl;

    @ApiModelProperty(value = "币种")
    private String vcBiz;

    @ApiModelProperty(value = "合同出单部门")
    private String vcHetcdbm;

    @ApiModelProperty(value = "项目经理")
    private String vcXiangmjl;

    @ApiModelProperty(value = "办事处经理")
    private String vcBscjlid;

    @ApiModelProperty(value = "项目协办")
    private String vcXiangmxb;

    @ApiModelProperty(value = "合同状态")
    private String vcHetzt;

    @ApiModelProperty(value = "合同状态类型")
    private String vcHetztlx;

    @ApiModelProperty(value = "是否核销")
    private String vcShifhx;

    @ApiModelProperty(value = "约定结束日")
    private String dtYuedjsr;

    @ApiModelProperty(value = "实际起租日")
    private String dtShijqzr;

    @ApiModelProperty(value = "会计起租日")
    private String dtKuaijqzr;

    @ApiModelProperty(value = "合同签约日期")
    private String dtHetqyrq;

    @ApiModelProperty(value = "合同实际结束日期")
    private String dtHetsjjsrq;

    @ApiModelProperty(value = "[内部标签]客户大小标签")
    private String vcKehdxbq;

    @ApiModelProperty(value = "[内部标签]行业分类标签")
    private String vcHangyflbq;

    @ApiModelProperty(value = "[内部标签]担保类型标签")
    private String vcDanblxbq;

    @ApiModelProperty(value = "[内部标签]内部标签审核")
    private String vcNeibbqsh;

    @ApiModelProperty(value = "[内部标签]内部租赁类型")
    private String vcNeibzllx;

    @ApiModelProperty(value = "[内部标签]客户上市标记")
    private String vcKehssbj;

    @ApiModelProperty(value = "[内部标签]内部客户性质")
    private String vcNeibkhxz;

    @ApiModelProperty(value = "创建日期")
    private String dtChuangjsj;

    @ApiModelProperty(value = "调息方式")
    private String vcTiaoxfs;

    @ApiModelProperty(value = "设备类型")
    private String vcSheblx;

    @ApiModelProperty(value = "是否出表")
    private String chShifcb;

    @ApiModelProperty(value = "是否拟出表")
    private String chShifncb;

    @ApiModelProperty(value = "撤销时间")
    private String dtChexsj;

    @ApiModelProperty(value = "挂靠商（商用车）")
    private String vcGuaks;

    @ApiModelProperty(value = "业务管理人（乘用车）")
    private String vcYewglr;

    @ApiModelProperty(value = "B端承租人（乘用车）")
    private String vcChengzrB;

    @ApiModelProperty(value = "大区（乘用车）")
    private String vcDaq;

    @ApiModelProperty(value = "区域（乘用车）")
    private String vcQuy;

    @ApiModelProperty(value = "是否保理资产出表")
    private String chShifblzccb;

    @ApiModelProperty(value = "")
    private String pt;

    @ApiModelProperty(value = "")
    private String vcYewxtid;
}
