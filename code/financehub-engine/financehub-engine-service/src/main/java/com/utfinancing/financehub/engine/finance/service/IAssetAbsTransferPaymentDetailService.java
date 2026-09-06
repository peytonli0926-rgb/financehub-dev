package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentDetailVO;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-20
 * @Description : AssetAbsTransferPaymentDetail服务类接口
 * @Modified :
 */
public interface IAssetAbsTransferPaymentDetailService extends IService<AssetAbsTransferPaymentDetailEntity> {

    Long saveAssetAbsTransferPaymentDetail(AssetAbsTransferPaymentDetailDTO dto);

    Long updateAssetAbsTransferPaymentDetail(Long id, AssetAbsTransferPaymentDetailDTO dto);

    AssetAbsTransferPaymentDetailDTO getAssetAbsTransferPaymentDetailDTOById(Long id);

    IPage<AssetAbsTransferPaymentDetailVO> selectPage(AssetAbsTransferPaymentDetailQueryDTO queryDTO);

    Boolean removeBatcheByDetailId(List<Long> idList);

    List<AssetAbsTransferPaymentDetailVO> selectPaymentDetailList(AssetAbsTransferPaymentDetailQueryDTO queryDTO);
}
