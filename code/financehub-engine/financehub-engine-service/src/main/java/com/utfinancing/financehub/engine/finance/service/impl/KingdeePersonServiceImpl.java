package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.entity.KingdeePersonEntity;
import com.utfinancing.financehub.engine.finance.mapper.KingdeePersonMapper;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;
import com.utfinancing.financehub.engine.finance.service.IKingdeePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeePerson服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeePersonServiceImpl extends ServiceImpl<KingdeePersonMapper, KingdeePersonEntity> implements IKingdeePersonService {

    private final KingdeePersonMapper kingdeePersonMapper;

    @Override
    public IPage<KingdeePersonDTO> selectPage(KingdeeOptionQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeePersonEntity> queryWrapper = Wrappers.<KingdeePersonEntity>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getSearchKey()), KingdeePersonEntity::getCode, queryDTO.getSearchKey())
                .or()
                .like(StrUtil.isNotBlank(queryDTO.getSearchKey()), KingdeePersonEntity::getName, queryDTO.getSearchKey());
        IPage<KingdeePersonEntity> entityIPage = kingdeePersonMapper.selectPage(new Page<KingdeePersonEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeePersonDTO.class);
    }

}

