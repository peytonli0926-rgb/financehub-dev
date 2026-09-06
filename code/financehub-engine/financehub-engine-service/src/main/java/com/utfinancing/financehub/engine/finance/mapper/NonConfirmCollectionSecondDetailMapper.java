package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSecondDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 未确认收款明细表(第二层明细) Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-03-22
 */
public interface NonConfirmCollectionSecondDetailMapper extends BaseMapper<NonConfirmCollectionSecondDetailEntity> {
    /**
     * 手工处理页面数据查询
     */
    public List<ManualProcessListDTO> selectManualProcessData(@Param("params") QueryManualProcessDTO params);

    /**
     * 详情页面数据查询
     */
    public List<QueryDetailListDataSubDTO> selectDetailData(@Param("params") QueryDetailDataDTO params);

    /**
     * 查询第三层明细列表-恒运业务
     */
    public List<QueryThirdDetailPageListDataDTO> selectThirdDetailForHy(@Param("params") QueryThirdDetailPageDataDTO params);

    /**
     * 查询第三层明细列表-其它业务
     */
    public List<QueryThirdDetailPageListDataDTO> selectThirdDetailForOther(@Param("params") QueryThirdDetailPageDataDTO params);

    /**
     * 查询第三层明细列表-分页查询
     */
    public List<QueryThirdDetailPageListDataDTO> selectThirdDetailData(@Param("params") QueryThirdDetailPageDataDTO params);

    /**
     * 查询第三层明细列表-汇总数
     */
    public Integer selectThirdDetailDataCount(@Param("params") QueryThirdDetailPageDataDTO params);


}
