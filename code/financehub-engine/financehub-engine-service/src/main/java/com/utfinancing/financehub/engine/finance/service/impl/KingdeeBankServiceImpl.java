package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.entity.KingdeeBankEntity;
import com.utfinancing.financehub.engine.finance.entity.KingdeeBankEntity;
import com.utfinancing.financehub.engine.finance.mapper.KingdeeBankMapper;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeBankDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeePersonDTO;
import com.utfinancing.financehub.engine.finance.service.IKingdeeBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeeBank服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeBankServiceImpl extends ServiceImpl<KingdeeBankMapper, KingdeeBankEntity> implements IKingdeeBankService {

    private final KingdeeBankMapper kingdeeBankMapper;


    @Override
    public IPage<KingdeeBankDTO> selectPage(KingdeeOptionQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeBankEntity> queryWrapper = Wrappers.<KingdeeBankEntity>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getSearchKey()), KingdeeBankEntity::getCode, queryDTO.getSearchKey())
                .or()
                .like(StrUtil.isNotBlank(queryDTO.getSearchKey()), KingdeeBankEntity::getName, queryDTO.getSearchKey());
        IPage<KingdeeBankEntity> entityIPage = kingdeeBankMapper.selectPage(new Page<KingdeeBankEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeBankDTO.class);
    }
}

