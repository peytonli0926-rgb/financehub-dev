package com.utfinancing.financehub.engine.finance.entity;

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
 * @author robjiang
 * @since 2024-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dws_bz_hetjyjgfyx_d")
public class DwsBzHetjyjgfyxDEntity extends Model<DwsBzHetjyjgfyxDEntity> {

    private static final long serialVersionUID = 1L;

    //数据抽取日期
    @TableId(value = "pt", type = IdType.ASSIGN_ID)
    private String pt;

    //业务系统编号
    private String vcYewxtid;

    //合同编号
    private String vcHetbh;

    //费用类型
    private String vcFeiylx;

    //费用名称
    private String vcFeiymc;

    //价税合计
    private String decJiashj;

    //金额
    private String decJine;

    //税额
    private String decShuie;

    //税率
    private String decShuil;

    //币种
    private String vcBiz;

    //费用比率
    private String decFeiybl;

    //结算方式
    private String vcJiesfs;

    //收支方向(收款/付款)
    private String vcShouzfx;

    //创建时间
    private String dtChuangjsj;


}
