package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.SelectAssistFlagsBySceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntrySaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherEntryVO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntryEntity;
import com.utfinancing.financehub.engine.scene.mapper.SceneVoucherEntryMapper;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :  SceneVoucherEntry服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SceneVoucherEntryServiceImpl extends ServiceImpl<SceneVoucherEntryMapper, SceneVoucherEntryEntity> implements ISceneVoucherEntryService {

    private final SceneVoucherEntryMapper sceneVoucherEntryMapper;

    @Override
    public Long saveSceneVoucherEntry(SceneVoucherEntrySaveDTO dto) {
        SceneVoucherEntryEntity entity = BeanUtil.copyProperties(dto, SceneVoucherEntryEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSceneVoucherEntry(Long id, SceneVoucherEntrySaveDTO dto) {
        SceneVoucherEntryEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SceneVoucherEntryDTO getSceneVoucherEntryDTOById(Long id) {
        SceneVoucherEntryEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SceneVoucherEntryDTO.class);
    }

    @Override
    public List<SceneVoucherEntryDTO> listEntryBySceneVoucherId(Long sceneVoucherId) {
        LambdaQueryWrapper<SceneVoucherEntryEntity> queryWrapper = Wrappers.<SceneVoucherEntryEntity>lambdaQuery();
        queryWrapper.eq(SceneVoucherEntryEntity::getSceneVoucherId, sceneVoucherId);
        List<SceneVoucherEntryEntity> entity = list(queryWrapper);
        if (CollectionUtils.isEmpty(entity)) {
            return new ArrayList<>();
        }
        List<SceneVoucherEntryDTO> dtoList = entity.stream().map(e->{
            SceneVoucherEntryDTO dto = BeanUtil.copyProperties(e, SceneVoucherEntryDTO.class);
            dto.setAssistFlags(e.getAssistFlags());
            return dto;
        }).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public void deleteSceneVoucherEntryByIds(List<Long> ids) {
        LambdaUpdateWrapper<SceneVoucherEntryEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
        updateChainWrapper
                .in(SceneVoucherEntryEntity::getId, ids)
                .set(SceneVoucherEntryEntity::getDelFlag, "1");
        this.update(updateChainWrapper);
    }

    @Override
    public IPage<SceneVoucherEntryVO> selectPage(SceneVoucherEntryQueryDTO queryDTO) {
        LambdaQueryWrapper<SceneVoucherEntryEntity> queryWrapper = Wrappers.<SceneVoucherEntryEntity>lambdaQuery();
        //这里注入查询条件
        IPage<SceneVoucherEntryEntity> entityIPage = sceneVoucherEntryMapper.selectPage(new Page<SceneVoucherEntryEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SceneVoucherEntryVO.class);
    }

    @Override
    public List<String> selectDistinctAssistFlagsBySceneCode(String sceneCode) {
        List<String> assistFlagList = sceneVoucherEntryMapper.selectDistinctAssistFlagsBySceneCode(sceneCode);
        if (CollectionUtil.isNotEmpty(assistFlagList)){
            return assistFlagList.stream().map(e->{
                if (StrUtil.isNotBlank(e)){
                    return StrUtil.replace(e, ",", "_");
                }
                return e;
            }).collect(Collectors.toList());
        }
        return assistFlagList;
    }


    public Map<String, List<SelectAssistFlagsBySceneDTO>> selectAllSceneAssistFlags() {
        List<SelectAssistFlagsBySceneDTO> selectAssistFlagsBySceneList = sceneVoucherEntryMapper.selectAssistFlagsByScene();
        if (selectAssistFlagsBySceneList == null || selectAssistFlagsBySceneList.isEmpty()) {
            return new HashMap<>();
        }

        selectAssistFlagsBySceneList.stream().forEach(e-> {
            if (StrUtil.isNotBlank(e.getAssistFlags())){
                e.setAssistFlags(StrUtil.replace(e.getAssistFlags(), ",", "_"));
            }
        });
        Map<String, List<SelectAssistFlagsBySceneDTO>> selectAssistFlagsBySceneMap = selectAssistFlagsBySceneList.stream().
                collect(Collectors.groupingBy(e -> e.getSceneCode()));
        return selectAssistFlagsBySceneMap;
    }
}

