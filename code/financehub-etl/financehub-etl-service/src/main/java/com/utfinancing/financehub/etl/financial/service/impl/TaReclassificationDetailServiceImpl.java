package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.etl.financial.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.etl.financial.mapper.TaReclassificationDetailMapper;
import com.utfinancing.financehub.etl.financial.service.ITaReclassificationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :  TaReclassificationDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaReclassificationDetailServiceImpl extends ServiceImpl<TaReclassificationDetailMapper, TaReclassificationDetailEntity> implements ITaReclassificationDetailService {

    private final TaReclassificationDetailMapper taReclassificationDetailMapper;

    @Override
    public List<TaReclassificationDetailEntity> selectDetailDataAndContractInfo(String queryDate) {
        return taReclassificationDetailMapper.selectDetailDataAndContractInfo(queryDate);
    }

    @Override
    public List<TaReclassificationDetailEntity> selectTYPTInfo(String queryDate, String queryDateNextDay, String systemCode) {
        return taReclassificationDetailMapper.selectTYPTInfo(queryDate, queryDateNextDay, systemCode);
    }

}

