package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherConditionSaveDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherConditionVO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherConditionEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : SceneVoucherCondition服务类接口
 * @Modified :
 */
public interface ISceneVoucherConditionService extends IService<SceneVoucherConditionEntity> {

    Long saveSceneVoucherCondition(SceneVoucherConditionSaveDTO dto);

    Long updateSceneVoucherCondition(Long id, SceneVoucherConditionSaveDTO dto);

    SceneVoucherConditionDTO getSceneVoucherConditionDTOById(Long id);

    List<SceneVoucherConditionDTO> getSceneVoucherConditionDTOsByEntryId(Long id);

    void deleteSceneVoucherConditionBySceneIds(List<Long> id);

    IPage<SceneVoucherConditionVO> selectPage(SceneVoucherConditionQueryDTO queryDTO);

}
