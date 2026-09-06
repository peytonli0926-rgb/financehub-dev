package com.utfinancing.financehub.engine.dw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数仓-法人客户信息实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dw_cm_farkhxx_d")
public class DwCmFarkhxxDEntity extends Model<DwCmFarkhxxDEntity> {

    private static final long serialVersionUID = 1L;

    //客户编号
    @TableId(value = "vc_kehbh", type = IdType.ASSIGN_ID)
    private String vcKehbh;

    //客户名称
    private String vcKehmc;

    //英文名称
    private String vcYingwmc;

    //客户类别
    private String vcKehlb;

    //客户等级
    private String vcKehdj;

    //机构信用代码
    private String vcJigxydm;

    //贷款证号码
    private String vcDaikzhm;

    //登记注册号类型
    private String vcDengjzchlx;

    //登记注册号
    private String vcDengjzch;

    //组织机构代码
    private String vcZuzjgdm;

    //注册地址
    private String vcZhucdz;

    //成立日期
    private String dtChenglrq;

    //营业执照到期日期
    private String dtYingyzzdqrq;

    //企业性质
    private String vcQiyxz;

    //上市公司标志
    private String vcShangsgsbz;

    //进出口权标志
    private String vcJinckqbz;

    //注册资金
    private String decZhuczj;

    //注册资金币种
    private String vcZhuczjbz;

    //国家
    private String vcGuoj;

    //省份
    private String vcShengf;

    //城市
    private String vcChengs;

    //区县
    private String vcQux;

    //手机
    private String vcShouj;

    //电话
    private String vcDianh;

    //传真
    private String vcChuanz;

    //网址
    private String vcWangz;

    //邮编
    private String vcYoub;

    //通讯地址
    private String vcTongxdz;

    //[经营地址]
    private String vcJingydz;

    //行业类型
    private String vcXingylx;

    //客户所属行业门类
    private String vcXingyml;

    //客户所属行业大类
    private String vcXingydl;

    //客户所属行业中类
    private String vcXingyzl;

    //客户所属行业小类
    private String vcXingyxl;

    //承租人概况
    private String vcChengzrgk;

    //备注
    private String vcBeiz;

    //渠道客户编号
    private String vcQudkhbh;

    //渠道来源分类
    private String vcQudlyfl;

    //报送人行征信
    private String vcBaosrxzx;

    //是否已报送人行
    private String vcShifybsrx;

    //股票号码
    private String vcGuphm;

    //境内境外(0:境内/1:境外)
    private String vcJingnjw;

    //实际控制人从业经验(年)
    private String nuShijkzrcyjy;

    //实际运营日期
    private String dtShijyyrq;

    //是否集团客户
    private String chJitkh;

    //是否集团总部客户
    private String chJitzbkh;

    //是否核心客户
    private String chVip;

    //是否事业单位
    private String chShifsydw;

    //有房产证影印本
    private String chYoufczyyb;

    //工商年检到期日
    private String dtGongsnjdqr;

    //组织机构类别
    private String vcZuzjglb;

    //组织机构类别细分
    private String vcZuzjglbxf;

    //经济类型
    private String vcJingjlx;

    //基本户状态
    private String vcJibhzt;

    //机构状态
    private String vcJigzt;

    //主要关系领域
    private String vcZhuygxly;

    //客户类型
    private String vcKehlx;

    //总对总客户
    private String vcZongdzkh;

    //国税登记号
    private String vcGuosdjh;

    //地税登记号
    private String vcDisdjh;

    //企业规模（定级）
    private String vcQiygm;

    //经营方式
    private String vcJingyfs;

    //经营范围(主营)
    private String vcZhuyfw;

    //经营范围(兼营)
    private String vcJianyfw;

    //客户拥有者
    private String vcKehyyz;

    //[内部标签]是否属于上市公司合并报表范围内
    private String chShangsgshbbbfw;

    //[内部标签]隶属上市公司名称
    private String vcLisssgsmc;

    //[内部标签]隶属上市公司代码
    private String vcLisssgsdm;

    //[内部标签]关联企业是否存在上市公司
    private String chGuanlqyssgs;

    //[内部标签]项目上市标记
    private String chXiangmssbj;

    //[内部标签]内部客户性质
    private String vcNeibkhxz;

    //[内部标签]平台类型
    private String vcPingtlx;

    //[内部标签]客户上市标记
    private String vcKehssbj;

    //[内部标签]客户上市代码
    private String vcKehssdm;

    //[内部标签]客户大小标签
    private String vcKehdxbq;

    //最近一期营业收入
    private String decZuijyqyysr;

    //渠道类别
    private String vcQudlb;

    //是否已备案
    private String chShifyba;

    //财报类型
    private String vcCaiblx;

    //有效标识
    private String chYouxbs;

    //创建时间
    private String dtChuangjsj;

    //交易场所
    private String vcJiaoycs;

    //彭博终端证券编码
    private String vcPengbzdzqbm;

    //国际证券识别编码（ISIN码）
    private String vcIsinm;

    //SEDOL代码
    private String vcSedoldm;

    //主营业务所在国家
    private String vcZhuyywszgj;

    //是否发债
    private String vcShiffz;

    //客户当前信用评级
    private String vcKehdqxypj;

    //最近一次信用评级更新日期
    private String dtZuijycxypjgxrq;

    //实控人类别
    private String vcShikrlb;

    //实控人名称
    private String vcShikrmc;

    //实控人的内部CUSTOMER_ID
    private String vcShikrnbbh;

    //实控人证照类别
    private String vcShikrzjlb;

    //实控人证照编号
    private String vcShikrzjbh;

    //违约概率
    private String vcWeiygl;

    //是否为违约客户
    private String vcShifwykh;

    //客户简称
    private String vcKehjc;

    //英文简称
    private String vcYingwjc;

    //是否服务中小微企业
    private String chShiffwzxwqy;

    //是否服务科技型企业
    private String chShiffwkjxqy;

    //是否服务三农
    private String chShiffwsn;

    //是否服务上海本地客户
    private String chShiffwshbdkh;

    //违约损失率LGD(客户最新项目LGD)
    private String vcWeiyssl;

    //五级分类(客户最新项目五级分类)
    private String vcWujfl;

    //总部评级(客户最新项目总部评级)
    private String vcZongbpj;

    //承租人归属
    private String vcChengzrgs;

    private String pt;

    private String vcYewxtid;


}
