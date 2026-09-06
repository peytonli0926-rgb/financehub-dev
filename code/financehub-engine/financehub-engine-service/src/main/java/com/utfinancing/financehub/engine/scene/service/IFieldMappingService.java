package com.utfinancing.financehub.engine.scene.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.model.dto.FieldMappingApiDTO;
import com.utfinancing.financehub.engine.model.vo.FieldMappingApiVO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingDTO;
import com.utfinancing.financehub.engine.scene.model.dto.FieldMappingSaveDTO;
import com.utfinancing.financehub.engine.scene.model.dto.MappingDTO;
import com.utfinancing.financehub.engine.scene.model.vo.FieldMappingVO;
import com.utfinancing.financehub.engine.scene.entity.FieldMappingEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-27
 * @Description : FieldMapping服务类接口
 * @Modified :
 */
public interface IFieldMappingService extends IService<FieldMappingEntity> {

    Long saveFieldMapping(FieldMappingSaveDTO dto);

    Long updateFieldMapping(Long id, FieldMappingSaveDTO dto);

    FieldMappingDTO getFieldMappingDTOById(Long id);

    IPage<FieldMappingVO> selectPage(FieldMappingQueryDTO queryDTO);

    /**
     * 查询所有的映射关系
     * @return 数据结构：Map<systemCode, <fieldCode, [{source, target}]>>
     */
    Map<String, Map<String, List<MappingDTO>>> queryAllFieldMapping();


    void convertDataFromMapping(JSONObject jsonObject);

    void cleanCache();

    List<FieldMappingApiVO> selectFieldMappingByCondition(FieldMappingApiDTO queryDTO);

    Map<String, Map<String, List<MappingDTO>>> queryAllMappingFromDB();
}
