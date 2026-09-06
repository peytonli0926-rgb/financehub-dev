package com.utfinancing.financehub.engine.dw.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : 数仓-法人客户信息DTO对象
 * @Modified :
 */
@Data
public class DwCmFarkhxxDDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户编号")
    private String vcKehbh;

    @ApiModelProperty(value = "客户名称")
    private String vcKehmc;

    @ApiModelProperty(value = "英文名称")
    private String vcYingwmc;

    @ApiModelProperty(value = "客户类别")
    private String vcKehlb;

    @ApiModelProperty(value = "客户等级")
    private String vcKehdj;

    @ApiModelProperty(value = "机构信用代码")
    private String vcJigxydm;

    @ApiModelProperty(value = "贷款证号码")
    private String vcDaikzhm;

    @ApiModelProperty(value = "登记注册号类型")
    private String vcDengjzchlx;

    @ApiModelProperty(value = "登记注册号")
    private String vcDengjzch;

    @ApiModelProperty(value = "组织机构代码")
    private String vcZuzjgdm;

    @ApiModelProperty(value = "注册地址")
    private String vcZhucdz;

    @ApiModelProperty(value = "成立日期")
    private String dtChenglrq;

    @ApiModelProperty(value = "营业执照到期日期")
    private String dtYingyzzdqrq;

    @ApiModelProperty(value = "企业性质")
    private String vcQiyxz;

    @ApiModelProperty(value = "上市公司标志")
    private String vcShangsgsbz;

    @ApiModelProperty(value = "进出口权标志")
    private String vcJinckqbz;

    @ApiModelProperty(value = "注册资金")
    private String decZhuczj;

    @ApiModelProperty(value = "注册资金币种")
    private String vcZhuczjbz;

    @ApiModelProperty(value = "国家")
    private String vcGuoj;

    @ApiModelProperty(value = "省份")
    private String vcShengf;

    @ApiModelProperty(value = "城市")
    private String vcChengs;

    @ApiModelProperty(value = "区县")
    private String vcQux;

    @ApiModelProperty(value = "手机")
    private String vcShouj;

    @ApiModelProperty(value = "电话")
    private String vcDianh;

    @ApiModelProperty(value = "传真")
    private String vcChuanz;

    @ApiModelProperty(value = "网址")
    private String vcWangz;

    @ApiModelProperty(value = "邮编")
    private String vcYoub;

    @ApiModelProperty(value = "通讯地址")
    private String vcTongxdz;

    @ApiModelProperty(value = "[经营地址]")
    private String vcJingydz;

    @ApiModelProperty(value = "行业类型")
    private String vcXingylx;

    @ApiModelProperty(value = "客户所属行业门类")
    private String vcXingyml;

    @ApiModelProperty(value = "客户所属行业大类")
    private String vcXingydl;

    @ApiModelProperty(value = "客户所属行业中类")
    private String vcXingyzl;

    @ApiModelProperty(value = "客户所属行业小类")
    private String vcXingyxl;

    @ApiModelProperty(value = "承租人概况")
    private String vcChengzrgk;

    @ApiModelProperty(value = "备注")
    private String vcBeiz;

    @ApiModelProperty(value = "渠道客户编号")
    private String vcQudkhbh;

    @ApiModelProperty(value = "渠道来源分类")
    private String vcQudlyfl;

    @ApiModelProperty(value = "报送人行征信")
    private String vcBaosrxzx;

    @ApiModelProperty(value = "是否已报送人行")
    private String vcShifybsrx;

    @ApiModelProperty(value = "股票号码")
    private String vcGuphm;

    @ApiModelProperty(value = "境内境外(0:境内/1:境外)")
    private String vcJingnjw;

    @ApiModelProperty(value = "实际控制人从业经验(年)")
    private String nuShijkzrcyjy;

    @ApiModelProperty(value = "实际运营日期")
    private String dtShijyyrq;

    @ApiModelProperty(value = "是否集团客户")
    private String chJitkh;

    @ApiModelProperty(value = "是否集团总部客户")
    private String chJitzbkh;

    @ApiModelProperty(value = "是否核心客户")
    private String chVip;

    @ApiModelProperty(value = "是否事业单位")
    private String chShifsydw;

    @ApiModelProperty(value = "有房产证影印本")
    private String chYoufczyyb;

    @ApiModelProperty(value = "工商年检到期日")
    private String dtGongsnjdqr;

    @ApiModelProperty(value = "组织机构类别")
    private String vcZuzjglb;

    @ApiModelProperty(value = "组织机构类别细分")
    private String vcZuzjglbxf;

    @ApiModelProperty(value = "经济类型")
    private String vcJingjlx;

    @ApiModelProperty(value = "基本户状态")
    private String vcJibhzt;

    @ApiModelProperty(value = "机构状态")
    private String vcJigzt;

    @ApiModelProperty(value = "主要关系领域")
    private String vcZhuygxly;

    @ApiModelProperty(value = "客户类型")
    private String vcKehlx;

    @ApiModelProperty(value = "总对总客户")
    private String vcZongdzkh;

    @ApiModelProperty(value = "国税登记号")
    private String vcGuosdjh;

    @ApiModelProperty(value = "地税登记号")
    private String vcDisdjh;

    @ApiModelProperty(value = "企业规模（定级）")
    private String vcQiygm;

    @ApiModelProperty(value = "经营方式")
    private String vcJingyfs;

    @ApiModelProperty(value = "经营范围(主营)")
    private String vcZhuyfw;

    @ApiModelProperty(value = "经营范围(兼营)")
    private String vcJianyfw;

    @ApiModelProperty(value = "客户拥有者")
    private String vcKehyyz;

    @ApiModelProperty(value = "[内部标签]是否属于上市公司合并报表范围内")
    private String chShangsgshbbbfw;

    @ApiModelProperty(value = "[内部标签]隶属上市公司名称")
    private String vcLisssgsmc;

    @ApiModelProperty(value = "[内部标签]隶属上市公司代码")
    private String vcLisssgsdm;

    @ApiModelProperty(value = "[内部标签]关联企业是否存在上市公司")
    private String chGuanlqyssgs;

    @ApiModelProperty(value = "[内部标签]项目上市标记")
    private String chXiangmssbj;

    @ApiModelProperty(value = "[内部标签]内部客户性质")
    private String vcNeibkhxz;

    @ApiModelProperty(value = "[内部标签]平台类型")
    private String vcPingtlx;

    @ApiModelProperty(value = "[内部标签]客户上市标记")
    private String vcKehssbj;

    @ApiModelProperty(value = "[内部标签]客户上市代码")
    private String vcKehssdm;

    @ApiModelProperty(value = "[内部标签]客户大小标签")
    private String vcKehdxbq;

    @ApiModelProperty(value = "最近一期营业收入")
    private String decZuijyqyysr;

    @ApiModelProperty(value = "渠道类别")
    private String vcQudlb;

    @ApiModelProperty(value = "是否已备案")
    private String chShifyba;

    @ApiModelProperty(value = "财报类型")
    private String vcCaiblx;

    @ApiModelProperty(value = "有效标识")
    private String chYouxbs;

    @ApiModelProperty(value = "创建时间")
    private String dtChuangjsj;

    @ApiModelProperty(value = "交易场所")
    private String vcJiaoycs;

    @ApiModelProperty(value = "彭博终端证券编码")
    private String vcPengbzdzqbm;

    @ApiModelProperty(value = "国际证券识别编码（ISIN码）")
    private String vcIsinm;

    @ApiModelProperty(value = "SEDOL代码")
    private String vcSedoldm;

    @ApiModelProperty(value = "主营业务所在国家")
    private String vcZhuyywszgj;

    @ApiModelProperty(value = "是否发债")
    private String vcShiffz;

    @ApiModelProperty(value = "客户当前信用评级")
    private String vcKehdqxypj;

    @ApiModelProperty(value = "最近一次信用评级更新日期")
    private String dtZuijycxypjgxrq;

    @ApiModelProperty(value = "实控人类别")
    private String vcShikrlb;

    @ApiModelProperty(value = "实控人名称")
    private String vcShikrmc;

    @ApiModelProperty(value = "实控人的内部CUSTOMER_ID")
    private String vcShikrnbbh;

    @ApiModelProperty(value = "实控人证照类别")
    private String vcShikrzjlb;

    @ApiModelProperty(value = "实控人证照编号")
    private String vcShikrzjbh;

    @ApiModelProperty(value = "违约概率")
    private String vcWeiygl;

    @ApiModelProperty(value = "是否为违约客户")
    private String vcShifwykh;

    @ApiModelProperty(value = "客户简称")
    private String vcKehjc;

    @ApiModelProperty(value = "英文简称")
    private String vcYingwjc;

    @ApiModelProperty(value = "是否服务中小微企业")
    private String chShiffwzxwqy;

    @ApiModelProperty(value = "是否服务科技型企业")
    private String chShiffwkjxqy;

    @ApiModelProperty(value = "是否服务三农")
    private String chShiffwsn;

    @ApiModelProperty(value = "是否服务上海本地客户")
    private String chShiffwshbdkh;

    @ApiModelProperty(value = "违约损失率LGD(客户最新项目LGD)")
    private String vcWeiyssl;

    @ApiModelProperty(value = "五级分类(客户最新项目五级分类)")
    private String vcWujfl;

    @ApiModelProperty(value = "总部评级(客户最新项目总部评级)")
    private String vcZongbpj;

    @ApiModelProperty(value = "承租人归属")
    private String vcChengzrgs;

    @ApiModelProperty(value = "")
    private String pt;

    @ApiModelProperty(value = "")
    private String vcYewxtid;

}
