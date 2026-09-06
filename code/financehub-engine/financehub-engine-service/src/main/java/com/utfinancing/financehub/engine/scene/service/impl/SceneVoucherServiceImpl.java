package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.scene.entity.SceneEntity;
import com.utfinancing.financehub.engine.scene.mapper.SceneMapper;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherVO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntity;
import com.utfinancing.financehub.engine.scene.mapper.SceneVoucherMapper;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lettuce.core.codec.RedisCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :  SceneVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SceneVoucherServiceImpl extends ServiceImpl<SceneVoucherMapper, SceneVoucherEntity> implements ISceneVoucherService {

    private final SceneVoucherMapper sceneVoucherMapper;
    private final RedisService redisService;
    private final SceneMapper sceneMapper;

    @Override
    public Long saveSceneVoucher(SceneVoucherSaveDTO dto) {
        SceneVoucherEntity entity = BeanUtil.copyProperties(dto, SceneVoucherEntity.class);
        this.save(entity);
        deleteRedis(dto.getSceneId());
        return entity.getId();
    }

    @Override
    public Long updateSceneVoucher(Long id, SceneVoucherSaveDTO dto) {
        SceneVoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        deleteRedis(dto.getSceneId());
        return id;
    }

    @Override
    public SceneVoucherDTO getSceneVoucherDTOById(Long id) {
        SceneVoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SceneVoucherDTO.class);
    }


    @Override
    public List<SceneVoucherDTO> listSceneVoucherDTOBySceneId(Long sceneId) {
        LambdaQueryWrapper<SceneVoucherEntity> queryWrapper = Wrappers.<SceneVoucherEntity>lambdaQuery();
        queryWrapper.eq(SceneVoucherEntity::getSceneId,sceneId);
        queryWrapper.orderByAsc(SceneVoucherEntity::getSceneVoucherName);
        List<SceneVoucherEntity> entityList = this.list(queryWrapper);
        return ListBeanUtil.copyList(entityList, SceneVoucherDTO.class);
    }

    @Override
    public IPage<SceneVoucherVO> selectPage(SceneVoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<SceneVoucherEntity> queryWrapper = Wrappers.<SceneVoucherEntity>lambdaQuery();
        //这里注入查询条件
        IPage<SceneVoucherEntity> entityIPage = sceneVoucherMapper.selectPage(new Page<SceneVoucherEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SceneVoucherVO.class);
    }

    public void deleteRedis(Long sceneId) {
        SceneEntity sceneEntity = sceneMapper.selectById(sceneId);
        if (ObjectUtil.isNull(sceneEntity)) {
            return;
        }
        redisService.deleteObject(String.format(RedisConstant.V_SCENE_CODE_RULE,sceneEntity.getSceneCode()));
    }

}

