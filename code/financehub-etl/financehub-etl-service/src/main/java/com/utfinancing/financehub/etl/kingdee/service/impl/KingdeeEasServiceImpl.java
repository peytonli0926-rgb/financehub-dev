package com.utfinancing.financehub.etl.kingdee.service.impl;

import com.utfinancing.financehub.etl.financial.entity.KingdeeHybVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeHybVoucherVO;
import com.utfinancing.financehub.etl.kingdee.mapper.KingdeeEasMapper;
import com.utfinancing.financehub.etl.kingdee.model.dto.OptionDTO;
import com.utfinancing.financehub.etl.kingdee.service.IKingdeeEasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 23/01/2024
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeEasServiceImpl implements IKingdeeEasService {

    private final KingdeeEasMapper kingdeeEasMapper;


    @Override
    public Integer getCurrentPeriodCode(String orgId) {
        return kingdeeEasMapper.getCurrentPeriodCode(orgId);
    }

    @Override
    public List<OptionDTO> selectAllPerson() {
        return kingdeeEasMapper.selectAllPerson();
    }

    @Override
    public List<OptionDTO> selectAllBank() {
        return kingdeeEasMapper.selectAllBank();
    }

    @Override
    public List<OptionDTO> selectAllCostCenter() {
        return kingdeeEasMapper.selectAllCostCenter();
    }

    @Override
    public List<OptionDTO> selectAllGeneralAsst() {
        return kingdeeEasMapper.selectAllGeneralAsst();
    }

    @Override
    public List<OptionDTO> selectContractData() {
        return kingdeeEasMapper.selectContractData();
    }

    @Override
    public List<Map<String, String>> getCurrentPeriodCodeAll() {
        List<Map<String, String>> map = kingdeeEasMapper.getCurrentPeriodCodeAll();
        return map;
    }

    @Override
    public List<KingdeeHybVoucherEntity> selectKingdeeHybVoucher(String periodCode, String voucherDate) {
        return kingdeeEasMapper.selectKingdeeHybVoucher(periodCode,voucherDate);
    }




}
