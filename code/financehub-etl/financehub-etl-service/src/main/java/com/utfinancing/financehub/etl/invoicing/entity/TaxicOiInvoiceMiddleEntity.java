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
@TableName("TAXIC_OI_INVOICE_MIDDLE")
public class TaxicOiInvoiceMiddleEntity extends Model<TaxicOiInvoiceMiddleEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    private String beiz;

    private String cankzd1;

    private String cankzd10;

    private String cankzd2;

    private String cankzd3;

    private String cankzd4;

    private String cankzd5;

    private String cankzd6;

    private String cankzd7;

    private String cankzd8;

    private String cankzd9;

    private LocalDateTime caozrq;

    private LocalDateTime chuangjrq;

    private String contractno;

    private String contractzt;

    private BigDecimal danj;

    private String danjh;

    private LocalDateTime danjrq;

    private String danw;

    private Long duqzt;

    private String faplx;

    private String fugr;

    private String goufdzdh;

    private String goufmc;

    private String goufnsrsbh;

    private String goufyhzh;

    private String guigxh;

    private BigDecimal jiashj;

    private Long jijfs;

    private BigDecimal jine;

    private String kehhy;

    private String pich;

    private String qishu;

    private String qiybh;

    private String quanjnjstate;

    private String shangpbh;

    private String shangpmc;

    private LocalDateTime shisrq;

    private BigDecimal shuie;

    private String shuil;

    private String shuisflbm;

    private String shuisflbmbbh;

    private String shul;

    private String yewlx;

    private String yuxkaipstate;

    private String zbkpstate;

    private String yewczr;

    //备注2
    private String beiz2;


}
