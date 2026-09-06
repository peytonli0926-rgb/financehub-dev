package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.entity.TailDifferenceAdjustmentEntity;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TailDifferenceAdjustmentQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TailDifferenceAdjustmentDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 尾差调整详情 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-03-05
 */
public interface TailDifferenceAdjustmentDetailMapper extends BaseMapper<TailDifferenceAdjustmentDetailEntity> {

    boolean initInsertData(@Param("param") TailDifferenceAdjustmentQueryDTO queryDTO);

    List<TailDifferenceAdjustmentEntity> selectAllOrgIdAccountCode();


    List<TailDifferenceAdjustmentDetailVO> selectByParams(@Param("param") TailDifferenceAdjustmentDetailQueryDTO queryDTO);

    IPage<TailDifferenceAdjustmentDetailVO> selectPageByParams(Page page, @Param("param") TailDifferenceAdjustmentDetailQueryDTO queryDTO);

    void generateContractBalanceTempData(@Param("param") TailDifferenceAdjustmentQueryDTO periodCode);

    void truncateContractBalanceTempData();
}
