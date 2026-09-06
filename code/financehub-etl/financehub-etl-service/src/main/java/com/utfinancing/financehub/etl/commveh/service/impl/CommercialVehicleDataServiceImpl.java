package com.utfinancing.financehub.etl.commveh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.utfinancing.financehub.etl.commveh.mapper.CommercialVehicleDataMapper;
import com.utfinancing.financehub.etl.commveh.model.*;
import com.utfinancing.financehub.etl.commveh.service.ICommercialVehicleDataService;
import com.utfinancing.financehub.etl.enums.AccrualMethodEnum;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialVehicleDataServiceImpl implements ICommercialVehicleDataService {

    @Resource
    CommercialVehicleDataMapper commercialVehicleDataMapper;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return commercialVehicleDataMapper.selectDataCommon(querySql);
    }

    /**
     * 分页查询合同列表
     */
    public List<String> selectContractCodeByPage(SelectContractCodeByPageDTO params) {
        return commercialVehicleDataMapper.selectContractCodeByPage(params);
    }

    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTInit(SelectRepaymentFromSYCXTInputDTO dto) {
        return commercialVehicleDataMapper.selectRepaymentFromSYCXTInit(dto);
    }

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTGroupInit(SelectRepaymentFromSYCXTInputDTO dto) {
        return commercialVehicleDataMapper.selectRepaymentFromSYCXTGroupInit(dto);
    }

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromSYCXTInputDTO dto) {
        return commercialVehicleDataMapper.selectReceiveRepayment(dto);
    }

    /**
     * 从业务系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromSYCXT(QueryRepaymentPlanDTO params) {
        SelectRepaymentFromSYCXTInputDTO dto = new SelectRepaymentFromSYCXTInputDTO();
        dto.setContractCodeList(Arrays.asList(params.getContractCode()));
        List<SelectRepaymentFromSYCXTDTO> selectRepaymentFromSYCXTDTOList = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(params.getIncomeProvisionMethod())){
            selectRepaymentFromSYCXTDTOList = commercialVehicleDataMapper.selectRepaymentFromSYCXT(dto);
        } else {
            selectRepaymentFromSYCXTDTOList = commercialVehicleDataMapper.selectRepaymentFromSYCXTGroup(dto);
        }
        if (selectRepaymentFromSYCXTDTOList == null || selectRepaymentFromSYCXTDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanSaveDTO> result = BeanUtil.copyToList(selectRepaymentFromSYCXTDTOList, RepaymentPlanSaveDTO.class);
        return result;
    }

    @Override
    public List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate , Map<String, String> orgMap) {
        List<TaReclassificationDetailEntity> list = commercialVehicleDataMapper.getTaReclassificationDetailList(queryDate);
        if(CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(i->{
                if(StringUtils.isNotEmpty(i.getBankOrgId())&&orgMap.containsKey(i.getBankOrgId())){
                    i.setBankOrgId(orgMap.get(i.getBankOrgId()));
                }
                i.setSystemCode(SystemEnum.SYCXT.getCode());
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
                commercialVehicleDataMapper.selectSurplusDeposit(selectSurplusDepositInputDTO);
        Map<String, SelectSurplusDepositOutputDTO> surplusDepositMap = selectSurplusDepositOutputDTOList.stream().
                collect(Collectors.toMap(e->e.getEbankSerialNumber(), (e)->e, (a, b)->b));

        // 取得未出账金额
        SelectNonConfirmAmountInputDTO selectNonConfirmAmountInputDTO = new SelectNonConfirmAmountInputDTO();
        selectNonConfirmAmountInputDTO.setQueryDate(DateUtils.format(queryDate, "yyyy-MM-dd"));
        selectNonConfirmAmountInputDTO.setSnapshootDate(DateUtil.format(
                DateUtil.offsetDay(queryDate, -1), "yyyy-MM-dd"));
        List<SelectNonConfirmAmountOutputDTO> selectNonConfirmAmountOutputDTOList =
                commercialVehicleDataMapper.selectNonConfirmAmount(selectNonConfirmAmountInputDTO);
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
