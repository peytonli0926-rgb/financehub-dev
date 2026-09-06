package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSceneSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessSceneVO;
import com.utfinancing.financehub.engine.scene.entity.BusinessSceneEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description : BusinessScene服务类接口
 * @Modified :
 */
public interface IBusinessSceneService extends IService<BusinessSceneEntity> {

    Long saveBusinessScene(BusinessSceneSaveDTO dto);

    Boolean removeByBusinessId(Long id);

    Long updateBusinessScene(Long id, BusinessSceneDTO dto);

    BusinessSceneDTO getBusinessSceneDTOById(Long id);

    List<BusinessSceneDTO> getBusinessSceneDTOsByBusinessId(Long id);

    IPage<BusinessSceneVO> selectPage(BusinessSceneQueryDTO queryDTO);

}
