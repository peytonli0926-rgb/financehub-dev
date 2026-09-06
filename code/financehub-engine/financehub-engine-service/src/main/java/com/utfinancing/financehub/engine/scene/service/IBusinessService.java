package com.utfinancing.financehub.engine.scene.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.scene.model.dto.BusinessQueryDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessSaveDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessVO;
import com.utfinancing.financehub.engine.scene.entity.BusinessEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : hzhao
 * @Date : Create in 2023-08-30
 * @Description : Business服务类接口
 * @Modified :
 */
public interface IBusinessService extends IService<BusinessEntity> {

    Long saveBusiness(BusinessSaveDTO dto);

    Long updateBusiness(Long id, BusinessDTO dto);

    BusinessDTO getBusinessDTOById(Long id);

    IPage<BusinessVO> selectPage(BusinessQueryDTO queryDTO);

    List<BusinessVO> selectAll(BusinessQueryDTO queryDTO);

    void addSceneCountByBusinessId(Long id);

    void substractSceneCountByBusinessId(Long id);

    /**
     * 根据编码集合返回: Map<编码, DTO对象>
     * @param businessCodeList
     * @return
     */
    Map<String, BusinessDTO> getMapByCode(List<String> businessCodeList);

    BusinessDTO getBusinessByCode(String businessCode);

    List<BusinessDTO> queryAll();

}
