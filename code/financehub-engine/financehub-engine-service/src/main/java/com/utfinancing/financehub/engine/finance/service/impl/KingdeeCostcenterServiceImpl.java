package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.entity.KingdeeCostcenterEntity;
import com.utfinancing.financehub.engine.finance.entity.KingdeeCostcenterEntity;
import com.utfinancing.financehub.engine.finance.mapper.KingdeeCostcenterMapper;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeCostcenterDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;
import com.utfinancing.financehub.engine.finance.service.IKingdeeCostcenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeeCostcenter服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeCostcenterServiceImpl extends ServiceImpl<KingdeeCostcenterMapper, KingdeeCostcenterEntity> implements IKingdeeCostcenterService {

    private final KingdeeCostcenterMapper kingdeeCostcenterMapper;


    @Override
    public IPage<KingdeeCostcenterDTO> selectPage(KingdeeOptionQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeCostcenterEntity> queryWrapper = Wrappers.<KingdeeCostcenterEntity>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getSearchKey()), KingdeeCostcenterEntity::getCode, queryDTO.getSearchKey())
                .or()
                .like(StrUtil.isNotBlank(queryDTO.getSearchKey()), KingdeeCostcenterEntity::getName, queryDTO.getSearchKey());
        IPage<KingdeeCostcenterEntity> entityIPage = kingdeeCostcenterMapper.selectPage(new Page<KingdeeCostcenterEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeCostcenterDTO.class);
    }

    @Override
    public List<KingdeeCostcenterDTO> selectAll() {
        LambdaQueryWrapper<KingdeeCostcenterEntity> queryWrapper = Wrappers.<KingdeeCostcenterEntity>lambdaQuery();
        queryWrapper.select(KingdeeCostcenterEntity::getCode,KingdeeCostcenterEntity::getName);
        List<KingdeeCostcenterEntity> entityList = list(queryWrapper).stream().distinct().collect(Collectors.toList());
        return ListBeanUtil.copyList(entityList,KingdeeCostcenterDTO.class);
    }
}

