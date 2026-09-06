package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 乘用车合同级业财模型；回收计划明细仍保存在 eg_repayment_plan。 */
@Data
@TableName("eg_vehicle_business_model")
public class VehicleBusinessModelEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String contractCode;
    private String sourceSystem;
    private String sourceEventId;
    private String eventCode;
    private String orgId;
    private String businessLine;
    private String leaseType;
    private String leaseMethod;
    private String productCode;
    private String productName;
    private String guaranteeFlag;
    private String channelMode;
    private String dealerCode;
    private String dealerName;
    private String customerCode;
    private String customerName;
    private LocalDate contractSignDate;
    private LocalDate leaseStartDate;
    private LocalDate maturityDate;
    private String currency;
    private BigDecimal contractAmount;
    private BigDecimal financeAmount;
    private BigDecimal actualDisbursement;
    private Integer totalTerms;
    private BigDecimal contractRate;
    private BigDecimal xirrRate;
    private BigDecimal residualValue;
    private String repaymentFrequency;
    private String repaymentMethod;
    private String vin;
    private String assetNo;
    private String assetName;
    private String brand;
    private String model;
    private String carType;
    private BigDecimal originalAssetValue;
    private BigDecimal recognizedAssetValue;
    private String mortgageFlag;
    private String mortgageCertificateNo;
    private String contractStatus;
    private String fiveClass;
    private String impairmentStage;
    private Integer overdueDays;
    private String specialStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
