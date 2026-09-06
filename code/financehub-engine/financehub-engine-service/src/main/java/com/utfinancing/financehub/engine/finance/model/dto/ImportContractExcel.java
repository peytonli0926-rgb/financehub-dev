package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 导入对象
 * @Modified :
 */
@Data
public class ImportContractExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "公司编号", type = Excel.Type.IMPORT)
    private String orgId;

    @Excel(name = "合同号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "租赁类型", type = Excel.Type.IMPORT)
    private String leaseType;

    @Excel(name = "客户编号", type = Excel.Type.IMPORT)
    private String clientCode;
    @Excel(name = "客户名称", type = Excel.Type.IMPORT)
    private String clientName;
    @Excel(name = "合同状态", type = Excel.Type.IMPORT)
    private String contractStatus;
    @Excel(name = "实际起租日", type = Excel.Type.IMPORT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateStart;
    @Excel(name = "合同约定到期日", type = Excel.Type.IMPORT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaseDateEnd;
    @Excel(name = "行业标签", type = Excel.Type.IMPORT)
    private String industry;
    @Excel(name = "省", type = Excel.Type.IMPORT)
    private String provinceCity;
    @Excel(name = "财务合同状态", type = Excel.Type.IMPORT)
    private String financialContractStatus;
    @Excel(name = "财务合同状态更新时间", type = Excel.Type.IMPORT)
    private Date financialContractStatusUpdateTime;

    @Excel(name = "科目代码", type = Excel.Type.IMPORT)
    private String accountCode;



}
