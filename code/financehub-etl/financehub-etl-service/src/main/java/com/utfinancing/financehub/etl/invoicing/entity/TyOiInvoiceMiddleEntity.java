package com.utfinancing.financehub.etl.invoicing.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
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
 * @author bruyang
 * @since 2023-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TY_OI_INVOICE_MIDDLE")
public class TyOiInvoiceMiddleEntity extends Model<TyOiInvoiceMiddleEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    private String danjh;

    //票据类型(收据:222;专票:004;普票:007;数电专票:008;数电普票:009)
    private String faplx;

    private String contractno;

    private String fugr;

    private String kehhy;

    private String contractzt;

    private String yuxkaipstate;

    private String quanjnjstate;

    private String zbkpstate;

    private String goufnsrsbh;

    private String goufmc;

    private String goufdz;

    private String goufdh;

    private String goufyhmc;

    private String goufyhzh;

    private LocalDateTime shisrq;

    private String qishu;

    private String shangpbh;

    private String shangpmc;

    private String danw;

    private String shuil;

    private BigDecimal jiashj;

    private String shul;

    private BigDecimal jine;

    private BigDecimal shuie;

    private BigDecimal danj;

    private String beiz;

    private String keymemo;

    private String shuisflbm;

    private String pich;

    private LocalDateTime danjrq;

    private String guigxh;

    private Long jijfs;

    private String qiybh;

    private String xfsh;

    private String kpddm;

    private String shuisflbmbbh;

    private String cankzd1;

    private String cankzd2;

    private String cankzd3;

    private String cankzd4;

    private String cankzd5;

    private String cankzd6;

    private String cankzd7;

    private String cankzd8;

    private String cankzd9;

    private String cankzd10;

    private String yewlx;

    private Long duqzt;

    private String duqsbyy;

    private String yewcjr;

    private LocalDateTime yewcjrq;

    private String gengxr;

    private LocalDateTime gengxrq;

    //备注2
    private String beiz2;


}
