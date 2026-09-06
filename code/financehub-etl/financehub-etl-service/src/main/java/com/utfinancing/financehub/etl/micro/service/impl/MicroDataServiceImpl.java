package com.utfinancing.financehub.etl.micro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.etl.micro.model.*;
import com.utfinancing.financehub.etl.enums.AccrualMethodEnum;
import com.utfinancing.financehub.etl.enums.Eas2SystemCodeSystemEnum;
import com.utfinancing.financehub.etl.enums.SystemEnum;
import com.utfinancing.financehub.etl.financial.entity.OrgCompanyEntity;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.etl.financial.service.IOrgCompanyService;
import com.utfinancing.financehub.etl.micro.mapper.MicroDataMapper;
import com.utfinancing.financehub.etl.micro.service.IMicroDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MicroDataServiceImpl implements IMicroDataService {

    @Resource
    MicroDataMapper microDataMapper;

    @Resource
    IOrgCompanyService orgCompanyService;

    @Override
    public List<Map<String, Object>> selectDataCommon(String querySql) {
        return microDataMapper.selectDataCommon(querySql);
    }


    /**
     * 取得偿还计划-xirr
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTInit(SelectRepaymentFromXWXTInputDTO dto) {
        return microDataMapper.selectRepaymentFromXWXTInit(dto);
    }

    /**
     * 取得偿还计划-irr
     */
    public List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTGroupInit(SelectRepaymentFromXWXTInputDTO dto) {
        return microDataMapper.selectRepaymentFromXWXTGroupInit(dto);
    }

    /**
     * 取得回笼数据
     */
    public List<SelectReceiveRepaymentDTO> selectReceiveRepayment(SelectRepaymentFromXWXTInputDTO dto) {
        return microDataMapper.selectReceiveRepayment(dto);
    }

    /**
     * 从小微系统取得最新的偿还计划
     */
    public List<RepaymentPlanSaveDTO> getRepaymentPlanFromXWXT(QueryRepaymentPlanDTO params) {
        SelectRepaymentFromXWXTInputDTO dto = new SelectRepaymentFromXWXTInputDTO();
        dto.setContractCode(params.getContractCode());
        List<SelectRepaymentFromXWXTDTO> selectRepaymentFromXWXTDTOList = new ArrayList<>();
        if (AccrualMethodEnum.XIRR.getCode().equals(params.getIncomeProvisionMethod())){
            selectRepaymentFromXWXTDTOList = microDataMapper.selectRepaymentFromXWXT(dto);
        } else {
            selectRepaymentFromXWXTDTOList = microDataMapper.selectRepaymentFromXWXTGroup(dto);
        }
        if (selectRepaymentFromXWXTDTOList == null || selectRepaymentFromXWXTDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        List<RepaymentPlanSaveDTO> result = BeanUtil.copyToList(selectRepaymentFromXWXTDTOList, RepaymentPlanSaveDTO.class);
        return result;
    }

    @Override
    public List<TaReclassificationDetailEntity> getTaReclassificationDetailList(String queryDate, Map<String, String> orgMap) {
        List<TaReclassificationDetailEntity> list = microDataMapper.getTaReclassificationDetailList(queryDate);
        if(CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(i->{
                if(StringUtils.isNotEmpty(i.getOrgId())&&orgMap.containsKey(i.getOrgId())){
                    i.setOrgId(orgMap.get(i.getOrgId()));
                }
                if(StringUtils.isNotEmpty(i.getBankOrgId())&&orgMap.containsKey(i.getBankOrgId())){
                    i.setBankOrgId(orgMap.get(i.getBankOrgId()));
                }
                if(StringUtils.isNotEmpty(i.getEbankSerialNumber())){
                    i.setEbankBatchNo(i.getEbankSerialNumber());
                }
                i.setSystemCode(SystemEnum.XWXT.getCode());
            });
        }
        return list;
    }

    /**
     * 取得业务系统的未确认金额
     */
    public List<SelectNonConfirmAmountOutputDTO> queryNonConfirmAmount(Date queryDate) {
        SelectNonConfirmAmountInputDTO params = new SelectNonConfirmAmountInputDTO();
        params.setQueryDate(DateUtils.format(queryDate, "yyyy-MM-dd"));
        return microDataMapper.selectNonConfirmAmount(params);
    }
}
