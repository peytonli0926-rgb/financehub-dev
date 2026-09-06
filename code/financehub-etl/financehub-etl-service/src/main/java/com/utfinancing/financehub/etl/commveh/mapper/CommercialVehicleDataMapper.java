package com.utfinancing.financehub.etl.commveh.mapper;

import com.utfinancing.financehub.etl.commveh.model.*;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommercialVehicleDataMapper extends CommonMapper {

    public List<String> selectContractCodeByPage(@Param("params") SelectContractCodeByPageDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXT(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroup(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTInit(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroupInit(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    List<TaReclassificationDetailEntity> getTaReclassificationDetailList(@Param("queryDate") String queryDate);

    /**
     * 查询溢存款金额
     */
    public List<SelectSurplusDepositOutputDTO> selectSurplusDeposit(@Param("params") SelectSurplusDepositInputDTO params);

    /**
     * 查询未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> selectNonConfirmAmount(@Param("params") SelectNonConfirmAmountInputDTO params);
}
