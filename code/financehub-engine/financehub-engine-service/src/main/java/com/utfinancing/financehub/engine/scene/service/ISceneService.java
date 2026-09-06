package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVO;
import com.utfinancing.financehub.engine.scene.entity.SceneEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : Scene服务类接口
 * @Modified :
 */
public interface ISceneService extends IService<SceneEntity> {

    Long saveScene(SceneSaveDTO dto);

    Long updateScene(Long id, SceneSaveDTO dto);

    SceneDTO getSceneDTOById(Long id);

    SceneDTO getSceneDTOByCode(String sceneCode);

    IPage<SceneVO> selectPage(SceneQueryDTO queryDTO);

    List<SceneVO> selectAll(SceneQueryDTO queryDTO);

    List<SceneRuleDTO> listRuleListBySceneId(Long sceneId);

    SceneRuleConfigDTO getSceneRuleConfigById(Long sceneId);

    Boolean saveSceneRuleConfig(SceneRuleConfigSaveDTO saveDTO);

    List<SceneRuleDTO> getSceneRuleDTOByCode(String sceneCode);
}
