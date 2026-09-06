package com.utfinancing.financehub.engine.claim.service.impl;

import com.utfinancing.financehub.engine.claim.entity.AssetsExpenseEntity;
import com.utfinancing.financehub.engine.claim.mapper.AssetsExpenseMapper;
import com.utfinancing.financehub.engine.claim.service.IAssetsExpenseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : robjiang
 * @Date : Create in 2024-09-11
 * @Description :  AssetsExpense服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AssetsExpenseServiceImpl extends ServiceImpl<AssetsExpenseMapper, AssetsExpenseEntity>
        implements IAssetsExpenseService {

    private final AssetsExpenseMapper assetsExpenseMapper;

}

