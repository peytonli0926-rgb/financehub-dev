package com.utfinancing.financehub.etl.commvehat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.etl.commvehat.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.commvehat.model.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.commvehat.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTDTO;
import com.utfinancing.financehub.etl.commvehat.model.SelectRepaymentFromSYCXTInputDTO;
import com.utfinancing.financehub.etl.commvehat.mapper.CommercialVehicleAsstTransferDataMapper;
import com.utfinancing.financehub.etl.commvehat.service.ICommercialVehicleAssetTransferDataService;
import com.utfinancing.financehub.etl.enums.AccrualMethodEnum;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommercialVehicleAssetTransferDataServiceImpl implements ICommercialVehicleAssetTransferDataService {

    @Resource
    CommercialVehicleAsstTransferDataMapper commercialVehicleAsstTransferDataMapper;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return commercialVehicleAsstTransferDataMapper.selectDataCommon(querySql);
    }

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(SelectContractCodeByPageDTO params) {
        return commercialVehicleAsstTransferDataMapper.selectContractCodeByPage(params);
    }

    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTInit(SelectRepaymentFromSYCXTInputDTO dto) {
        return commercialVehicleAsstTransferDataMapper.selectRepaymentFromSYCXTInit(dto);
    }

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroupInit(SelectRepaymentFromSYCXTInputDTO dto) {
        return commercialVehicleAsstTransferDataMapper.selectRepaymentFromSYCXTGroupInit(dto);
    }

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromSYCXTInputDTO dto) {
        return commercialVehicleAsstTransferDataMapper.selectReceiveRepayment(dto);
    }

    /**
     * 从业务系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromSYCXT(QueryRepaymentPlanDTO params) {
        SelectRepaymentFromSYCXTInputDTO dto = new SelectRepaymentFromSYCXTInputDTO();
        dto.setContractCodeList(Arrays.asList(params.getContractCode()));
        List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTDTOList = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(params.getIncomeProvisionMethod())){
            selectRepaymentFromSYCXTDTOList = commercialVehicleAsstTransferDataMapper.selectRepaymentFromSYCXT(dto);
        } else {
            selectRepaymentFromSYCXTDTOList = commercialVehicleAsstTransferDataMapper.selectRepaymentFromSYCXTGroup(dto);
        }
        if (selectRepaymentFromSYCXTDTOList == null || selectRepaymentFromSYCXTDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanSaveDTO> result = BeanUtil.copyToList(selectRepaymentFromSYCXTDTOList, RepaymentPlanSaveDTO.class);
        return result;
    }
}
