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
 * 数仓-合同基本信息实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dw_bz_hetjbxx_d")
public class DwBzHetjbxxDEntity extends Model<DwBzHetjbxxDEntity> {

    private static final long serialVersionUID = 1L;

    //合同编号
    @TableId(value = "vc_hetbh", type = IdType.ASSIGN_ID)
    private String vcHetbh;

    //客户编号
    private String vcKehbh;

    //项目编号
    private String vcXiangmbh;

    //纳税人种类
    private String vcNasrzl;

    //客户行业类型
    private String vcKehhylx;

    //合同类型
    private String vcHetlx;

    //[合同性质]COA/发票前/发货前
    private String vcHetxz;

    //起租类型
    private String vcQizlx;

    //内贸外贸(进口出口内贸外贸)
    private String vcNeimwm;

    //业务大类
    private String vcYewdl;

    //业务类型
    private String vcYewlx;

    //业务子类
    private String vcYewzl;

    //币种
    private String vcBiz;

    //合同出单部门
    private String vcHetcdbm;

    //项目经理
    private String vcXiangmjl;

    //办事处经理
    private String vcBscjlid;

    //项目协办
    private String vcXiangmxb;

    //合同状态
    private String vcHetzt;

    //合同状态类型
    private String vcHetztlx;

    //是否核销
    private String vcShifhx;

    //约定结束日
    private String dtYuedjsr;

    //实际起租日
    private String dtShijqzr;

    //会计起租日
    private String dtKuaijqzr;

    //合同签约日期
    private String dtHetqyrq;

    //合同实际结束日期
    private String dtHetsjjsrq;

    //[内部标签]客户大小标签
    private String vcKehdxbq;

    //[内部标签]行业分类标签
    private String vcHangyflbq;

    //[内部标签]担保类型标签
    private String vcDanblxbq;

    //[内部标签]内部标签审核
    private String vcNeibbqsh;

    //[内部标签]内部租赁类型
    private String vcNeibzllx;

    //[内部标签]客户上市标记
    private String vcKehssbj;

    //[内部标签]内部客户性质
    private String vcNeibkhxz;

    //创建日期
    private String dtChuangjsj;

    //调息方式
    private String vcTiaoxfs;

    //设备类型
    private String vcSheblx;

    //是否出表
    private String chShifcb;

    //是否拟出表
    private String chShifncb;

    //撤销时间
    private String dtChexsj;

    //挂靠商（商用车）
    private String vcGuaks;

    //业务管理人（乘用车）
    private String vcYewglr;

    //B端承租人（乘用车）
    private String vcChengzrB;

    //大区（乘用车）
    private String vcDaq;

    //区域（乘用车）
    private String vcQuy;

    //是否保理资产出表
    private String chShifblzccb;

    private String pt;

    private String vcYewxtid;


}
