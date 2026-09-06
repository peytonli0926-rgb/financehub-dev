package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceInterfaceTotalQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceInterfaceTotalDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PayableInsuranceInterfaceTotalSaveDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PayableInsuranceInterfaceTotalVO;
import com.utfinancing.financehub.engine.finance.entity.PayableInsuranceInterfaceTotalEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-11-30
 * @Description : PayableInsuranceInterfaceTotal服务类接口
 * @Modified :
 */
public interface IPayableInsuranceInterfaceTotalService extends IService<PayableInsuranceInterfaceTotalEntity> {

    Long savePayableInsuranceInterfaceTotal(PayableInsuranceInterfaceTotalSaveDTO dto);

    Long updatePayableInsuranceInterfaceTotal(Long id, PayableInsuranceInterfaceTotalDTO dto);

    PayableInsuranceInterfaceTotalDTO getPayableInsuranceInterfaceTotalDTOById(Long id);

    IPage<PayableInsuranceInterfaceTotalVO> selectPage(PayableInsuranceInterfaceTotalQueryDTO queryDTO);

    void saveFromInterfaceData(Map<String, Object> dataMap);

    List<PayableInsuranceInterfaceTotalDTO> listByContractCode(List<String> contractCodes);
}
