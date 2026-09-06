package com.utfinancing.financehub.etl.passvehat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.etl.enums.AccrualMethodEnum;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.passvehat.model.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectRepaymentFromCYCXTDTO;
import com.utfinancing.financehub.etl.passvehat.model.SelectRepaymentFromCYCXTInputDTO;
import com.utfinancing.financehub.etl.passvehat.mapper.PassengerVehicleAsstTransferDataMapper;
import com.utfinancing.financehub.etl.passvehat.service.IPassengerVehicleAssetTransferDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PassengerVehicleAssetTransferDataServiceImpl implements IPassengerVehicleAssetTransferDataService {

    @Resource
    PassengerVehicleAsstTransferDataMapper passengerVehicleAsstTransferDataMapper;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return passengerVehicleAsstTransferDataMapper.selectDataCommon(querySql);
    }

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(SelectContractCodeByPageDTO params) {
        return passengerVehicleAsstTransferDataMapper.selectContractCodeByPage(params);
    }

    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTInit(SelectRepaymentFromCYCXTInputDTO dto) {
        return passengerVehicleAsstTransferDataMapper.selectRepaymentFromCYCXTInit(dto);
    }

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTGroupInit(SelectRepaymentFromCYCXTInputDTO dto) {
        return passengerVehicleAsstTransferDataMapper.selectRepaymentFromCYCXTGroupInit(dto);
    }

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromCYCXTInputDTO dto) {
        return passengerVehicleAsstTransferDataMapper.selectReceiveRepayment(dto);
    }

    /**
     * 从业务系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromCYCXT(QueryRepaymentPlanDTO params) {
        SelectRepaymentFromCYCXTInputDTO dto = new SelectRepaymentFromCYCXTInputDTO();
        dto.setContractCodeList(Arrays.asList(params.getContractCode()));
        List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTDTOList = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(params.getIncomeProvisionMethod())){
            selectRepaymentFromCYCXTDTOList = passengerVehicleAsstTransferDataMapper.selectRepaymentFromCYCXT(dto);
        } else {
            selectRepaymentFromCYCXTDTOList = passengerVehicleAsstTransferDataMapper.selectRepaymentFromCYCXTGroup(dto);
        }
        if (selectRepaymentFromCYCXTDTOList == null || selectRepaymentFromCYCXTDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanSaveDTO> result = BeanUtil.copyToList(selectRepaymentFromCYCXTDTOList, RepaymentPlanSaveDTO.class);
        return result;
    }
}
