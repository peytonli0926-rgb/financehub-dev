package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailReportVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailSummaryVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionDetailEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description : ImpairmentProvisionDetail服务类接口
 * @Modified :
 */
public interface IImpairmentProvisionDetailService extends IService<ImpairmentProvisionDetailEntity> {

    Long saveImpairmentProvisionDetail(ImpairmentProvisionDetailDTO dto);

    Long updateImpairmentProvisionDetail(Long id, ImpairmentProvisionDetailDTO dto);

    ImpairmentProvisionDetailDTO getImpairmentProvisionDetailDTOById(Long id);

    IPage<ImpairmentProvisionDetailVO> selectPage(ImpairmentProvisionDetailQueryDTO queryDTO);

    /**
     * 查询
     * @param queryDTO
     * @return
     */
    List<ImpairmentProvisionDetailVO> selectList(ImpairmentProvisionDetailQueryDTO queryDTO);

    /**
     * 查询汇总数据
     * @param queryDTO
     * @return
     */
    ImpairmentProvisionDetailSummaryVO summary(ImpairmentProvisionDetailQueryDTO queryDTO);

    /**
     * 获取本月数据
     * @return
     */
    List<ImpairmentProvisionDetailVO> getThisMonthDataList(String periodCode, String firstDay);

    /**
     * 查询本月转出
     * @return
     */
    Map<String, BigDecimal> getTransferOut(String periodCode, String firstDay);

    /**
     * 导出excel-明细数据
     * @return
     */
    void exportProvisionDetailExcel(HttpServletResponse response,ImpairmentProvisionDetailQueryDTO queryDTO) throws Exception;

    List<ImpairmentProvisionDetailVO> selectDetailList(ImpairmentProvisionDetailQueryDTO queryDTO);
}
