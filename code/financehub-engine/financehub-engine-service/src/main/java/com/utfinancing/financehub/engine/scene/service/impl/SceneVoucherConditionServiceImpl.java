package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntryEntity;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionSaveDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherConditionVO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherConditionEntity;
import com.utfinancing.financehub.engine.scene.mapper.SceneVoucherConditionMapper;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :  SceneVoucherCondition服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SceneVoucherConditionServiceImpl extends ServiceImpl<SceneVoucherConditionMapper, SceneVoucherConditionEntity> implements ISceneVoucherConditionService {

    private final SceneVoucherConditionMapper sceneVoucherConditionMapper;

    @Override
    public Long saveSceneVoucherCondition(SceneVoucherConditionSaveDTO dto) {
        SceneVoucherConditionEntity entity = BeanUtil.copyProperties(dto, SceneVoucherConditionEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSceneVoucherCondition(Long id, SceneVoucherConditionSaveDTO dto) {
        SceneVoucherConditionEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SceneVoucherConditionDTO getSceneVoucherConditionDTOById(Long id) {
        SceneVoucherConditionEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SceneVoucherConditionDTO.class);
    }

    @Override
    public List<SceneVoucherConditionDTO> getSceneVoucherConditionDTOsByEntryId(Long id) {
        LambdaQueryWrapper<SceneVoucherConditionEntity> queryWrapper = Wrappers.<SceneVoucherConditionEntity>lambdaQuery();
        queryWrapper.eq(SceneVoucherConditionEntity::getSceneVoucherEntryId,id);
        List<SceneVoucherConditionEntity> entity = list(queryWrapper);
        if (CollectionUtils.isEmpty(entity)) {
            return new ArrayList<>();
        }
        return ListBeanUtil.copyList(entity, SceneVoucherConditionDTO.class);
    }

    @Override
    public void deleteSceneVoucherConditionBySceneIds(List<Long> ids) {
            LambdaUpdateWrapper<SceneVoucherConditionEntity> updateChainWrapper = new LambdaUpdateWrapper<>();
            updateChainWrapper
                    .in(SceneVoucherConditionEntity::getSceneVoucherEntryId, ids)
                    .set(SceneVoucherConditionEntity::getDelFlag, "1");
            this.update(updateChainWrapper);
    }

    @Override
    public IPage<SceneVoucherConditionVO> selectPage(SceneVoucherConditionQueryDTO queryDTO) {
        LambdaQueryWrapper<SceneVoucherConditionEntity> queryWrapper = Wrappers.<SceneVoucherConditionEntity>lambdaQuery();
        //这里注入查询条件
        IPage<SceneVoucherConditionEntity> entityIPage = sceneVoucherConditionMapper.selectPage(new Page<SceneVoucherConditionEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SceneVoucherConditionVO.class);
    }

}

