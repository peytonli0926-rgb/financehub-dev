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
 * @Description :   DwCmGerkhxxD查询from对象
 * @Modified :
 */
@ApiModel("DwCmGerkhxxD查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class DwCmGerkhxxDQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "客户编号")
    private String vcKehbh;

    @ApiModelProperty(value = "合同编号（乘、商）")
    private String vcHetbh;

    @ApiModelProperty(value = "个人名称")
    private String vcGermc;

    @ApiModelProperty(value = "英文名称")
    private String vcYingwmc;

    @ApiModelProperty(value = "客户类别")
    private String vcKehlb;

    @ApiModelProperty(value = "证件类型")
    private String vcZhengjlx;

    @ApiModelProperty(value = "证件号码")
    private String vcZhengjhm;

    @ApiModelProperty(value = "境内境外")
    private String vcJingnjw;

    @ApiModelProperty(value = "性别")
    private String vcXingb;

    @ApiModelProperty(value = "手机")
    private String vcShouj;

    @ApiModelProperty(value = "住宅电话")
    private String vcZhuzdh;

    @ApiModelProperty(value = "传真")
    private String vcChuanz;

    @ApiModelProperty(value = "Email")
    private String vcEmail;

    @ApiModelProperty(value = "网址")
    private String vcWangz;

    @ApiModelProperty(value = "邮编")
    private String vcYoub;

    @ApiModelProperty(value = "出生年月")
    private String dtChusny;

    @ApiModelProperty(value = "籍贯")
    private String vcJig;

    @ApiModelProperty(value = "通讯地址")
    private String vcTongxdz;

    @ApiModelProperty(value = "办公地址")
    private String vcBangdz;

    @ApiModelProperty(value = "户籍地址")
    private String vcHujdz;

    @ApiModelProperty(value = "家庭住址")
    private String vcJiatzz;

    @ApiModelProperty(value = "居住状况")
    private String vcJuzzk;

    @ApiModelProperty(value = "国家")
    private String vcGuoj;

    @ApiModelProperty(value = "省份")
    private String vcShengf;

    @ApiModelProperty(value = "城市")
    private String vcChengs;

    @ApiModelProperty(value = "区县")
    private String vcQux;

    @ApiModelProperty(value = "年收入")
    private String decNiansr;

    @ApiModelProperty(value = "个人概况")
    private String vcGergk;

    @ApiModelProperty(value = "渠道客户编号")
    private String vcQudkhbh;

    @ApiModelProperty(value = "渠道客户名称")
    private String vcQudkhmc;

    @ApiModelProperty(value = "渠道来源分类")
    private String vcQudlyfl;

    @ApiModelProperty(value = "导入eas状态")
    private String vcDaoreaszt;

    @ApiModelProperty(value = "导入eas备注")
    private String vcDaoreasbz;

    @ApiModelProperty(value = "性格")
    private String vcXingg;

    @ApiModelProperty(value = "毕业学校")
    private String vcBiyxx;

    @ApiModelProperty(value = "专业")
    private String vcZhuany;

    @ApiModelProperty(value = "学历")
    private String vcXuel;

    @ApiModelProperty(value = "学位")
    private String vcXuew;

    @ApiModelProperty(value = "爱好")
    private String vcAih;

    @ApiModelProperty(value = "学科领域")
    private String vcXuekly;

    @ApiModelProperty(value = "学术职务")
    private String vcXueszw;

    @ApiModelProperty(value = "社会职务")
    private String vcShehzw;

    @ApiModelProperty(value = "管理风格")
    private String vcGuanlfg;

    @ApiModelProperty(value = "信息来源")
    private String vcXinxly;

    @ApiModelProperty(value = "对恒信态度")
    private String vcDuihxtd;

    @ApiModelProperty(value = "只读标识")
    private String vcZhidbs;

    @ApiModelProperty(value = "进件来源")
    private String vcJinjly;

    @ApiModelProperty(value = "PC补全")
    private String vcPcbq;

    @ApiModelProperty(value = "有效标识")
    private String vcYouxbs;

    @ApiModelProperty(value = "职称")
    private String vcZhic;

    @ApiModelProperty(value = "创建时间")
    private String dtChuangjsj;

    @ApiModelProperty(value = "违约概率")
    private String vcWeiygl;

    @ApiModelProperty(value = "是否为违约客户")
    private String vcShifwykh;

    @ApiModelProperty(value = "是否服务中小微企业")
    private String chShiffwzxwqy;

    @ApiModelProperty(value = "是否服务科技型企业")
    private String chShiffwkjxqy;

    @ApiModelProperty(value = "是否服务三农")
    private String chShiffwsn;

    @ApiModelProperty(value = "是否服务上海本地客户")
    private String chShiffwshbdkh;

    @ApiModelProperty(value = "")
    private String pt;

    @ApiModelProperty(value = "")
    private String vcYewxtid;
}
