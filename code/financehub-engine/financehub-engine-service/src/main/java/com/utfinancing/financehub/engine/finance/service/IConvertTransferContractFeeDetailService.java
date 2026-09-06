package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeExcelVO;

import java.util.List;

public interface IConvertTransferContractFeeDetailService extends IService<ConvertTransferContractFeeDetailEntity> {
    List<ConvertTransferContractFeeDetailExcelVO> export(Long transferId, String transferFeeType, String contractCode);
}
