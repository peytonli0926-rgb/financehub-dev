package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemDetailVO;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsRedeemDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-03-15
 * @Description : AssetAbsRedeemDetail服务类接口
 * @Modified :
 */
public interface IAssetAbsRedeemDetailService extends IService<AssetAbsRedeemDetailEntity> {

    Long saveAssetAbsRedeemDetail(AssetAbsRedeemDetailDTO dto);

    Long updateAssetAbsRedeemDetail(Long id, AssetAbsRedeemDetailDTO dto);

    AssetAbsRedeemDetailDTO getAssetAbsRedeemDetailDTOById(Long id);

    IPage<AssetAbsRedeemDetailVO> selectPage(AssetAbsRedeemDetailQueryDTO queryDTO);

    Boolean removeBatcheByDetailId(List<Long> idList);

    List<AssetAbsRedeemDetailVO> selectByCondition(AssetAbsRedeemDetailQueryDTO queryDTO);

}
