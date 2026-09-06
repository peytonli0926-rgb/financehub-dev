package com.utfinancing.financehub.etl.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.utfinancing.financehub.etl.enums.AccrualMethodEnum;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.platform.mapper.PlatformDataMapper;
import com.utfinancing.financehub.etl.platform.model.*;
import com.utfinancing.financehub.etl.platform.service.IPlatformDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PlatformDataServiceImpl implements IPlatformDataService {

    @Resource
    PlatformDataMapper platformDataMapper;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return platformDataMapper.selectDataCommon(querySql);
    }

    /**
     * 取得偿还计划-xirr-期初数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTInit(SelectRepaymentFromTYPTInputDTO dto) {
        return platformDataMapper.selectRepaymentFromTYPTInit(dto);
    }

    /**
     * 取得偿还计划-irr-期初数据
     */
    public List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTGroupInit(SelectRepaymentFromTYPTInputDTO dto) {
        return platformDataMapper.selectRepaymentFromTYPTGroupInit(dto);
    }

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromTYPTInputDTO dto) {
        return platformDataMapper.selectReceiveRepayment(dto);
    }

    /**
     * 从统一平台取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromTYPT(QueryRepaymentPlanDTO params) {
        SelectRepaymentFromTYPTInputDTO dto = new SelectRepaymentFromTYPTInputDTO();
        dto.setContractCode(params.getContractCode());
        List<SelectRepaymentFromTYPTDTO> selectRepaymentFromTYPTDTOList = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(params.getIncomeProvisionMethod())){
            selectRepaymentFromTYPTDTOList = platformDataMapper.selectRepaymentFromTYPT(dto);
        } else {
            selectRepaymentFromTYPTDTOList = platformDataMapper.selectRepaymentFromTYPTGroup(dto);
        }
        if (selectRepaymentFromTYPTDTOList == null || selectRepaymentFromTYPTDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanSaveDTO> result = BeanUtil.copyToList(selectRepaymentFromTYPTDTOList, RepaymentPlanSaveDTO.class);
        return result;
    }


    @Override
    public List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate, Map<String, String> orgMap) {
        List<TaReclassificationDetailEntity> list = platformDataMapper.getTaReclassificationDetailList(queryDate);
        if(CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(i->{
                i.setSystemCode(SystemEnum.TYPT.getCode());
                i.setEbankBatchNo(i.getEbankSerialNumber());
            });
        }

        return list;
    }

    /**
     * 查询ta余额
     */
    public List<SelectTaAmountDTO> queryTaAmount() {
        return platformDataMapper.selectTaAmount();
    }

    /**
     * 取得业务系统的未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> queryNonConfirmAmount(Date queryDate) {
        SelectNonConfirmAmountInputDTO params = new SelectNonConfirmAmountInputDTO();
        params.setQueryDate(DateUtils.format(queryDate, "yyyy-MM-dd"));
        return platformDataMapper.selectNonConfirmAmount(params);
    }
}
