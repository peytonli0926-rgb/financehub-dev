package com.utfinancing.financehub.engine.integration.service;

import com.utfinancing.financehub.engine.integration.model.dto.*;

import java.util.List;

public interface IOrgIntegrationService {


    /**
     * 2.1.1、 查询部门全量信息
     * @return
     */
    List<OrgDTO> queryAll();


    /**
     * 查询一级部门信息
     * @return
     */
    List<OrgDTO> queryFirst();

    /**
     *
     * @param queryDTO
     * @return
     */
    List<OrgDTO> queryOrgList(OrgQueryDTO queryDTO);

    /**
     * 批量查询部门下的岗位信息
     */
    List<PositionDTO> queryOrgPositionList(String orgCode);

    /**
     * 批量查询部门下的员工信息
     */
    List<StaffDTO> queryOrgStaffList(String orgCode);


    /**
     * 批量查询岗位信息
     */
    List<PositionDTO> queryPositionList(PositionQueryDTO queryDTO);


    /**
     * 批量查询员工信息
     * @return
     */
    List<StaffDTO> queryStaffList(StaffQueryDTO queryDTO);

}
