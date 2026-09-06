package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO;
import com.utfinancing.financehub.engine.model.vo.FieldMappingApiVO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingDTO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingSaveDTO;
import com.utfinancing.financehub.engine.scene.model.dto.MappingDTO;
import com.utfinancing.financehub.engine.scene.model.vo.FieldMappingVO;
import com.utfinancing.financehub.engine.scene.entity.FieldMappingEntity;
import com.utfinancing.financehub.engine.scene.mapper.FieldMappingMapper;
import com.utfinancing.financehub.engine.scene.service.IFieldMappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-27
 * @Description :  FieldMapping服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FieldMappingServiceImpl extends ServiceImpl<FieldMappingMapper, FieldMappingEntity> implements IFieldMappingService {

    private final FieldMappingMapper fieldMappingMapper;
    private final RedisService redisService;

    private final static String REDIS_KEY_FIELD_MAPPING = "_FIELD_MAPPING_DATA";

    @Override
    public Long saveFieldMapping(FieldMappingSaveDTO dto) {
        FieldMappingEntity entity = BeanUtil.copyProperties(dto, FieldMappingEntity.class);
        this.save(entity);
        //删除缓存
        redisService.deleteObject(REDIS_KEY_FIELD_MAPPING);
        return entity.getId();
    }

    @Override
    public Long updateFieldMapping(Long id, FieldMappingSaveDTO dto) {
        FieldMappingEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        //删除缓存
        redisService.deleteObject(REDIS_KEY_FIELD_MAPPING);
        return id;
    }

    @Override
    public FieldMappingDTO getFieldMappingDTOById(Long id) {
        FieldMappingEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FieldMappingDTO.class);
    }

    @Override
    public IPage<FieldMappingVO> selectPage(FieldMappingQueryDTO queryDTO) {
        LambdaQueryWrapper<FieldMappingEntity> queryWrapper = Wrappers.<FieldMappingEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getSystemCode()), FieldMappingEntity::getSystemCode, queryDTO.getSystemCode());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getFieldCode()), FieldMappingEntity::getFieldCode, queryDTO.getSystemCode());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getFieldName()), FieldMappingEntity::getFieldName, queryDTO.getFieldName());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getSourceValue()), FieldMappingEntity::getSourceValue, queryDTO.getSourceValue());
        IPage<FieldMappingEntity> entityIPage = fieldMappingMapper.selectPage(new Page<FieldMappingEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FieldMappingVO.class);
    }

    @Override
    public Map<String, Map<String, List<MappingDTO>>> queryAllFieldMapping() {
        Map<String, Map<String, List<MappingDTO>>> cacheMap = redisService.getCacheObject(REDIS_KEY_FIELD_MAPPING);
        if (cacheMap == null){
            cacheMap = this.queryAllMappingFromDB();
            redisService.setCacheObject(REDIS_KEY_FIELD_MAPPING, cacheMap, 60L, TimeUnit.MINUTES); //过期时间：60分钟
        }
        return cacheMap;
    }

    @Override
    public void convertDataFromMapping(JSONObject jsonObject) {
        log.info("解析数据{}",jsonObject);
        String systemCode = jsonObject.getString("systemCode");
        String sceneCode = jsonObject.getString("sceneCode");
        //原始场景code
        jsonObject.put(RuleConstant.FIELD_SCENE_CODE_ORIGINAL,sceneCode);
        Map<String, Map<String, List<MappingDTO>>> allFieldMapping =  queryAllFieldMapping();
        Map<String, List<MappingDTO>> systemMapping = allFieldMapping.get(systemCode);
        if (systemMapping == null){
            return;
        }
        for (Map.Entry<String, List<MappingDTO>> systemEntry: systemMapping.entrySet()){
            String fieldCode = systemEntry.getKey();
            if (jsonObject.containsKey(fieldCode)){
                List<MappingDTO> fieldList = systemEntry.getValue();
                for (MappingDTO fieldMap: fieldList){
                    if (StrUtil.split(fieldMap.getSourceValue(),",").contains(jsonObject.getString(fieldCode))){
                        if (StrUtil.isNotBlank(fieldMap.getTargetFieldCode())) {
                            jsonObject.put(fieldMap.getTargetFieldCode(), fieldMap.getTargetValue());
                        } else {
                            jsonObject.put(fieldCode, fieldMap.getTargetValue());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void cleanCache() {
        redisService.deleteObject(REDIS_KEY_FIELD_MAPPING);
    }

    @Override
    public List<FieldMappingApiVO> selectFieldMappingByCondition(FieldMappingApiDTO queryDTO) {
        LambdaQueryWrapper<FieldMappingEntity> queryWrapper = Wrappers.<FieldMappingEntity>lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getSystemCode())) {
            queryWrapper.eq(FieldMappingEntity::getSystemCode, queryDTO.getSystemCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getFieldCode())) {
            queryWrapper.eq(FieldMappingEntity::getFieldCode, queryDTO.getFieldCode());
        }
        return BeanUtil.copyToList(list(queryWrapper),FieldMappingApiVO.class);
    }

    @Override
    public Map<String, Map<String, List<MappingDTO>>> queryAllMappingFromDB(){
        List<FieldMappingEntity> entityList = this.list();
        Map<String, List<FieldMappingEntity>> systemCodeMap = entityList.stream().collect(Collectors.groupingBy(e -> e.getSystemCode()));
        Map<String, Map<String, List<MappingDTO>>> resultMap = new HashMap<>(200);
        for (Map.Entry<String, List<FieldMappingEntity>> sysCodeEntry : systemCodeMap.entrySet()) {
            Map<String, List<MappingDTO>> mappingMap = sysCodeEntry.getValue().stream().map(e -> {
                MappingDTO mappingDTO = BeanUtil.copyProperties(e, MappingDTO.class);
                return mappingDTO;
            }).collect(Collectors.groupingBy(e -> e.getFieldCode()));
            resultMap.put(sysCodeEntry.getKey(), mappingMap);
        }
        return resultMap;
    }




}

