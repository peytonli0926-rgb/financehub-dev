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
@TableName("TY_OI_INVOICE_BACK_MIDDLE")
public class TyOiInvoiceBackMiddleEntity extends Model<TyOiInvoiceBackMiddleEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    private String danjh;

    private String contractno;

    private String fapdm;

    private String faphm;

    //票据类型(收据:222;专票:004;普票:007;数电专票:008;数电普票:009)
    private String faplx;

    private BigDecimal hansjine;

    private BigDecimal jine;

    private BigDecimal shuie;

    private String caozlx;

    private String yuanfpdm;

    private String yuanfphm;

    private LocalDateTime kaiprq;

    private String kaipxmm;

    private String shangpmc;

    private String goufsh;

    private String goufmc;

    private String pich;

    private String qic;

    private String beiz;

    private String fapdzpath;

    private String cankzd1;

    private String cankzd2;

    private String cankzd3;

    private String cankzd4;

    private String cankzd5;

    private String yewly;

    private Long duqzt;

    private String duqsbyy;

    private String chuangjr;

    private LocalDateTime chuangjrq;

    private String gengxr;

    private LocalDateTime gengxrq;

    //xml路径
    private String xmlpath;

    //备注2
    private String beiz2;


}
