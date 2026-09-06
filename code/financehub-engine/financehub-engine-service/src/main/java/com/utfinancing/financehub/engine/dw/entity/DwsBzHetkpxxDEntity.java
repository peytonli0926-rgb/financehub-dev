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
 * 数仓-合同发票类型表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dws_bz_hetkpxx_d")
public class DwsBzHetkpxxDEntity extends Model<DwsBzHetkpxxDEntity> {

    private static final long serialVersionUID = 1L;

    //合同编号
    @TableId(value = "vc_hetbh", type = IdType.ASSIGN_ID)
    private String vcHetbh;

    //客户编号
    private String vcKehbh;

    //纳税人种类
    private String vcNasrzl;

    //开票对象
    private String vcKaipdx;

    //是否开票
    private String vcShifkp;

    //是否先开发票
    private String vcShifxkfp;

    //先开发票天数
    private String nuXiankfpts;

    //租金发票类型
    private String vcZujfplx;

    //租金发票本利金是否拆分
    private String vcZujfpbljsfcf;

    //发票为放款先决条件
    private String vcFapwfkxjtj;

    //本金是否开收据
    private String vcBenjsfksj;

    //合同开票主体
    private String vcHetkpzt;

    //税号（增值税发票）
    private String vcShuih;

    //开票地址（增值税发票）
    private String vcKaipdz;

    //开票电话（增值税发票）
    private String vcKaipdh;

    //开票开户行（增值税发票）
    private String vcKaipkhx;

    //开票银行账号（增值税发票）
    private String vcKaipyxzh;

    //暂不开票
    private String vcZanbkp;

    //咨询服务费开票主体
    private String vcZixfwfkpzt;

    //创建时间
    private String dtChuangjsj;

    //税率
    private String vcShuil;

    //咨询服务费开票对象
    private String vcZixfwfkpdx;

    private String pt;

    private String vcYewxtid;


}
