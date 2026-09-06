package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同接口表累计金额实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_interface_total")
public class ContractInterfaceTotalEntity extends Model<ContractInterfaceTotalEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编码
    private String contractCode;

    //签约主体
    private String orgId;

    //来源系统
    private String systemCode;

    //合同状态
    private String contractStatus;

    //回收本金累计金额
    private BigDecimal recyclePrincipalAmount;

    //回收利息累计金额
    private BigDecimal recycleInterestAmount;

    //回收罚息累计金额
    private BigDecimal recycleDefaultInterestAmount;

    //收取首付款累计金额
    private BigDecimal receiveFirstAmount;

    //收取手续费累计金额
    private BigDecimal receiveProcedureAmount;

    //收取保险费累计金额
    private BigDecimal receiveInsuranceAmount;

    //收取服务费累计金额
    private BigDecimal receiveServiceAmount;

    //收取履约保证金累计金额
    private BigDecimal receiveMarginAmount;

    //收取留购价累计金额
    private BigDecimal receiveRetainedPrice;

    //收取其他收入累计金额
    private BigDecimal receiveOtherRevenues;

    //收取厂商返利累计金额
    private BigDecimal receiveFirmRebate;

    //收取合同解约及更改手续费累计金额
    private BigDecimal receiveTerminateProcedureAmount;

    //收取违约金累计金额
    private BigDecimal receivePenal;

    //收到GPS累计金额
    private BigDecimal receiveGPS;

    //收到保险费差额累计金额
    private BigDecimal receiveInsuranceDifferAmount;

    //收到收车款累计金额
    private BigDecimal receiveRecycleCarAmount;

    //是否可用1:可用 0:不可用
    private String enableFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
    @TableLogic
    private String delFlag;


}
