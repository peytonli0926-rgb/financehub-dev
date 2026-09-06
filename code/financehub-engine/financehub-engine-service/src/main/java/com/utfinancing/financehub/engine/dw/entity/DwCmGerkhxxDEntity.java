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
 * 实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dw_cm_gerkhxx_d")
public class DwCmGerkhxxDEntity extends Model<DwCmGerkhxxDEntity> {

    private static final long serialVersionUID = 1L;

    //客户编号
    @TableId(value = "vc_kehbh", type = IdType.ASSIGN_ID)
    private String vcKehbh;

    //合同编号（乘、商）
    private String vcHetbh;

    //个人名称
    private String vcGermc;

    //英文名称
    private String vcYingwmc;

    //客户类别
    private String vcKehlb;

    //证件类型
    private String vcZhengjlx;

    //证件号码
    private String vcZhengjhm;

    //境内境外
    private String vcJingnjw;

    //性别
    private String vcXingb;

    //手机
    private String vcShouj;

    //住宅电话
    private String vcZhuzdh;

    //传真
    private String vcChuanz;

    //Email
    private String vcEmail;

    //网址
    private String vcWangz;

    //邮编
    private String vcYoub;

    //出生年月
    private String dtChusny;

    //籍贯
    private String vcJig;

    //通讯地址
    private String vcTongxdz;

    //办公地址
    private String vcBangdz;

    //户籍地址
    private String vcHujdz;

    //家庭住址
    private String vcJiatzz;

    //居住状况
    private String vcJuzzk;

    //国家
    private String vcGuoj;

    //省份
    private String vcShengf;

    //城市
    private String vcChengs;

    //区县
    private String vcQux;

    //年收入
    private String decNiansr;

    //个人概况
    private String vcGergk;

    //渠道客户编号
    private String vcQudkhbh;

    //渠道客户名称
    private String vcQudkhmc;

    //渠道来源分类
    private String vcQudlyfl;

    //导入eas状态
    private String vcDaoreaszt;

    //导入eas备注
    private String vcDaoreasbz;

    //性格
    private String vcXingg;

    //毕业学校
    private String vcBiyxx;

    //专业
    private String vcZhuany;

    //学历
    private String vcXuel;

    //学位
    private String vcXuew;

    //爱好
    private String vcAih;

    //学科领域
    private String vcXuekly;

    //学术职务
    private String vcXueszw;

    //社会职务
    private String vcShehzw;

    //管理风格
    private String vcGuanlfg;

    //信息来源
    private String vcXinxly;

    //对恒信态度
    private String vcDuihxtd;

    //只读标识
    private String vcZhidbs;

    //进件来源
    private String vcJinjly;

    //PC补全
    private String vcPcbq;

    //有效标识
    private String vcYouxbs;

    //职称
    private String vcZhic;

    //创建时间
    private String dtChuangjsj;

    //违约概率
    private String vcWeiygl;

    //是否为违约客户
    private String vcShifwykh;

    //是否服务中小微企业
    private String chShiffwzxwqy;

    //是否服务科技型企业
    private String chShiffwkjxqy;

    //是否服务三农
    private String chShiffwsn;

    //是否服务上海本地客户
    private String chShiffwshbdkh;

    private String pt;

    private String vcYewxtid;


}
