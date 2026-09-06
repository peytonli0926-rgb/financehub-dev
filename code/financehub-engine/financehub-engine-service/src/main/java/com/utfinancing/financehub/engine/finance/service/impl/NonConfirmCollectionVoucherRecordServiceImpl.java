package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionVoucherRecordEntity;
import com.utfinancing.financehub.engine.finance.mapper.NonConfirmCollectionVoucherRecordMapper;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionVoucherRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2024-04-25
 * @Description :  NonConfirmCollectionVoucherRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class NonConfirmCollectionVoucherRecordServiceImpl extends ServiceImpl<NonConfirmCollectionVoucherRecordMapper,
        NonConfirmCollectionVoucherRecordEntity> implements INonConfirmCollectionVoucherRecordService {

    private final NonConfirmCollectionVoucherRecordMapper nonConfirmCollectionVoucherRecordMapper;

    /**
     * 根据明细ID查询凭证记录
     */
    public List<NonConfirmCollectionVoucherRecordEntity> selectByDetailId(String detailId) {
        if (StringUtils.isEmpty(detailId)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<NonConfirmCollectionVoucherRecordEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(NonConfirmCollectionVoucherRecordEntity::getDetailId, detailId);
        return nonConfirmCollectionVoucherRecordMapper.selectList(wrapper);
    }

}

