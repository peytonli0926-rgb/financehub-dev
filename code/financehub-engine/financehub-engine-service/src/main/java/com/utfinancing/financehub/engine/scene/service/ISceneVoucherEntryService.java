package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.SelectAssistFlagsBySceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherEntrySaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherEntryVO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntryEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : SceneVoucherEntry服务类接口
 * @Modified :
 */
public interface ISceneVoucherEntryService extends IService<SceneVoucherEntryEntity> {

    Long saveSceneVoucherEntry(SceneVoucherEntrySaveDTO dto);

    Long updateSceneVoucherEntry(Long id, SceneVoucherEntrySaveDTO dto);

    SceneVoucherEntryDTO getSceneVoucherEntryDTOById(Long id);

    List<SceneVoucherEntryDTO> listEntryBySceneVoucherId(Long sceneVoucherId);

    void deleteSceneVoucherEntryByIds(List<Long> id);

    IPage<SceneVoucherEntryVO> selectPage(SceneVoucherEntryQueryDTO queryDTO);

    List<String> selectDistinctAssistFlagsBySceneCode(String sceneCode);

    public Map<String, List<SelectAssistFlagsBySceneDTO>> selectAllSceneAssistFlags();

}
