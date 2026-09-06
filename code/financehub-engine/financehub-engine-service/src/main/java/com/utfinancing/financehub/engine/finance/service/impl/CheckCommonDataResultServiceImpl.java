package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.entity.CheckCommonDataResultEntity;
import com.utfinancing.financehub.engine.finance.mapper.CheckCommonDataResultMapper;
import com.utfinancing.financehub.engine.finance.service.ICheckCommonDataResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : jnc
 * @Date : Create in 2024-04-02
 * @Description :  CheckCommonDataResult服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CheckCommonDataResultServiceImpl extends ServiceImpl<CheckCommonDataResultMapper, CheckCommonDataResultEntity> implements ICheckCommonDataResultService {

    private final CheckCommonDataResultMapper checkCommonDataResultMapper;

}

