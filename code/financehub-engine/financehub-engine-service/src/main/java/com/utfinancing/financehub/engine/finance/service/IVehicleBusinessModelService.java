package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.entity.VehicleBusinessModelEntity;
import com.utfinancing.financehub.engine.finance.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.VehicleLifecycleVO;
import com.utfinancing.financehub.engine.finance.model.vo.VehicleContractExcelVO;

import java.util.List;
import java.util.Map;

public interface IVehicleBusinessModelService extends IService<VehicleBusinessModelEntity> {
    void saveOrUpdateFromLeaseStart(Map<String, Object> data);
    IPage<VehicleBusinessModelEntity> selectPage(ContractQueryDTO queryDTO);
    List<VehicleContractExcelVO> export(ContractQueryDTO queryDTO);
    VehicleLifecycleVO getLifecycle(Long contractId);
}
