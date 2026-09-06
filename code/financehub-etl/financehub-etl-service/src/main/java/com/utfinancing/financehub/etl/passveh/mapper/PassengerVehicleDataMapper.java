package com.utfinancing.financehub.etl.passveh.mapper;

import com.utfinancing.financehub.etl.passveh.model.*;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassengerVehicleDataMapper extends CommonMapper {


    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(@Param("params") SelectContractCodeByPageDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXT(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTGroup(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTInit(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据-期初数据
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTGroupInit(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

    /**
     * 从业务系统取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

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
