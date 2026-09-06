package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-09-24
 * @Description : 上传分摊信息及此次计提相关合同的分摊方式
 * @Modified :
 */
@Data
public class ServiceFeeImport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "计提月份", cellType = Excel.ColumnType.DATE, type = Excel.Type.IMPORT)
    private Date businessDate;

    @Excel(name = "服务费协议编号", type = Excel.Type.IMPORT)
    private String serviceFeeNo;

    @Excel(name = "合同编号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "分摊方式", type = Excel.Type.IMPORT, readConverterExp = "0=租赁收入分摊,1=服务费收入分摊", comment = "租赁收入分摊/服务费收入分摊")
    private String allocationMethod;

    @Excel(name = "计提金额", cellType = Excel.ColumnType.NUMERIC, type = Excel.Type.IMPORT)
    private BigDecimal accruedAmount;

    @Excel(name = "是否服务费分摊标识", type = Excel.Type.IMPORT, readConverterExp = "0=否,1=是")
    private String sharingServiceFeeFlag;

    @Excel(name = "是否结束服务费分摊标识", type = Excel.Type.IMPORT, readConverterExp = "0=否,1=是")
    private String endSharingServiceFeeFlag;


}
