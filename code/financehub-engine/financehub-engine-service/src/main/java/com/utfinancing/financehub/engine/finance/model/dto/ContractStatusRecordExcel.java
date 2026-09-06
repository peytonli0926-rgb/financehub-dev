package com.utfinancing.financehub.engine.finance.model.dto;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 合同状态记录表导出
 * @Modified :
 */
@Data
public class ContractStatusRecordExcel implements Serializable{
    private static final long serialVersionUID = 1L;


    @ColumnWidth(20)
    @Excel(name = "合同号")
    private String contractCode;

    @ColumnWidth(50)
    @Excel(name = "客户名称")
    private String clientName;

    @ColumnWidth(50)
    @Excel(name = "签约主体")
    private String orgId;

    @ColumnWidth(20)
    @Excel(name = "财务合同状态更新时间")
    private Date financialContractStatusUpdateTime;

    @ColumnWidth(20)
    @Excel(name = "财务合同状态")
    private String financialContractStatus;

    @ColumnWidth(20)
    @Excel(name = "业务合同状态")
    private String contractStatus;

    @ColumnWidth(50)
    @Excel(name = "转入公司(针对内部转让)")
    private String transferOrgId;

    @ColumnWidth(20)
    @Excel(name = "转入合同号(针对内部转让)")
    private String transferContractCode;

    @ColumnWidth(20)
    @Excel(name = "转入合同系统合同状态(针对内部转让)")
    private String transferContractStatus;

    @ColumnWidth(20)
    @Excel(name = "处理状态", readConverterExp = "1=已录入,2=已提交,3=复核通过,4=复核失败")
    private String recordStatus;

}
