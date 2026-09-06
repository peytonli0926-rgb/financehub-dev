package com.utfinancing.financehub.etl.micro.mapper;

import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.mapper.CommonMapper;
import com.utfinancing.financehub.etl.micro.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MicroDataMapper extends CommonMapper {

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXT(@Param("params") SelectRepaymentFromXWXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTGroup(@Param("params") SelectRepaymentFromXWXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTInit(@Param("params") SelectRepaymentFromXWXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTGroupInit(@Param("params") SelectRepaymentFromXWXTInputDTO params);


    /**
     * 从业务系统取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(@Param("params") SelectRepaymentFromXWXTInputDTO params);

    /**
     * 业务系统查询ta数据
     * @param queryDate
     * @return
     */
    List<TaReclassificationDetailEntity> getTaReclassificationDetailList(@Param("queryDate") String queryDate);

    /**
     * 查询未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> selectNonConfirmAmount(@Param("params") SelectNonConfirmAmountInputDTO params);
}
