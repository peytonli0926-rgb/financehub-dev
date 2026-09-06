package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ChargeOffSummaryReportDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffSummaryReportVO;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffSummaryReportEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-07-26
 * @Description : ChargeOffSummaryReport服务类接口
 * @Modified :
 */
public interface IChargeOffSummaryReportService extends IService<ChargeOffSummaryReportEntity> {

    Long saveChargeOffSummaryReport(ChargeOffSummaryReportDTO dto);

    Long updateChargeOffSummaryReport(Long id, ChargeOffSummaryReportDTO dto);

    ChargeOffSummaryReportDTO getChargeOffSummaryReportDTOById(Long id);

    IPage<ChargeOffSummaryReportVO> selectPage(ChargeOffSummaryReportQueryDTO queryDTO);

    IPage<ChargeOffSummaryReportVO> summaryDetailPage(ChargeOffSummaryReportQueryDTO queryDTO);
}
