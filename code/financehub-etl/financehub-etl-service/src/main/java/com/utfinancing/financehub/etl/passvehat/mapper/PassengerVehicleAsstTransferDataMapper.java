package com.utfinancing.financehub.etl.passvehat.mapper;

import com.utfinancing.financehub.etl.passvehat.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.financial.mapper.CommonMapper;
import com.utfinancing.financehub.etl.passvehat.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectRepaymentFromCYCXTDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectRepaymentFromCYCXTInputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassengerVehicleAsstTransferDataMapper extends CommonMapper {

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXT(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTGroup(@Param("params") SelectRepaymentFromCYCXTInputDTO params);

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(@Param("params") SelectContractCodeByPageDTO params);

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
}
