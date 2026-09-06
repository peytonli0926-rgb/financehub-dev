package com.utfinancing.financehub.etl.kingdee.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.kingdee.entity.TGLAssistBalanceEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TGLAssistBalanceMapper;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGLVoucherInitDTO;
import com.utfinancing.financehub.etl.kingdee.service.ITGLAssistBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TGLAssistBalanceServiceImpl extends ServiceImpl<TGLAssistBalanceMapper, TGLAssistBalanceEntity>
        implements ITGLAssistBalanceService {

    private final TGLAssistBalanceMapper tGLAssistBalanceMapper;

    /**
     * 未偿还金额列表查询
     *
     * @param period
     */
    @Override
    public List<TGLAssistBalanceEntity> outstandingAmountQuery(String period) {
        return tGLAssistBalanceMapper.selectOutstandingData(period);
    }

    public List<TGLVoucherInitDTO> voucherInitQuery(TGLVoucherInitDTO dto) {
        return tGLAssistBalanceMapper.selectVoucherDataInit(dto);
    }
}
