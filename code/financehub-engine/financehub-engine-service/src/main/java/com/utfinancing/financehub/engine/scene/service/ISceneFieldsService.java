package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.SceneFieldsVO;
import com.utfinancing.financehub.engine.scene.entity.SceneFieldsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : SceneFields服务类接口
 * @Modified :
 */
public interface ISceneFieldsService extends IService<SceneFieldsEntity> {

    Long saveSceneFields(SceneFieldsSaveDTO dto);

    Long updateSceneFields(Long id, SceneFieldsSaveDTO dto);

    Boolean removeSceneField(Long id);

    SceneFieldsDTO getSceneFieldsDTOById(Long id);

    IPage<SceneFieldsVO> selectPage(SceneFieldsQueryDTO queryDTO);

    /**
     * 根据场景编码查询场景字段列表
     * @param sceneCode
     * @return
     */
    List<SceneFieldsDTO> listSceneFieldsByCode(String sceneCode);


    /**
     * 根据场景编码长袖字段 Map<fieldName， fieldCode>
     * @param sceneCode
     * @return
     */
    Map<String, Object> selectSceneFieldsMapByCode(String sceneCode);

    List<EditorOptionDTO> getTreeBySceneId(Long sceneId);

    List<EditorOptionDTO> listEditorOption(Long sceneId);

    /**
     * 查询去重的所有字段列表，用于字段选择下拉框
     * @return
     */
    List<SceneFieldsDTO> listAllSceneFieldsDistinct();

    /**
     * 获取数字类型字段
     * @param sceneCode
     * @return
     */
    List<SceneFieldsDTO> selectNumberSceneFields(String sceneCode);

    /**
     * 获取数组类型字段
     * @param sceneCode
     * @return
     */
    List<SceneFieldsDTO> selectListSceneFields(String sceneCode);
}
