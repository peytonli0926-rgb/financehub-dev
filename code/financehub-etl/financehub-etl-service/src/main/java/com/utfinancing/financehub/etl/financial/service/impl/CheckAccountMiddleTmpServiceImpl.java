package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.CheckAccountMiddleTmpEntity;
import com.utfinancing.financehub.etl.financial.mapper.CheckAccountMiddleTmpMapper;
import com.utfinancing.financehub.etl.financial.service.ICheckAccountMiddleTmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-28
 * @Description :  CheckAccountMiddleTmp服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountMiddleTmpServiceImpl extends ServiceImpl<CheckAccountMiddleTmpMapper, CheckAccountMiddleTmpEntity> implements ICheckAccountMiddleTmpService {

    private final CheckAccountMiddleTmpMapper checkAccountMiddleTmpMapper;

    @Override
    public void clearTableData() {
        checkAccountMiddleTmpMapper.clearTableData();
    }

}

