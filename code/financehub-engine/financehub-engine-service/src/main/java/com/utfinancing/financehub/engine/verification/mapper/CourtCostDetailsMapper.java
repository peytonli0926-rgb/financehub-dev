package com.utfinancing.financehub.engine.verification.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.verification.entity.CourtCostDetailsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.verification.model.dto.CourtCostDetailsQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.CourtCostReportFormVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 诉讼费转费用详情表 Mapper 接口
 * </p>
 *
 * @author bruyang
 * @since 2023-10-23
 */
public interface CourtCostDetailsMapper extends BaseMapper<CourtCostDetailsEntity> {

    IPage<CourtCostReportFormVO> selectReportFormPage(Page page, @Param("queryDTO")CourtCostDetailsQueryDTO queryDTO);

    List<CourtCostReportFormVO> selectReportFormDetails( @Param("queryDTO")CourtCostDetailsQueryDTO queryDTO);

}
