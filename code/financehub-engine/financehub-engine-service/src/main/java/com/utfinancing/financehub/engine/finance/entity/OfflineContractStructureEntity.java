package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 线下合同交易数据实体对象
 * </p>
 *
 * @author hzhao
 * @since 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_offline_contract_structure")
public class OfflineContractStructureEntity extends Model<OfflineContractStructureEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //主合同编号
    private String contractCodeM;

    //合同编号
    private String contractCode;

    //设备款
    private BigDecimal payableDevice;

    //首付款
    private BigDecimal receivableDownpayment;

    //出租人保险费
    private BigDecimal lessorInsurance;

    //承租人履约保证金
    private BigDecimal lesseeMargin;

    //渠道费用
    private BigDecimal channelFee;

    //手续费收入(含增值税)
    private BigDecimal receivableCommission;

    //出租人其它成本
    private BigDecimal lessorOtherincome;

    //厂商返利
    private BigDecimal receivableRebate;

    //承租人保险费
    private BigDecimal receivableInsurance;

    //期末残值
    private BigDecimal receivableResidualValue;

    //其他收入 (含增值税)
    private BigDecimal receivableOtherincome;

    //咨询服务收入(含增值税)
    private BigDecimal receivableService;

    //供应商履约保证金
    private BigDecimal supplierMargin;

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
