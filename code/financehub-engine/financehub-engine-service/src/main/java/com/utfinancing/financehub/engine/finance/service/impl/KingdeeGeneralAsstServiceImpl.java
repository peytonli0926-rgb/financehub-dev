package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.entity.KingdeeGeneralAsstEntity;
import com.utfinancing.financehub.engine.finance.entity.KingdeeGeneralAsstEntity;
import com.utfinancing.financehub.engine.finance.mapper.KingdeeGeneralAsstMapper;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeCostcenterDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeGeneralAsstDTO;
import com.utfinancing.financehub.engine.finance.model.dto.KingdeeOptionQueryDTO;
import com.utfinancing.financehub.engine.finance.service.IKingdeeGeneralAsstService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeeGeneralAsst服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeGeneralAsstServiceImpl extends ServiceImpl<KingdeeGeneralAsstMapper, KingdeeGeneralAsstEntity> implements IKingdeeGeneralAsstService {

    private final KingdeeGeneralAsstMapper kingdeeGeneralAsstMapper;


    @Override
    public IPage<KingdeeGeneralAsstDTO> selectPage(KingdeeOptionQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeGeneralAsstEntity> queryWrapper = Wrappers.<KingdeeGeneralAsstEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getAsstType()), KingdeeGeneralAsstEntity::getAsstType, queryDTO.getAsstType())
                .and(StrUtil.isNotBlank(queryDTO.getSearchKey()), i-> i.like(KingdeeGeneralAsstEntity::getCode, queryDTO.getSearchKey())
                    .or()
                    .like(KingdeeGeneralAsstEntity::getName, queryDTO.getSearchKey()));
        IPage<KingdeeGeneralAsstEntity> entityIPage = kingdeeGeneralAsstMapper.selectPage(new Page<KingdeeGeneralAsstEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeGeneralAsstDTO.class);
    }
}

