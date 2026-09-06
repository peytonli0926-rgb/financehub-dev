package com.utfinancing.financehub.etl.middle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * EAS凭证头实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "EAS_VOUCHER_HEAD", schema = "FINANCINGSYS")
public class EasVoucherHeadEntity extends Model<EasVoucherHeadEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "ID_", type = IdType.ASSIGN_ID)
    private String id;

    //系统名称
    @TableField("SYSTEM_")
    private String system;

    //优先级(5，30）
    @TableField("PRIORITY")
    private BigDecimal priority;

    //模块名称
    @TableField("MODELNAME")
    private String modelname;

    //财务组织
    @TableField("ORGNUMBER")
    private String orgnumber;

    //业务流程编号（单据ID（业务系统唯一码）
    @TableField("BZPROCID")
    private String bzprocid;

    //凭证号（另定义存储过程来生成该号码）
    @TableField("VOUCHERNUMBER")
    private String vouchernumber;

    //业务日期
    @TableField("BZDATE")
    private LocalDateTime bzdate;

    //记账日期
    @TableField("FINANCIALDATE")
    private LocalDateTime financialdate;

    //期间年
    @TableField("PERIODYEAR")
    private String periodyear;

    //期间月
    @TableField("PERIODMONTH")
    private String periodmonth;

    //凭证类型（凭证字）
    @TableField("VOUCHERTYPE")
    private String vouchertype;

    //合同号
    @TableField("CONTRACTID")
    private String contractid;

    //单据状态(0未完整，1已完整)
    @TableField("BILLSTATUS")
    private String billstatus;

    //状态（1新增、2修改、3作废、9删除）
    @TableField("STATUS")
    private String status;

    //备注
    @TableField("MEMO")
    private String memo;

    //创建人
    @TableField("CREATOR")
    private String creator;

    //创建时间
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    //更新人
    @TableField("MODIFICATOR")
    private String modificator;

    //更新时间
    @TableField("MODIFY_DATE")
    private LocalDateTime modifyDate;

    //EAS标记（0待处理，1成功、2失败）
    @TableField("EASFLAG")
    private String easflag;

    //EAS操作信息
    @TableField("EASREASON")
    private String easreason;

    //EAS操作时间
    @TableField("EASOPTIME")
    private LocalDateTime easoptime;

    //EAS业务编码
    @TableField("EASBZCODE")
    private String easbzcode;

    //业务标记（0待处理，1成功、2失败）
    @TableField("BZFLAG")
    private String bzflag;

    //业务操作信息
    @TableField("BZREASON")
    private String bzreason;

    //业务操作时间
    @TableField("BZOPTIME")
    private LocalDateTime bzoptime;

    @TableField("DESCRIPTION")
    private String description;

    //EAS审核后凭证编码
    @TableField("EAS_VOUCHERNUMBER")
    private String easVouchernumber;

    //互联魔方报销单ID
    @TableField("BX_BILLID")
    private String bxBillid;

    //互联魔方报销单页面ID
    @TableField("BX_MAINID")
    private String bxMainid;


}
