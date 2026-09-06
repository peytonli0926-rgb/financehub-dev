package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferDetailVO;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description : ConvertTransferDetail服务类接口
 * @Modified :
 */
public interface IConvertTransferDetailService extends IService<ConvertTransferDetailEntity> {

    Long saveConvertTransferDetail(ConvertTransferDetailDTO dto);

    Long updateConvertTransferDetail(Long id, ConvertTransferDetailDTO dto);

    ConvertTransferDetailDTO getConvertTransferDetailDTOById(Long id);

    IPage<ConvertTransferDetailVO> selectPage(ConvertTransferDetailQueryDTO queryDTO);

    List<ConvertTransferDetailExcelVO> selectList(List<Long> transferIds);

    void deleteByConvertTransferId(List<Long> ids);

    IPage<ConvertTransferCheckVO> check( ConvertTransferDetailQueryDTO queryDTO);
}
