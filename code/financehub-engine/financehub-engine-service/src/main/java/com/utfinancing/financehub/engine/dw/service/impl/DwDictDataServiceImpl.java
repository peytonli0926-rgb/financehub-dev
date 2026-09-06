package com.utfinancing.financehub.engine.dw.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.dw.entity.DwDictDataEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwDictDataMapper;
import com.utfinancing.financehub.engine.dw.service.IDwDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :  DwDictData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwDictDataServiceImpl extends ServiceImpl<DwDictDataMapper, DwDictDataEntity> implements IDwDictDataService {

    private final DwDictDataMapper dwDictDataMapper;
    private final RedisService redisService;

    @Override
    public Map<String, Map<String, String>> selectAllDictMap() {
        Map<String, Map<String, String>> dictKeyGroupList = redisService.getCacheObject(RedisConstant.V_DW_DICT_DATA);
        if (dictKeyGroupList == null){
            List<DwDictDataEntity> entityList = this.list();
            dictKeyGroupList = entityList.stream().filter(e-> ObjectUtil.isNotNull(e.getVcZhi())).collect(Collectors.groupingBy(DwDictDataEntity::getVcFujdid, Collectors.toMap(e->e.getVcZhi(), e->e.getVcMingc())));
            redisService.setCacheObject(RedisConstant.V_DW_DICT_DATA, dictKeyGroupList, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
        }
        return dictKeyGroupList;
    }

    @Override
    public String getDictValueByKey(Map<String, Map<String, String>> allDataMap, String dictType, String key) {
        Map<String, String> groupMap = allDataMap.get(dictType);
        if (groupMap != null){
            return groupMap.get(key);
        }
        return null;
    }
}

