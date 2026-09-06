package com.utfinancing.financehub.engine.dw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.dw.entity.DwDictDataEntity;

import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : DwDictData服务类接口
 * @Modified :
 */
public interface IDwDictDataService extends IService<DwDictDataEntity> {

    Map<String, Map<String, String>> selectAllDictMap();

    String getDictValueByKey(Map<String, Map<String, String>> allDataMap, String dictType, String key);
}
