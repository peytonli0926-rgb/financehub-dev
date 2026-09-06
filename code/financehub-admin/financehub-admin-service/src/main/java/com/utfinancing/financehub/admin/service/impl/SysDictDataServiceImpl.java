package com.utfinancing.financehub.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.common.core.constant.UserConstants;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.security.utils.DictUtils;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.admin.mapper.SysDictDataMapper;
import com.utfinancing.financehub.admin.service.ISysDictDataService;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 获取所有字典数据
     *
     * @return 字典数据集合信息Map
     */
    @Override
    public Map mapAllDictData() {
        SysDictData sysDictData = new SysDictData();
        sysDictData.setStatus("0");
        List<SysDictData> sysDictDataList = dictDataMapper.selectDictDataList(sysDictData);
        Map<String, LinkedHashMap<String, String>> result = new LinkedMap<>();
        Map<String, List<SysDictData>> collectMap = sysDictDataList.stream().filter(e -> StringUtils.isNotBlank(e.getDictType())).collect(Collectors.groupingBy(SysDictData::getDictType, Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    list.sort(Comparator.comparingLong(SysDictData::getDictSort));
                    return list;
                }
        )));
        collectMap.forEach((key, value) -> {
            LinkedHashMap<String, String> resultMap = value.stream()
                    .filter(e -> StringUtils.isNotBlank(e.getDictValue()) && UserConstants.DICT_NORMAL.equals(e.getStatus()))
                    .collect(LinkedHashMap::new,(map, item) -> map.put(item.getDictValue(), item.getDictLabel()),Map::putAll);
            result.put(key, resultMap);
        });

        return result;
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteDictDataById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        int count = dictDataMapper.countDictDataByTypeAndKey(data.getDictType(), data.getDictValue());
        if (count > 0){
            throw new ServiceException(StrUtil.format("参数键值[{}]重复", data.getDictValue()));
        }
        int row = dictDataMapper.insertDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(Long dictCode, SysDictData data) {
        data.setDictCode(dictCode);
        int row = dictDataMapper.updateDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
