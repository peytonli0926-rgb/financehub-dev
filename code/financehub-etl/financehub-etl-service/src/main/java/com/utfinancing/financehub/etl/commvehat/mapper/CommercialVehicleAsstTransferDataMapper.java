package com.utfinancing.financehub.etl.commvehat.mapper;

import com.utfinancing.financehub.etl.commvehat.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.commvehat.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTDTO;
import com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTInputDTO;
import com.utfinancing.financehub.etl.financial.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommercialVehicleAsstTransferDataMapper extends CommonMapper {

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXT(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroup(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(@Param("params") SelectContractCodeByPageDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTInit(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得偿还计划数据
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroupInit(@Param("params") SelectRepaymentFromSYCXTInputDTO params);

    /**
     * 从业务系统取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(@Param("params") SelectRepaymentFromSYCXTInputDTO params);
}
