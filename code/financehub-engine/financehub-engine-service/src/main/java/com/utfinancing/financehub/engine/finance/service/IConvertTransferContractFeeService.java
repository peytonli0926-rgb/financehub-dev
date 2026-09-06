package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferContractFeeQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface IConvertTransferContractFeeService extends IService<ConvertTransferContractFeeEntity> {

    IPage<ConvertTransferContractFeeVO> pageQuery(ConvertTransferContractFeeQueryDTO dto);

    List<ConvertTransferContractFeeExcelVO> export(LocalDate accountDate, List<String> orgIdList);

    Boolean importFile(List<ConvertTransferContractFeeDTO> dtos);

    Boolean generateVoucher(List<Long> ids);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    @Transactional
    void updateProcessStatus(CommonApproveDTO dto);
}
