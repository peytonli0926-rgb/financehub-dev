package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.RepaymentPlanHisEntity;
import com.utfinancing.financehub.etl.financial.enums.MarginStatusEnum;
import com.utfinancing.financehub.etl.financial.enums.YesOrNoEnum;
import com.utfinancing.financehub.etl.financial.mapper.RepaymentPlanHisMapper;
import com.utfinancing.financehub.etl.financial.model.dto.RepaymentPlanSaveDTO;
import com.utfinancing.financehub.etl.financial.service.IRepaymentPlanHisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-31
 * @Description :  RepaymentPlanHis服务实现类
 * @Modified :
 */
@Service
@Transactional
public class RepaymentPlanHisServiceImpl extends ServiceImpl<RepaymentPlanHisMapper, RepaymentPlanHisEntity>
        implements IRepaymentPlanHisService {

    @Resource
    private RepaymentPlanHisMapper repaymentPlanHisMapper;

    @Override
    public void saveRepaymentPlanHisBatch(List<RepaymentPlanSaveDTO> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            return;
        }
        List<RepaymentPlanHisEntity> hisEntities = BeanUtil.copyToList(dtos, RepaymentPlanHisEntity.class);
        //查最新记录的版本号
        Integer version = repaymentPlanHisMapper.selectNewVersionByContractCode(dtos.get(0).getContractCode());
        if (null == version) {
            version = 1;
        }
        // 版本号+1
        Integer finalVersion = version;
        hisEntities.forEach(e -> {
            e.setVersion(finalVersion + 1);
            e.setProcessStatus(MarginStatusEnum.PASS.getCode());
            e.setManualChangeMark(YesOrNoEnum.NO.getCode());
        });
        this.saveBatch(hisEntities);
    }

}

