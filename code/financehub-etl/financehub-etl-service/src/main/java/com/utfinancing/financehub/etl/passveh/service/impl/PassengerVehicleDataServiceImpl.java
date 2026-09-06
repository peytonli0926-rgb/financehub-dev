package com.utfinancing.financehub.etl.passveh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.utfinancing.financehub.etl.passveh.model.SelectNonConfirmAmountInputDTO;
import com.utfinancing.financehub.etl.passveh.model.SelectNonConfirmAmountOutputDTO;
import com.utfinancing.financehub.etl.passveh.model.SelectSurplusDepositInputDTO;
import com.utfinancing.financehub.etl.passveh.model.SelectSurplusDepositOutputDTO;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.service.IOrgCompanyService;
import com.utfinancing.financehub.etl.passveh.model.SelectContractCodeByPageDTO;
import com.utfinancing.financehub.etl.enums.AccrualMethodEnum;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.passveh.mapper.PassengerVehicleDataMapper;
import com.utfinancing.financehub.etl.passveh.model.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.passveh.model.SelectReceiveRepaymentDTO;
import com.utfinancing.financehub.etl.passveh.model.SelectRepaymentFromCYCXTDTO;
import com.utfinancing.financehub.etl.passveh.model.SelectRepaymentFromCYCXTInputDTO;
import com.utfinancing.financehub.etl.passveh.service.IPassengerVehicleDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PassengerVehicleDataServiceImpl implements IPassengerVehicleDataService {

    @Resource
    PassengerVehicleDataMapper passengerVehicleDataMapper;

    @Resource
    private IOrgCompanyService orgCompanyService;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return passengerVehicleDataMapper.selectDataCommon(querySql);
    }

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(SelectContractCodeByPageDTO params) {
        return passengerVehicleDataMapper.selectContractCodeByPage(params);
    }


    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTInit(SelectRepaymentFromCYCXTInputDTO dto) {
        return passengerVehicleDataMapper.selectRepaymentFromCYCXTInit(dto);
    }

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTGroupInit(SelectRepaymentFromCYCXTInputDTO dto) {
        return passengerVehicleDataMapper.selectRepaymentFromCYCXTGroupInit(dto);
    }

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromCYCXTInputDTO dto) {
        return passengerVehicleDataMapper.selectReceiveRepayment(dto);
    }

    /**
     * 从业务系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromCYCXT(QueryRepaymentPlanDTO params) {
        SelectRepaymentFromCYCXTInputDTO dto = new SelectRepaymentFromCYCXTInputDTO();
        dto.setContractCodeList(Arrays.asList(params.getContractCode()));
        List<SelectRepaymentFromCYCXTDTO> selectRepaymentFromCYCXTDTOList = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(params.getIncomeProvisionMethod())){
            selectRepaymentFromCYCXTDTOList = passengerVehicleDataMapper.selectRepaymentFromCYCXT(dto);
        } else {
            selectRepaymentFromCYCXTDTOList = passengerVehicleDataMapper.selectRepaymentFromCYCXTGroup(dto);
        }
        if (selectRepaymentFromCYCXTDTOList == null || selectRepaymentFromCYCXTDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanSaveDTO> result = BeanUtil.copyToList(selectRepaymentFromCYCXTDTOList, RepaymentPlanSaveDTO.class);
        return result;
    }

    @Override
    public List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate, Map<String, String> orgMap) {
        List<TaReclassificationDetailEntity> list = passengerVehicleDataMapper.getTaReclassificationDetailList(queryDate);
        if(CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(i->{
                i.setSystemCode(SystemEnum.CYCXT.getCode());

                if(StringUtils.isNotEmpty(i.getBankOrgId())&&orgMap.containsKey(i.getBankOrgId())){
                    i.setBankOrgId(orgMap.get(i.getBankOrgId()));
                }
            });
        }

        return list;
    }

    /**
     * 取得业务系统的未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> queryNonConfirmAmount(Date queryDate) {
        // 查询溢存款余额
        SelectSurplusDepositInputDTO selectSurplusDepositInputDTO = new SelectSurplusDepositInputDTO();
        selectSurplusDepositInputDTO.setQueryDate(DateUtils.format(queryDate, "yyyy-MM-dd"));
        List<SelectSurplusDepositOutputDTO> selectSurplusDepositOutputDTOList =
                passengerVehicleDataMapper.selectSurplusDeposit(selectSurplusDepositInputDTO);
        Map<String, SelectSurplusDepositOutputDTO> surplusDepositMap = selectSurplusDepositOutputDTOList.stream().
                collect(Collectors.toMap(e->e.getEbankSerialNumber(), (e)->e, (a, b)->b));

        // 取得未出账金额
        SelectNonConfirmAmountInputDTO selectNonConfirmAmountInputDTO = new SelectNonConfirmAmountInputDTO();
        selectNonConfirmAmountInputDTO.setQueryDate(DateUtils.format(queryDate, "yyyy-MM-dd"));
        selectNonConfirmAmountInputDTO.setSnapshootDate(DateUtil.format(
                DateUtil.offsetDay(queryDate, -1), "yyyy-MM-dd"));
        List<SelectNonConfirmAmountOutputDTO> selectNonConfirmAmountOutputDTOList =
                passengerVehicleDataMapper.selectNonConfirmAmount(selectNonConfirmAmountInputDTO);
        if (selectNonConfirmAmountOutputDTOList == null || selectNonConfirmAmountOutputDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        for (SelectNonConfirmAmountOutputDTO e : selectNonConfirmAmountOutputDTOList) {
            if (surplusDepositMap.containsKey(e.getVcWangybHy())) {
                SelectSurplusDepositOutputDTO selectSurplusDepositOutputDTO = surplusDepositMap.get(e.getVcWangybHy());
                if (e.getDecKehxje() == null) {
                    e.setDecKehxje(BigDecimal.ZERO);
                }
                if (selectSurplusDepositOutputDTO.getNonOutcomeAmount() == null) {
                    selectSurplusDepositOutputDTO.setNonOutcomeAmount(BigDecimal.ZERO);
                }
                e.setDecKehxje(e.getDecKehxje().add(selectSurplusDepositOutputDTO.getNonOutcomeAmount()));
            }
        }
        return selectNonConfirmAmountOutputDTOList;
    }
}
