package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.entity.VoucherAmountInitEntity;
import com.utfinancing.financehub.engine.finance.mapper.VoucherAmountInitMapper;
import com.utfinancing.financehub.engine.finance.model.dto.TGLVoucherInitDTO;
import com.utfinancing.financehub.engine.finance.service.IVoucherAmountInitService;
import com.utfinancing.financehub.etl.api.KingdeeDataSyncFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-01-10
 * @Description :  VoucherAmountInit服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class VoucherAmountInitServiceImpl extends ServiceImpl<VoucherAmountInitMapper, VoucherAmountInitEntity>
        implements IVoucherAmountInitService {

    @Resource
    private final VoucherAmountInitMapper voucherAmountInitMapper;

    @Resource
    private KingdeeDataSyncFacade kingdeeDataSyncFacade;

    /**
     * 根据合同查询收益数据列表
     */
    public Map<String, List<VoucherAmountInitEntity>> getVoucherAmountByContract(Set<String> contractCodeList) {
        LambdaQueryWrapper<VoucherAmountInitEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(VoucherAmountInitEntity :: getContractCode, contractCodeList);
        List<VoucherAmountInitEntity> voucherAmountInitEntityList = voucherAmountInitMapper.selectList(wrapper);

        if (voucherAmountInitEntityList != null && !voucherAmountInitEntityList.isEmpty()) {
            Map<String, List<VoucherAmountInitEntity>> result = voucherAmountInitEntityList.stream().collect(
                    Collectors.groupingBy(e -> e.getContractCode()));
            return result;
        } else {
            return new HashMap<>();
        }
    }

    /**
     * 取得金蝶每月的现金流数据
     */
    public Map<String, BigDecimal> getVoucherAmountInit(String startPeriod, String endPeriod, String contractCode) {
        com.utfinancing.financehub.etl.model.dto.TGLVoucherInitDTO tglVoucherInitDTO =
                new com.utfinancing.financehub.etl.model.dto.TGLVoucherInitDTO();
        tglVoucherInitDTO.setStartPeriod(startPeriod);
        tglVoucherInitDTO.setEndPeriod(endPeriod);
        tglVoucherInitDTO.setContractCode(contractCode);
        log.info("kingdeeDataSyncFacade.voucherAmountSync input params:" + JSON.toJSONString(tglVoucherInitDTO));
        R<List<com.utfinancing.financehub.etl.model.dto.TGLVoucherInitDTO>> tglVoucherInitR =
                kingdeeDataSyncFacade.voucherAmountSync(tglVoucherInitDTO);

        if (tglVoucherInitR.getData() != null && !tglVoucherInitR.getData().isEmpty()) {
            List<VoucherAmountInitEntity> voucherAmountInitList = BeanUtil.copyToList(
                    tglVoucherInitR.getData(), VoucherAmountInitEntity.class);
            Map<String, BigDecimal> voucherAmountInitMap = voucherAmountInitList.stream().collect(
                    Collectors.toMap(VoucherAmountInitEntity::getPeriod, VoucherAmountInitEntity::getDtAmount, (a, b) -> a.add(b)));
            return voucherAmountInitMap;
        } else {
            return new HashMap<>();
        }
    }

    public List<VoucherAmountInitEntity> getVoucherAmountByPeriod(String period, String contractCode) {
        TGLVoucherInitDTO result = new TGLVoucherInitDTO();
        LambdaQueryWrapper<VoucherAmountInitEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VoucherAmountInitEntity :: getContractCode, contractCode);
        wrapper.eq(VoucherAmountInitEntity :: getPeriod, period);
        List<VoucherAmountInitEntity> voucherAmountInitEntityList = voucherAmountInitMapper.selectList(wrapper);
        return voucherAmountInitEntityList;
    }
}

