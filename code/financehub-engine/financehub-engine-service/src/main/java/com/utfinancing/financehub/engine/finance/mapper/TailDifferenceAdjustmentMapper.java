package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceVO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 尾差调整 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-05
 */
public interface TailDifferenceAdjustmentMapper extends BaseMapper<TailDifferenceAdjustmentEntity> {

    IPage<TailDifferenceAdjustmentVO> selectDifferenceAdjustPage(Page page, @Param("param") TailDifferenceAdjustmentQueryDTO queryDTO);

}
