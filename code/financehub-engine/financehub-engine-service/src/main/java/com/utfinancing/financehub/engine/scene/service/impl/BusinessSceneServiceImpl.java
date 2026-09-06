package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.scene.entity.BusinessSceneEntity;
import com.utfinancing.financehub.engine.scene.mapper.BusinessSceneMapper;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessSceneVO;
import com.utfinancing.financehub.engine.scene.service.IBusinessSceneService;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description :  BusinessScene服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BusinessSceneServiceImpl extends ServiceImpl<BusinessSceneMapper, BusinessSceneEntity> implements IBusinessSceneService {

    private final BusinessSceneMapper businessSceneMapper;
    private final IBusinessService businessService;

    @Override
    public Long saveBusinessScene(BusinessSceneSaveDTO dto) {
        BusinessSceneEntity entity = BeanUtil.copyProperties(dto, BusinessSceneEntity.class);
        this.save(entity);
        // 更新业务配置关联数量
        businessService.addSceneCountByBusinessId(dto.getBusinessId());
        return entity.getId();
    }

    @Override
    public Boolean removeByBusinessId(Long id) {
        BusinessSceneEntity entity = this.getById(id);
        // 逻辑删除
        boolean isRemove = this.removeById(id);
        // 更新业务配置关联数量
        businessService.substractSceneCountByBusinessId(entity.getBusinessId());
        return isRemove;
    }

    @Override
    public Long updateBusinessScene(Long id, BusinessSceneDTO dto) {
        BusinessSceneEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public BusinessSceneDTO getBusinessSceneDTOById(Long id) {
        BusinessSceneEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BusinessSceneDTO.class);
    }

    @Override
    public List<BusinessSceneDTO> getBusinessSceneDTOsByBusinessId(Long id) {
        LambdaQueryWrapper<BusinessSceneEntity> queryWrapper = Wrappers.<BusinessSceneEntity>lambdaQuery();
        queryWrapper.eq(BusinessSceneEntity::getBusinessId, id);
        List<BusinessSceneEntity> entity = list(queryWrapper);
        if (CollectionUtils.isEmpty(entity)) {
            return new ArrayList<>();
        }
        return ListBeanUtil.copyList(entity, BusinessSceneDTO.class);
    }

    @Override
    public IPage<BusinessSceneVO> selectPage(BusinessSceneQueryDTO queryDTO) {
        LambdaQueryWrapper<BusinessSceneEntity> queryWrapper = Wrappers.<BusinessSceneEntity>lambdaQuery();
        //这里注入查询条件
        IPage<BusinessSceneEntity> entityIPage = businessSceneMapper.selectPage(new Page<BusinessSceneEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BusinessSceneVO.class);
    }

}

