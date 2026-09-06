package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConvertTransferContractFeeDTO {

    @Excel(name = "记账日期", dateFormat = "yyyy-MM-dd")
    private String accountDate;

    @Excel(name = "签约主体")
    private String orgId;

    @Excel(name = "合同编号")
    private String contractCode;

    @Excel(name = "费用类型")
    private String transferFeeType;

    @Excel(name = "金额")
    private BigDecimal transferFee;

    @Excel(name = "成本中心")
    private String costCenter;

    public ConvertTransferContractFeeDetailEntity toDetailEntity() {
        validate();
        ConvertTransferContractFeeDetailEntity entity = new ConvertTransferContractFeeDetailEntity();
        entity.setUploadDate(LocalDate.now());
        entity.setAccountDate(LocalDate.parse(accountDate));
        entity.setOrgId(orgId);
        entity.setContractCode(contractCode);
        entity.setTransferFeeType(transferFeeType);
        entity.setTransferFee(transferFee);
        entity.setCostCenter(costCenter);
        return entity;
    }
    public void validate(){
        if(accountDate == null){
            throw new ServiceException("记账日期不能为空");
        }
        if(orgId == null || orgId.trim().isEmpty()){
            throw new ServiceException("签约主体不能为空");
        }
        orgId = orgId.trim();
        if(contractCode == null || contractCode.trim().isEmpty()){
            throw new ServiceException("合同编号不能为空");
        }
        contractCode = contractCode.trim();
        if(transferFeeType == null || transferFeeType.trim().isEmpty()){
            throw new ServiceException("费用类型不能为空");
        }
        transferFeeType = transferFeeType.trim();
        if(transferFee == null){
            throw new ServiceException("金额不能为空");
        }
        if(costCenter == null || costCenter.trim().isEmpty()){
            throw new ServiceException("成本中心不能为空");
        }
        costCenter = costCenter.trim();
    }
}
