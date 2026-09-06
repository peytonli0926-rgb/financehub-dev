package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.etl.financial.entity.OutstandingAmountInitEntity;
import com.utfinancing.financehub.etl.financial.mapper.OutstandingAmountInitMapper;
import com.utfinancing.financehub.etl.financial.service.IOutstandingAmountService;
import com.utfinancing.financehub.etl.kingdee.entity.TGLAssistBalanceEntity;
import com.utfinancing.financehub.etl.kingdee.service.ITGLAssistBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class OutstandingAmountServiceImpl extends ServiceImpl<OutstandingAmountInitMapper, OutstandingAmountInitEntity>
        implements IOutstandingAmountService {

    @Resource
    private OutstandingAmountInitMapper outstandingAmountInitMapper;

    @Resource
    private ITGLAssistBalanceService iTGLAssistBalanceService;

    @Override
    public R<Boolean> outstandingAmountInit(String period) {
        outstandingAmountInitMapper.delete(new LambdaQueryWrapper<>());
        List<TGLAssistBalanceEntity> assistBalanceEntities = iTGLAssistBalanceService.outstandingAmountQuery(period);
        List<OutstandingAmountInitEntity> outstandingAmountInitEntities = BeanUtil.copyToList(
                assistBalanceEntities, OutstandingAmountInitEntity.class);
        this.saveBatch(outstandingAmountInitEntities);
        return R.ok(true);
    }
}
