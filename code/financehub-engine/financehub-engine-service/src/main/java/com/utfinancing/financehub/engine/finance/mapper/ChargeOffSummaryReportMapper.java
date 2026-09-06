package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffSummaryReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffSummaryReportVO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Charge Off汇总报表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2024-07-26
 */
public interface ChargeOffSummaryReportMapper extends BaseMapper<ChargeOffSummaryReportEntity> {
    IPage<ChargeOffSummaryReportVO> summaryPage(Page page, @Param("param") ChargeOffSummaryReportQueryDTO queryDTO);

    IPage<ChargeOffSummaryReportVO> summaryDetailPage(Page page,@Param("param") ChargeOffSummaryReportQueryDTO queryDTO);
}
