package com.utfinancing.financehub.engine.finance.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务费计划表实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceFeeAllocationExcelVo extends Model<ServiceFeeAllocationExcelVo> {

    private static final long serialVersionUID = 1L;
    private Date planDate;
    //合同编号
    private String contractCode;

    //签约主体
    private String orgId;

    //服务费协议编号
    private String serviceFeeNo;

    //服务费签约主体
    private String serviceOrgId;

    //实收服务费比例（实收服务费/设备金额）
    private BigDecimal actualReceiveRatio;
    //承租人
    private String lesseeName;
    //会计起租日
    private String leaseDateStart;
    //结束日
    private String leaseDateEnd;
    //计划分摊金额
    private BigDecimal planApportionAmount;

    //应分摊的服务费收入（税后）
    private BigDecimal planApportionNoTax;

    //服务费实收（税后）
    private BigDecimal actualReceive;

    //服务费实收（税后）
    private BigDecimal actualReceiveNoTax;

    //实际计提金额
    private BigDecimal actualAccruedAmount;

    //合同设备金额
    private BigDecimal payableDeviceAmount;
    //分摊比例
    private BigDecimal allocationRatio;
    private BigDecimal planAmount;
    private BigDecimal planAmountTaxInclude;
    //实际计提金额
    private List<ServiceFeeMonthlyData> monthlyDatas;
    private BigDecimal serviceFeeRatio;

}
