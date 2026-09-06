package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutDTO;
import com.utfinancing.financehub.engine.finance.entity.ReportFinanceInOutEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.hthx.common.page.PageResult;

import java.util.Map;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-22
 * @Description : ReportFinanceInOut服务类接口
 * @Modified :
 */
public interface IReportFinanceInOutService extends IService<ReportFinanceInOutEntity> {

    Long saveReportFinanceInOut(ReportFinanceInOutDTO dto);

    Long updateReportFinanceInOut(Long id, ReportFinanceInOutDTO dto);

    ReportFinanceInOutDTO getReportFinanceInOutDTOById(Long id);

    PageResult selectPage(ReportFinanceInOutQueryDTO queryDTO);

    String syncFinanceInboundOutboundData();

    Map<String, String> generateInboundOutboundReportExcel(ReportFinanceInOutQueryDTO queryDTO);

    IPage<FileRecordEntity> selectInboundOutboundFileList(FileRecordQueryDTO queryDTO);
}
