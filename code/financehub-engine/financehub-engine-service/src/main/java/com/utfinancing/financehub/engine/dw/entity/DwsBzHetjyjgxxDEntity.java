package com.utfinancing.financehub.engine.dw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dws_bz_hetjyjgxx_d")
public class DwsBzHetjyjgxxDEntity extends Model<DwsBzHetjyjgxxDEntity> {

    private static final long serialVersionUID = 1L;

    //合同编号
    @TableId(value = "vc_hetbh", type = IdType.ASSIGN_ID)
    private String vcHetbh;

    //客户编号
    private String vcKehbh;

    //租赁期限（月）
    private String nuZulqx;

    //还租方式
    private String nuHuanzfs;

    //还租次数
    private String nuHuanzcs;

    //租赁年利率
    private String decZulnll;

    //利率浮动类型
    private String vcLilfdlx;

    //客户平面利率
    private String decKehpmll;

    //期初（期末）支付
    private String vcQicqmzf;

    //结算方式
    private String vcJiesfs;

    //每月偿付日
    private String nuMeiycfr;

    //设备价格（设备金额）
    private String decShebjg;

    //租金概算本金
    private String decZujgsbj;

    //净融资额
    private String decJingrze;

    //租赁销售额
    private String decZulxse;

    //租赁合同总金额
    private String decZulhtzje;

    //起租时租赁销售额
    private String decQizszlxse;

    //起租时系统IRR(含税)
    private String decQizsxtirr;

    //起租时实际IRR(含税)
    private String decQizssjirr;

    //系统IRR(含税)
    private String decXitirr;

    //实际IRR(含税)
    private String decShijirr;

    //系统IRR(税后)
    private String decXitirrNottax;

    //实际IRR(税后)
    private String decShijirrNottax;

    //GP(含税)
    private String decGp;

    //GP(付税后)
    private String decGpNottax;

    //保证金是否抵扣
    private String vcBaozjsfdk;

    //资金成本
    private String decZijcb;

    //创建时间
    private String dtChuangjsj;

    //利差（含税）
    private String decLichs;

    //保险费支付方式
    private String vcBaoxfzffs;

    //罚息利率
    private String decFaxll;

    //罚息计算方式
    private String vcFaxjsfs;

    //厂商贴息(商用车)
    private String decChangstx;

    //起租时租赁年利率
    private String decQizszlnll;

    //保理费收入
    private String decBaolfsr;

    //日期
    private String pt;

    //业务系统
    private String vcYewxtid;


}
