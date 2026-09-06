package com.utfinancing.financehub.engine.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailSummaryVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 减值计提明细 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-03-25
 */
public interface ImpairmentProvisionDetailMapper extends BaseMapper<ImpairmentProvisionDetailEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryDTO
     * @return
     */
    IPage<ImpairmentProvisionDetailVO> selectPageByMapper(Page page, @Param("queryDTO") ImpairmentProvisionDetailQueryDTO queryDTO);

    /**
     * 查询
     * @param queryDTO
     * @return
     */
    List<ImpairmentProvisionDetailVO> selectPageByMapper(@Param("queryDTO") ImpairmentProvisionDetailQueryDTO queryDTO);

    /**
     * 查询本月转出
     * @return
     */
    Map<String, BigDecimal> getTransferOut(@Param("periodCode") String periodCode,@Param("firstDay") String firstDay);

    /**
     * 汇总数据
     * @param queryDTO
     * @return
     */
    ImpairmentProvisionDetailSummaryVO selectSummary(@Param("queryDTO") ImpairmentProvisionDetailQueryDTO queryDTO);

    /**
     * 1、有会计期间，那么期间按就必须是当前期间；
     * 2、没有会计期间，那么创建日期必须是大于当前期间的第一天；
     * 这样就把本月操作的数据查询出来了。
     * @param
     * @return
     */
    List<ImpairmentProvisionDetailVO> getThisMonthDataList(@Param("periodCode") String periodCode,@Param("firstDay") String firstDay);
}
