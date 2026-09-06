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
@TableName("TAXIC_II_INVOICE_MIDDLE")
public class TaxicIiInvoiceMiddleEntity extends Model<TaxicIiInvoiceMiddleEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    private String cank1;

    private String cank10;

    private String cank2;

    private String cank3;

    private String cank4;

    private String cank5;

    private String cank6;

    private String cank7;

    private String cank8;

    private String cank9;

    private LocalDateTime caozrq;

    private String contractno;

    private Long duqzt;

    private LocalDateTime editimgrq;

    private String fapdm;

    private String faphm;

    private String faplx;

    private String goufnsrsbh;

    private String hejjine;

    private String imagepath;

    private String isdelete;

    private LocalDateTime kaiprq;

    private String message;

    private BigDecimal notaxjine;

    private String qiybh;

    private LocalDateTime scimgrq;

    private String shuie;

    private BigDecimal taxjine;

    private Long yewly;

    private LocalDateTime ywxyeditrq;

    private String xiaofsbh;

    private String faplydzxz;

    //0-Normal 1-OFD
    private String isofd;


}
