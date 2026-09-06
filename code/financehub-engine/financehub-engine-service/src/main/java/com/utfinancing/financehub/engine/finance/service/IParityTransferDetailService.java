package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ParityTransferDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ParityTransferDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ParityTransferDetailVO;
import com.utfinancing.financehub.engine.finance.entity.ParityTransferDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description : ParityTransferDetail服务类接口
 * @Modified :
 */
public interface IParityTransferDetailService extends IService<ParityTransferDetailEntity> {

    Long saveParityTransferDetail(ParityTransferDetailDTO dto);

    Long updateParityTransferDetail(Long id, ParityTransferDetailDTO dto);

    ParityTransferDetailDTO getParityTransferDetailDTOById(Long id);

    IPage<ParityTransferDetailVO> selectPage(ParityTransferDetailQueryDTO queryDTO);

    Boolean removeBatcheByDetailId(List<Long> idList);

    List<ParityTransferDetailEntity> getByParityTransferId(Long parityTransferId);

    List<ParityTransferDetailVO> getByBatch(String batch);
}
