package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneVoucherSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.SceneVoucherVO;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description : SceneVoucher服务类接口
 * @Modified :
 */
public interface ISceneVoucherService extends IService<SceneVoucherEntity> {

    Long saveSceneVoucher(SceneVoucherSaveDTO dto);

    Long updateSceneVoucher(Long id, SceneVoucherSaveDTO dto);

    SceneVoucherDTO getSceneVoucherDTOById(Long id);

    List<SceneVoucherDTO> listSceneVoucherDTOBySceneId(Long sceneId);

    IPage<SceneVoucherVO> selectPage(SceneVoucherQueryDTO queryDTO);

}
