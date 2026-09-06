package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.AssistFlagEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.scene.entity.*;
import com.utfinancing.financehub.engine.scene.mapper.SceneMapper;
import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVO;
import com.utfinancing.financehub.engine.scene.service.ISceneService;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherConditionService;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherEntryService;
import com.utfinancing.financehub.engine.scene.service.ISceneVoucherService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :  Scene服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SceneServiceImpl extends ServiceImpl<SceneMapper, SceneEntity> implements ISceneService {

    private final SceneMapper sceneMapper;
    private final ISceneVoucherService sceneVoucherService;
    private final ISceneVoucherEntryService sceneVoucherEntryService;
    private final ISceneVoucherConditionService sceneVoucherConditionService;
    private final RedisService redisService;

    @Override
    public Long saveScene(SceneSaveDTO dto) {
        //check 编码 名称唯一
        if (getBaseMapper().selectCount(Wrappers.<SceneEntity>lambdaQuery().eq(SceneEntity::getSceneCode, dto.getSceneCode())) > 0) {
            throw new ServiceException("编码重复");
        }
        if (getBaseMapper().selectCount(Wrappers.<SceneEntity>lambdaQuery().eq(SceneEntity::getSceneName, dto.getSceneName())) > 0) {
            throw new ServiceException("名称重复");
        }
        SceneEntity entity = BeanUtil.copyProperties(dto, SceneEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateScene(Long id, SceneSaveDTO dto) {
        SceneEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        //删除凭证Redis缓存
        redisService.deleteObject(String.format(RedisConstant.V_SCENE_CODE_KEY,entity.getSceneCode()));
        return id;
    }

    @Override
    public SceneDTO getSceneDTOById(Long id) {
        SceneEntity entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, SceneDTO.class);
    }

    @Override
    public SceneDTO getSceneDTOByCode(String sceneCode) {
        SceneEntity entity = this.getOne(Wrappers.<SceneEntity>lambdaQuery()
                .eq(SceneEntity::getSceneCode, sceneCode));
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, SceneDTO.class);
    }

    @Override
    public IPage<SceneVO> selectPage(SceneQueryDTO queryDTO) {
        LambdaQueryWrapper<SceneEntity> queryWrapper = Wrappers.<SceneEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getSceneCode()), SceneEntity::getSceneCode, queryDTO.getSceneCode());
        queryWrapper.eq(ObjUtil.isNotNull(queryDTO.getScenePeriod()), SceneEntity::getScenePeriod, queryDTO.getScenePeriod());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getEnableFlag()), SceneEntity::getEnableFlag, queryDTO.getEnableFlag());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getSceneName()), SceneEntity::getSceneName, queryDTO.getSceneName());
        queryWrapper.orderByDesc(SceneEntity::getUpdateTime);
        //这里注入查询条件
        IPage<SceneEntity> entityIPage = sceneMapper.selectPage(new Page<SceneEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SceneVO.class);
    }

    @Override
    public List<SceneVO> selectAll(SceneQueryDTO queryDTO) {
        LambdaQueryWrapper<SceneEntity> queryWrapper = Wrappers.<SceneEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getSceneCode()), SceneEntity::getSceneCode, queryDTO.getSceneCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getSceneName()), SceneEntity::getSceneName, queryDTO.getSceneName());
        List<SceneEntity> sceneEntities = sceneMapper.selectList(queryWrapper);
        return ListBeanUtil.copyList(sceneEntities, SceneVO.class);
    }

    @Override
    public List<SceneRuleDTO> listRuleListBySceneId(Long sceneId) {
        List<SceneVoucherDTO> voucherDTOList = sceneVoucherService.listSceneVoucherDTOBySceneId(sceneId);
        if (CollectionUtils.isEmpty(voucherDTOList)) {
            return null;
        }
        List<SceneRuleDTO> ruleList = new ArrayList<>();
        for (SceneVoucherDTO sceneVoucherDTO: voucherDTOList){
            SceneRuleDTO ruleDTO = BeanUtil.copyProperties(sceneVoucherDTO, SceneRuleDTO.class);
            ruleList.add(ruleDTO);
            List<SceneVoucherEntryDTO> sceneVoucherEntryDTOList = sceneVoucherEntryService.listEntryBySceneVoucherId(sceneVoucherDTO.getId());
            if (CollectionUtils.isEmpty(sceneVoucherEntryDTOList)){
                continue;
            }
            ruleDTO.setEntryList(sceneVoucherEntryDTOList);
            for (SceneVoucherEntryDTO entryDTO: sceneVoucherEntryDTOList){
                List<SceneVoucherConditionDTO> conditionDTOs = sceneVoucherConditionService.getSceneVoucherConditionDTOsByEntryId(entryDTO.getId());
                if (CollectionUtils.isNotEmpty(conditionDTOs)) {
                    entryDTO.setConditionList(conditionDTOs);
                }
            }
        }
        return ruleList;
    }

    @Override
    public SceneRuleConfigDTO getSceneRuleConfigById(Long sceneId) {
        SceneEntity entity = this.getById(sceneId);
        if (entity == null) {
            return null;
        }
        SceneRuleConfigDTO sceneRuleConfigDTO = new SceneRuleConfigDTO();
        sceneRuleConfigDTO.setSceneId(entity.getId());
        sceneRuleConfigDTO.setSceneName(entity.getSceneName());
        sceneRuleConfigDTO.setVersion(entity.getVersion());

        List<SceneVoucherDTO> voucherDTOList = sceneVoucherService.listSceneVoucherDTOBySceneId(sceneId);
        if (CollectionUtils.isEmpty(voucherDTOList)) {
            return sceneRuleConfigDTO;
        }
        List<SceneRuleDTO> ruleList = new ArrayList<>();
        sceneRuleConfigDTO.setRuleList(ruleList);
        for (SceneVoucherDTO sceneVoucherDTO: voucherDTOList){
            SceneRuleDTO ruleDTO = BeanUtil.copyProperties(sceneVoucherDTO, SceneRuleDTO.class);
            ruleDTO.setSceneName(entity.getSceneName());
            ruleList.add(ruleDTO);
            List<SceneVoucherEntryDTO> sceneVoucherEntryDTOList = sceneVoucherEntryService.listEntryBySceneVoucherId(sceneVoucherDTO.getId());
            if (CollectionUtils.isEmpty(sceneVoucherEntryDTOList)){
                continue;
            }
            ruleDTO.setEntryList(sceneVoucherEntryDTOList);
            for (SceneVoucherEntryDTO entryDTO: sceneVoucherEntryDTOList){
                List<SceneVoucherConditionDTO> conditionDTOs = sceneVoucherConditionService.getSceneVoucherConditionDTOsByEntryId(entryDTO.getId());
                if (CollectionUtils.isNotEmpty(conditionDTOs)) {
                    entryDTO.setConditionList(conditionDTOs);
                }
            }
        }
        return sceneRuleConfigDTO;
    }

    @Override
    public List<SceneRuleDTO> getSceneRuleDTOByCode(String sceneCode) {
        SceneDTO dto = getSceneDTOByCode(sceneCode);
        if (dto == null){
            throw new ServiceException(StrUtil.format("场景[{}]不存在", sceneCode));
        }
        return listRuleListBySceneId(dto.getId());
    }


    @Override
    public Boolean saveSceneRuleConfig(SceneRuleConfigSaveDTO saveDTO) {
        Long sceneId = saveDTO.getSceneId();
        //更新版本(乐观锁)
        SceneEntity sceneEntity = new SceneEntity();
        sceneEntity.setId(sceneId);
        sceneEntity.setVersion(saveDTO.getVersion());
        Boolean updateResult = sceneEntity.updateById();
        if (!updateResult){
            return false;
        }
        //删除旧数据
        removeOldRuleConfig(sceneId);
        if (CollectionUtils.isEmpty(saveDTO.getRuleList())){
            return Boolean.TRUE;
        }
        List<SceneVoucherEntity> sceneVoucherEntityList = new ArrayList<>();
        List<SceneVoucherEntryEntity> sceneVoucherEntryEntityList = new ArrayList<>();
        List<SceneVoucherConditionEntity> sceneVoucherConditionEntityList = new ArrayList<>();
        for (SceneRuleSaveDTO ruleDTO: saveDTO.getRuleList()){
            SceneVoucherEntity voucherEntity = BeanUtil.copyProperties(ruleDTO, SceneVoucherEntity.class);
            voucherEntity.setSceneId(sceneId);
            voucherEntity.setId(IdWorker.getId());
            sceneVoucherEntityList.add(voucherEntity);
            if (CollectionUtils.isEmpty(ruleDTO.getEntryList())){
                continue;
            }
            for (SceneVoucherEntrySaveDTO entryDTO: ruleDTO.getEntryList()){
                entryDTO.setSceneVoucherId(voucherEntity.getId());
                SceneVoucherEntryEntity sceneVoucherEntryEntity = BeanUtil.copyProperties(entryDTO, SceneVoucherEntryEntity.class);
                sceneVoucherEntryEntity.setId(IdWorker.getId());
                sceneVoucherEntryEntity.setAssistFlags(entryDTO.getAssistFlags());
                sceneVoucherEntryEntityList.add(sceneVoucherEntryEntity);
                if (CollectionUtils.isEmpty(entryDTO.getConditionList())){
                    continue;
                }
                for (SceneVoucherConditionSaveDTO conditionDTO: entryDTO.getConditionList()){
                    SceneVoucherConditionEntity sceneVoucherConditionEntity = BeanUtil.copyProperties(conditionDTO, SceneVoucherConditionEntity.class);
                    sceneVoucherConditionEntity.setSceneVoucherEntryId(sceneVoucherEntryEntity.getId());
                    sceneVoucherConditionEntity.setId(IdWorker.getId());
                    sceneVoucherConditionEntityList.add(sceneVoucherConditionEntity);
                }
            }
        }
        //批量入库
        if (CollectionUtils.isNotEmpty(sceneVoucherEntityList)){
            sceneVoucherService.saveBatch(sceneVoucherEntityList);
        }
        if (CollectionUtils.isNotEmpty(sceneVoucherEntryEntityList)){
            sceneVoucherEntryService.saveBatch(sceneVoucherEntryEntityList);
        }
        if (CollectionUtils.isNotEmpty(sceneVoucherConditionEntityList)){
            sceneVoucherConditionService.saveBatch(sceneVoucherConditionEntityList);
        }
        deleteRedis(sceneId);
        return Boolean.TRUE;
    }


    /**
     * 删除旧的规则配置，包括凭证头、凭证行、凭证条件
     * @param sceneId
     * @return
     */
    private void removeOldRuleConfig(Long sceneId){
        List<SceneVoucherDTO> oldSceneVoucherDTOList = sceneVoucherService.listSceneVoucherDTOBySceneId(sceneId);
        if (CollectionUtils.isEmpty(oldSceneVoucherDTOList)){
            return;
        }
        List<Long> oldSceneVoucherIdList =  oldSceneVoucherDTOList.stream().map(SceneVoucherDTO::getId).collect(Collectors.toList());
        //删除凭证头
        sceneVoucherService.removeByIds(oldSceneVoucherIdList);

        List<SceneVoucherEntryEntity> oldEntryEntityList = sceneVoucherEntryService.list(Wrappers.<SceneVoucherEntryEntity>lambdaQuery()
                .in(SceneVoucherEntryEntity::getSceneVoucherId, oldSceneVoucherIdList));
        if (CollectionUtils.isEmpty(oldEntryEntityList)){
            return;
        }
        List<Long> oldSceneEntryIdList = oldEntryEntityList.stream().map(SceneVoucherEntryEntity::getId).collect(Collectors.toList());
        //删除凭证行
        sceneVoucherEntryService.removeByIds(oldSceneEntryIdList);

        //删除凭证行条件
        sceneVoucherConditionService.remove(Wrappers.<SceneVoucherConditionEntity>lambdaQuery()
                .in(SceneVoucherConditionEntity::getSceneVoucherEntryId, oldSceneEntryIdList));
    }

    public void deleteRedis(Long sceneId){
        SceneEntity sceneEntity = sceneMapper.selectById(sceneId);
        if (null == sceneEntity) {
            return;
        }
        redisService.deleteObject(String.format(RedisConstant.V_SCENE_CODE_RULE, sceneEntity.getSceneCode()));
    }

}

