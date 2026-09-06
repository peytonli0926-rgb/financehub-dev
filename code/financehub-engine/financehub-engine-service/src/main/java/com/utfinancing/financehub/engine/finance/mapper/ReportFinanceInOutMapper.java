package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ReportFinanceInOutEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ReportFinanceInOutQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表-财务入库出库 Mapper 接口
 * </p>
 *
 * @author jnc
 * @since 2024-04-22
 */
public interface ReportFinanceInOutMapper extends BaseMapper<ReportFinanceInOutEntity> {

    List<ReportFinanceInOutDTO> selectInAndOutDataByPeriodCode();

    List<ReportFinanceInOutDTO> selectFinanceInboundAndOutboundDataByPage(@Param("params") ReportFinanceInOutQueryDTO queryDTO);

    Map<String, Long> selectFinanceInboundAndOutboundSize(@Param("param") ReportFinanceInOutQueryDTO queryDTO);

    /**
     * @description: 获取当前月份的财务出库入库数据
     * @author: zhangli.chen
     **/
    List<ReportFinanceInOutDTO> getFinanceInboundAndOutboundDataForCurrentMonth(@Param("param") ReportFinanceInOutQueryDTO queryDTO);




}
