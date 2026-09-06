package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产转付详情表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
public interface AssetAbsTransferPaymentDetailMapper extends BaseMapper<AssetAbsTransferPaymentDetailEntity> {

    List<AssetAbsTransferPaymentDetailVO> selectPaymentDetailList(@Param("param")AssetAbsTransferPaymentDetailQueryDTO queryDTO);
}
