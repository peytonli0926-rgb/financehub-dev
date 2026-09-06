package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportLeaseTableQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ReportLeaseTableVO;

import java.util.Map;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description : 统计报表服务类接口
 * @Modified :
 */
public interface IReportCommonService {

    IPage<ReportLeaseTableVO> selectLeaseTable(ReportLeaseTableQueryDTO queryDTO);

    Map<String, String> checkLeaseTableDate(ReportLeaseTableQueryDTO queryDTO);

    Map<String, String> generateLeaseTableReportExcel(ReportLeaseTableQueryDTO queryDTO);

    IPage<FileRecordEntity> selectLeaseTableFileList(FileRecordQueryDTO queryDTO);

}
