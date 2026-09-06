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
@TableName("TAXIC_OI_INVOICE_BACK_MIDDLE")
public class TaxicOiInvoiceBackMiddleEntity extends Model<TaxicOiInvoiceBackMiddleEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    private String beiz;

    private String cankzd1;

    private String cankzd2;

    private String cankzd3;

    private String cankzd4;

    private String cankzd5;

    private String caozr;

    private LocalDateTime caozrq;

    private LocalDateTime chuangjrq;

    private String contractno;

    private String danjh;

    private Long duqzt;

    private String fapdm;

    private String fapdzpath;

    private String faphm;

    private String faplx;

    private BigDecimal hansjine;

    private LocalDateTime kaiprq;

    private String kaipxmm;

    private String pich;

    private String qic;

    private BigDecimal shuie;

    private String yewly;

    private String shangpmc;

    //备注2
    private String beiz2;


}
