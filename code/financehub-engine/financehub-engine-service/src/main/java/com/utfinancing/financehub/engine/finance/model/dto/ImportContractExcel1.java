package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 导入对象
 * @Modified :
 */
@Data
public class ImportContractExcel1 implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "公司", type = Excel.Type.IMPORT)
    private String orgId;
    @Excel(name = "合同", type = Excel.Type.IMPORT)
    private String contractCode;
    @Excel(name = "承租人", type = Excel.Type.IMPORT)
    private String clientName;
    @Excel(name = "合同状态", type = Excel.Type.IMPORT)
    private String contractStatus;
    @Excel(name = "回收融资租赁资产成本余额receive_cost", type = Excel.Type.IMPORT)
    private BigDecimal receiveCostBalance;
    @Excel(name = "回收融资租赁资产减值准备余额equipment_depreciation_reserves", type = Excel.Type.IMPORT)
    private BigDecimal equipmentDepreciationReservesBalance;



}
