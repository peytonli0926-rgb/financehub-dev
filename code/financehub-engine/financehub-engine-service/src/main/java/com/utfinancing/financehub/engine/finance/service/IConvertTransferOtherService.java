package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferOtherEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherPlanQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferOtherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferOtherVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;

import java.util.List;

public interface IConvertTransferOtherService extends IService<ConvertTransferOtherEntity> {
    void importExcel(List<ConvertTransferOtherDTO> dtos);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    Boolean submit(List<Long> ids);

    Boolean withdraw(List<Long> ids);

    Boolean delete(List<Long> ids);

    IPage<ConvertTransferOtherPlanVO> planPage(ConvertTransferOtherPlanQueryDTO dto);

    IPage<ConvertTransferOtherVO> pageQuery(ConvertTransferOtherQueryDTO dto);

    List<ConvertTransferOtherExcelVO> listQuery(ConvertTransferOtherQueryDTO dto);
}
