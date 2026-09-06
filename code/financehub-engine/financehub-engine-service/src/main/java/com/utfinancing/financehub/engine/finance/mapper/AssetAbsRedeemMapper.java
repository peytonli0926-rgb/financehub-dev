package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.AssetAbsRedeemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.AssetAbsRedeemDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.AssetAbsRedeemDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资产转让ABS-赎回 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-15
 */
public interface AssetAbsRedeemMapper extends BaseMapper<AssetAbsRedeemEntity> {

    List<AssetAbsRedeemDetailVO> selectByCondition(@Param("param") AssetAbsRedeemDetailQueryDTO detailQueryDTO);

}
