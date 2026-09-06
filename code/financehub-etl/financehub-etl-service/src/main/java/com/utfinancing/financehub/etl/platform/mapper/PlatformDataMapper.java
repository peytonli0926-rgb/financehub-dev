package com.utfinancing.financehub.etl.platform.mapper;

import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.mapper.CommonMapper;
import com.utfinancing.financehub.etl.platform.model.SelectNonConfirmAmountInputDTO;
import com.utfinancing.financehub.etl.platform.model.SelectNonConfirmAmountOutputDTO;
import com.utfinancing.financehub.etl.platform.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.platform.model.SelectRepaymentFromTYPTDTO;
import com.utfinancing.financehub.etl.platform.model.SelectRepaymentFromTYPTInputDTO;
import com.utfinancing.financehub.etl.platform.model.SelectTaAmountDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformDataMapper extends CommonMapper {

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPT(@Param("params") SelectRepaymentFromTYPTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTGroup(@Param("params") SelectRepaymentFromTYPTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTInit(@Param("params") SelectRepaymentFromTYPTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTGroupInit(@Param("params") SelectRepaymentFromTYPTInputDTO params);

    /**
     * 从业务系统取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(@Param("params") SelectRepaymentFromTYPTInputDTO params);

    /**
     * 查询ta余额
     */
    public List<SelectTaAmountDTO> selectTaAmount();


    List<TaReclassificationDetailEntity> getTaReclassificationDetailList(@Param("queryDate") String queryDate);

    /**
     * 查询未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> selectNonConfirmAmount(@Param("params") SelectNonConfirmAmountInputDTO params);
}
