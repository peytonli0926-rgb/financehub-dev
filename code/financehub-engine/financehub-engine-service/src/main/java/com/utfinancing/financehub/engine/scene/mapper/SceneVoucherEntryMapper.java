package com.utfinancing.financehub.engine.scene.mapper;

import com.utfinancing.financehub.engine.finance.model.dto.SelectAssistFlagsBySceneDTO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 场景凭证分录配置; Mapper 接口
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
public interface SceneVoucherEntryMapper extends BaseMapper<SceneVoucherEntryEntity> {


    List<String> selectDistinctAssistFlagsBySceneCode(@Param("sceneCode")String sceneCode);

    List<SelectAssistFlagsBySceneDTO> selectAssistFlagsByScene();

}
