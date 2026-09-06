package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractStructureQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OfflineContractStructureDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractStructureVO;
import com.utfinancing.financehub.engine.finance.entity.OfflineContractStructureEntity;
import com.utfinancing.financehub.engine.finance.mapper.OfflineContractStructureMapper;
import com.utfinancing.financehub.engine.finance.service.IOfflineContractStructureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-18
 * @Description :  OfflineContractStructure服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class OfflineContractStructureServiceImpl extends ServiceImpl<OfflineContractStructureMapper, OfflineContractStructureEntity> implements IOfflineContractStructureService {

    private final OfflineContractStructureMapper offlineContractStructureMapper;

    @Override
    public Long saveOfflineContractStructure(OfflineContractStructureDTO dto) {
        OfflineContractStructureEntity entity = BeanUtil.copyProperties(dto, OfflineContractStructureEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOfflineContractStructure(Long id, OfflineContractStructureDTO dto) {
        OfflineContractStructureEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OfflineContractStructureDTO getOfflineContractStructureDTOById(Long id) {
        OfflineContractStructureEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OfflineContractStructureDTO.class);
    }

    @Override
    public OfflineContractStructureDTO getOfflineContractStructureDTOByContractCode(String contractCode, String contractCodeM) {
        LambdaQueryWrapper<OfflineContractStructureEntity> queryWrapper = Wrappers.<OfflineContractStructureEntity>lambdaQuery();
        queryWrapper.eq(OfflineContractStructureEntity::getContractCode, contractCode);
        if (StringUtils.isNotBlank(contractCodeM)) {
            queryWrapper.eq(OfflineContractStructureEntity::getContractCodeM, contractCodeM);
        } else {
            queryWrapper.eq(OfflineContractStructureEntity::getContractCodeM, "");
        }
        OfflineContractStructureEntity entity = getBaseMapper().selectOne(queryWrapper);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OfflineContractStructureDTO.class);
    }

    @Override
    public IPage<OfflineContractStructureVO> selectPage(OfflineContractStructureQueryDTO queryDTO) {
        LambdaQueryWrapper<OfflineContractStructureEntity> queryWrapper = Wrappers.<OfflineContractStructureEntity>lambdaQuery();
        //这里注入查询条件
        IPage<OfflineContractStructureEntity> entityIPage = offlineContractStructureMapper.selectPage(new Page<OfflineContractStructureEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, OfflineContractStructureVO.class);
    }

    @Override
    public List<OfflineContractStructureVO> selectList(OfflineContractStructureQueryDTO queryDTO) {
        LambdaQueryWrapper<OfflineContractStructureEntity> queryWrapper = Wrappers.<OfflineContractStructureEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(null != queryDTO.getId(), OfflineContractStructureEntity::getId, queryDTO.getId());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getContractCode()), OfflineContractStructureEntity::getContractCode, queryDTO.getContractCode());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryDTO.getContractCodeList()), OfflineContractStructureEntity::getContractCode, queryDTO.getContractCodeList());
        List<OfflineContractStructureEntity> entityIPage = offlineContractStructureMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(entityIPage, OfflineContractStructureVO.class);
    }

}

