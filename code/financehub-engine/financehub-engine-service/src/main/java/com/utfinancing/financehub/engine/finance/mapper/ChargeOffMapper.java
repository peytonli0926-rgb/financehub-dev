package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Charge Off手工上传表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-02-27
 */
public interface ChargeOffMapper extends BaseMapper<ChargeOffEntity> {

    IPage<ChargeOffVO> summaryPage(Page page,@Param("param") ChargeOffQueryDTO queryDTO);

    IPage<ChargeOffVO> summaryDetailPage(Page page,@Param("param") ChargeOffDetailQueryDTO queryDTO);

    List<ChargeOffVO> exportForCurMonth(@Param("param") ChargeOffQueryDTO queryDTO);
    List<ChargeOffVO> summaryList(@Param("param") ChargeOffQueryDTO queryDTO);

    boolean isGenerateBalanceMonth(@Param("periodCode") int periodCode);

    List<ChargeOffVO> selectChargeOffSummaryReport(@Param("param") ChargeOffQueryDTO param);
}
