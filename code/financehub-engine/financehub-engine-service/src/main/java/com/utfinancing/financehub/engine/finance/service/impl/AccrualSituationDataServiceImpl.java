package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.AccrualSituationDataEntity;
import com.utfinancing.financehub.engine.finance.mapper.AccrualSituationDataMapper;
import com.utfinancing.financehub.engine.finance.service.IAccrualSituationDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : jnc
 * @Date : Create in 2024-06-30
 * @Description :  AccrualSituationData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccrualSituationDataServiceImpl extends ServiceImpl<AccrualSituationDataMapper, AccrualSituationDataEntity> implements IAccrualSituationDataService {

    private final AccrualSituationDataMapper accrualSituationDataMapper;

    @Override
    public AccrualSituationDataEntity getDataByVoucherId(Long voucherId, String fillType) {
        AccrualSituationDataEntity entity = accrualSituationDataMapper.getDataByVoucherId(voucherId, fillType);
        return entity;
    }
}

