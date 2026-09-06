package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.scene.model.dto.AccountPeriodDTO;
import com.utfinancing.financehub.engine.scene.entity.AccountPeriodEntity;
import com.utfinancing.financehub.engine.scene.mapper.AccountPeriodMapper;
import com.utfinancing.financehub.engine.scene.service.IAccountPeriodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-28
 * @Description :  AccountPeriod服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccountPeriodServiceImpl extends ServiceImpl<AccountPeriodMapper, AccountPeriodEntity> implements IAccountPeriodService {

    private final AccountPeriodMapper accountPeriodMapper;


    @Override
    public List<AccountPeriodDTO> queryAllAccountPeriod() {
        List<AccountPeriodEntity> entityList = this.list(Wrappers.<AccountPeriodEntity>lambdaQuery().orderByAsc(AccountPeriodEntity::getPeriodCode));
        return ListBeanUtil.copyList(entityList,AccountPeriodDTO.class);
    }

}

