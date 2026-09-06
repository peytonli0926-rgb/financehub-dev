package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanySaveDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-12
 * @Description : OrgCompany服务类接口
 * @Modified :
 */
public interface IOrgCompanyService extends IService<OrgCompanyEntity> {

    Long saveOrgCompany(OrgCompanySaveDTO dto);

    Long updateOrgCompany(Long id, OrgCompanySaveDTO dto);

    OrgCompanyDTO getOrgCompanyDTOById(Long id);

    IPage<OrgCompanyVO> selectPage(OrgCompanyQueryDTO queryDTO);

    List<OrgCompanyVO> selectByCondition(OrgCompanyQueryDTO queryDTO);

    List<OrgCompanyVO> selectAllOrgIdAndName();

    /**
     * 存在旧签约主体，则返回old签约主体
     */
    public String getOldOrgId(String newOrgId);
}
