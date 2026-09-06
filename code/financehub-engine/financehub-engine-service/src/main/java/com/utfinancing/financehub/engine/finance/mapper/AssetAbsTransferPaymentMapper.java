package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.AssetAbsTransferPaymentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsTransferPaymentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsTransferPaymentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产转付 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
public interface AssetAbsTransferPaymentMapper extends BaseMapper<AssetAbsTransferPaymentEntity> {

    IPage<AssetAbsTransferPaymentVO> selectPageByCondition(Page page,@Param("param") AssetAbsTransferPaymentQueryDTO queryDTO);

    List<AssetAbsTransferPaymentDetailVO> selectListByCondition(@Param("param") AssetAbsTransferPaymentQueryDTO queryDTO);

    List<AssetAbsTransferPaymentVO> selectPaymentList(@Param("param") AssetAbsTransferPaymentQueryDTO queryDTO);

}
